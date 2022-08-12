package me.spthiel.klacaiba.module.actions.information;

import net.eq2online.macros.scripting.ScriptActionProvider;
import net.eq2online.macros.scripting.Variable;
import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.base.BaseScriptAction;

public class Mod extends BaseScriptAction {
	
	public Mod() {
		super("mod");
	}
	
	@Override
	public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		if (params.length <= 1) {
			return new ReturnValue("Invalid arguments");
		}
		
		String varName;
		int offset = 0;
		if (params.length > 2) {
			varName = Variable.getValidVariableOrArraySpecifier(params[0]);
			offset = 1;
		} else {
			varName = null;
		}
		int dividend = getIntOrDefault(provider, macro, params[offset], 0);
		int divisor = getIntOrDefault(provider, macro, params[offset+1], 0);
		if (varName != null) {
			provider.setVariable(macro, varName, dividend % divisor);
		}
		return new ReturnValue(dividend % divisor);
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "[#result =] MOD([#result],<dividend>,<divisor>)";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Performs the modulo operation on the given values";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Result of the modulo operation";
	}
}
