package me.spthiel.klacaiba.newactions.without;

import net.eq2online.macros.compatibility.I18n;
import net.eq2online.macros.scripting.Variable;
import net.eq2online.macros.scripting.actions.lang.ScriptActionDo;
import net.eq2online.macros.scripting.actions.lang.ScriptActionNext;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IScriptAction;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.eq2online.util.Game;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class For extends ScriptActionDo {
	private static final Pattern EXPRESSIVE = Pattern.compile("^(.+?)=(.+?) to (.+?)( step (.+?))?$", Pattern.CASE_INSENSITIVE);

	public For() {
		super(ScriptContext.MAIN, "for");
	}

	public String getExpectedPopCommands() {
		return I18n.get("script.error.stackhint", this, "NEXT");
	}

	public boolean executeStackPush(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {

		Class stateClass = ScriptActionDo.class.getDeclaredClasses()[0];

		Constructor constructor;
		try {
			constructor = stateClass.getDeclaredConstructor(String.class, int.class, int.class, int.class);
			constructor.setAccessible(true);
		} catch (NoSuchMethodException e) {
			Game.addChatMessage("Something went wrong in the for loop (couldn't get constructor) please report this to spthiel with the attachment of your log file");
			e.printStackTrace();
			return false;
		}

		Object state = instance.getState();
		if (params.length <= 0) {
			return false;
		} else {
			if (state == null) {
				String variableName = params[0].toLowerCase();
				String strFrom = null;
				String strTo = null;
				String strStep = null;
				Matcher m = EXPRESSIVE.matcher(variableName);
				if (params.length > 2) {
					strFrom = provider.expand(macro, params[1], false);
					strTo = provider.expand(macro, params[2], false);
					if (params.length > 3) {
						strStep = provider.expand(macro, params[3], false);
					}
				} else if (m.matches()) {
					variableName = m.group(1).trim();
					strFrom = provider.expand(macro, m.group(2).trim(), false);
					strTo = provider.expand(macro, m.group(3).trim(), false);
					strStep = m.group(5) != null ? provider.expand(macro, m.group(5).trim(), false) : null;
				}

				if (strFrom == null || strTo == null || !Variable.isValidScalarVariableName(variableName)) {
					return false;
				}

				int from = ScriptCore.tryParseInt(strFrom, 0);
				int to = ScriptCore.tryParseInt(strTo, 0);
				int step = ScriptCore.tryParseInt(strStep, 1);
				try {
					state = constructor.newInstance(variableName, from, to, step);
				} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
					Game.addChatMessage("Something went wrong in the for loop (" + e.getMessage() + ") please report this to spthiel with the attachment of your log file");
					e.printStackTrace();
				}
				instance.setState(state);
			} else {

				Object o = getAndInvokeMethod("increment",state);

			}

			Object isActiveO = getAndInvokeMethod("isActive",state);
			if(isActiveO == null)
				return false;
			if(!(isActiveO instanceof Boolean)) {
				Game.addChatMessage("Something went wrong in the for loop (isActive is not a boolean) please report this to spthiel with the attachment of your log file");
				return false;
			}

			Object getVariableName = getAndInvokeMethod("getVariableName",state);
			Object getCounter = getAndInvokeMethod("getCounter",state);

			if(getVariableName == null || getCounter == null)
				return false;

			if(!(getVariableName instanceof String)) {
				Game.addChatMessage("Something went wrong in the for loop (getVariableName is not a string) please report this to spthiel with the attachment of your log file");
				System.out.println("GetVariable: " + getVariableName.getClass().getName());
				return false;
			}

			if(!(getCounter instanceof Integer)) {
				Game.addChatMessage("Something went wrong in the for loop (getCounter is not an integer) please report this to spthiel with the attachment of your log file");
				System.out.println("GetCounter: " + getCounter.getClass().getName());
				return false;
			}

			if ((boolean) isActiveO) {
				provider.setVariable(macro, (String)getVariableName, (int)getCounter);
			}

			return (boolean) isActiveO;
		}
	}

	public boolean canBePoppedBy(IScriptAction action) {
		return action instanceof ScriptActionNext;
	}

	private Object getAndInvokeMethod(String name, Object state, Object... args) {
		Method method = getMethod(name,state);
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
