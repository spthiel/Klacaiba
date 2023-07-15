package me.spthiel.klacaiba.module.events;

import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.api.IScriptedIterator;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.module.actions.base.IDocumentable;

public class Await extends ScriptAction implements IDocumentable {
	
	public Await() {
		
		super(ScriptContext.MAIN, "await");
	}
	
	@Override
	public boolean canExecuteNow(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		IScriptedIterator state = (IScriptedIterator) macro.getState("pollevent");
		return state == null || state.isActive();
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "await";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Closes PollEvent stack";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "";
	}
}
