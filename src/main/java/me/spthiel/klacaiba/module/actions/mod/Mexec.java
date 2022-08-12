package me.spthiel.klacaiba.module.actions.mod;

import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;

public class Mexec extends ScriptAction {
	
	public Mexec() {
		
		super(ScriptContext.MAIN, "mexec");
	}
	
	@Override
	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		return new ReturnValue("Nope");
	}
}