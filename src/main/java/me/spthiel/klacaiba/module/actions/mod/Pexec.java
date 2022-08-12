package me.spthiel.klacaiba.module.actions.mod;

import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;

public class Pexec extends ScriptAction {
	
	public Pexec() {
		
		super(ScriptContext.MAIN, "pexec");
	}
	
	@Override
	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		return new ReturnValue("Nope");
	}
}
