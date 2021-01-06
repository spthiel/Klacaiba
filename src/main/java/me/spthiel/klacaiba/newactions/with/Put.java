package me.spthiel.klacaiba.newactions.with;

import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;

import javax.annotation.Nonnull;

public class Put extends Push {
	
	public Put() {
		
		super("put");
	}
	
	@Override
	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		if (params.length > 1) {
			String arrayName = provider.expand(macro, params[0], false);
			
			for (int i = 1 ; i < params.length ; i++) {
				evaluateParam(provider, macro, provider.expand(macro, params[i], false), (value) -> provider.putValueToArray(macro, arrayName, value.toString()));
			}
			
		}
		
		return null;
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "PUT(<array>,<...values>)";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Inserts value[s] at the first empty point in array. Values may be constants, array[*] to copy an entire array or array[a:b:step] to copy a <= indexes < b to new array. Negative values wrap around.";
	}
}
