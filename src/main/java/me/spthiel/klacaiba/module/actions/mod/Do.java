package me.spthiel.klacaiba.module.actions.mod;

import net.eq2online.macros.scripting.actions.lang.ScriptActionDo;
import net.eq2online.macros.scripting.actions.lang.ScriptActionUntil;
import net.eq2online.macros.scripting.actions.lang.ScriptActionWhile;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.util.Game;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Do extends ScriptActionDo {
	
	public Do() {
		
		super(ScriptContext.MAIN);
	}
	
	protected Do(ScriptContext context, String actionName) {
		
		super(context, actionName);
	}
	
	@Override
	public boolean executeStackPop(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params, IMacroAction popAction) {
		
		Class stateClass = ScriptActionDo.class.getDeclaredClasses()[0];
		
		Constructor constructor;
		try {
			constructor = stateClass.getDeclaredConstructor(int.class);
			constructor.setAccessible(true);
		} catch (NoSuchMethodException e) {
			Game.addChatMessage("Something went wrong in the do loop (couldn't get constructor) please report this to spthiel with the attachment of your log file");
			e.printStackTrace();
			return false;
		}
		
		if (popAction.getAction() instanceof ScriptActionWhile || popAction.getAction() instanceof ScriptActionUntil) {
			Object state = instance.getState();
			Object isActiveO = getAndInvokeMethod("isActive",state);
			if(isActiveO == null)
				return false;
			if(!(isActiveO instanceof Boolean)) {
				Game.addChatMessage("Something went wrong in the do loop (isActive is not a boolean) please report this to Elspeth with the attachment of your log file");
				return false;
			}
			if (state == null || (boolean)isActiveO) {
				boolean result;
				if (popAction.getAction().getClass().equals(ScriptActionWhile.class) || popAction.getAction().getClass().equals(ScriptActionUntil.class)) {
					result = provider.getExpressionEvaluator(macro, provider.expand(macro, popAction.getRawParams(), true))
											 .evaluate() != 0;
					if (popAction.getAction() instanceof ScriptActionWhile) {
						result = !result;
					}
				} else {
					result = popAction.getAction().executeConditional(provider, macro, popAction, popAction.getRawParams(), popAction.getParams());
				}
				
				if (result) {
					if (state == null) {
						try {
							state = constructor.newInstance(0);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
							Game.addChatMessage("Something went wrong in the do loop (" + e.getMessage() + ") please report this to Elspeth with the attachment of your log file");
							e.printStackTrace();
						}
						instance.setState(state);
					}
					
					getAndInvokeMethod("cancel", state);
					return true;
				}
			}
		}
		
		return false;
	}
	
	private Object getAndInvokeMethod(String name, Object state, Object... args) {
		Method method = getMethod(name, state);
		if(method != null) {
			try {
				return method.invoke(state, args);
			} catch (IllegalAccessException e) {
				Game.addChatMessage("Something went wrong in the for loop (couldn't access " + name + ") please report this to spthiel with the attachment of your log file");
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				Game.addChatMessage("Something went wrong in the for loop (couldn't invoke " + name + ") please report this to spthiel with the attachment of your log file");
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private Method getMethod(String name, Object state) {
		
		try {
			Method f = state.getClass().getDeclaredMethod(name);
			f.setAccessible(true);
			return f;
		} catch (NoSuchMethodException e) {
			Game.addChatMessage("Something went wrong in the for loop (couldn't find " + name + ") please report this to spthiel with the attachment of your log file");
			e.printStackTrace();
		}
		return null;
		
	}
}
