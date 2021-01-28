package me.spthiel.klacaiba.module.actions.language;

import net.eq2online.macros.scripting.Variable;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.base.BaseScriptAction;
import me.spthiel.klacaiba.utils.Utils;

public class Stop extends BaseScriptAction {
	
	public Stop() {
		
		super("stop");
	}
	
	@Override
	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		if (params.length > 0) {
			String string = provider.expand(macro, params[0], false);
			if ("all".equalsIgnoreCase(string) || "*".equals(string)) {
				provider.actionStopMacros();
				return null;
			}
			
			String arrayName;
			if ((arrayName = Variable.getValidVariableOrArraySpecifier(string)) != null) {
				if ((arrayName.startsWith("@&") || arrayName.startsWith("&")) && provider.getArrayExists(macro, arrayName)) {
					
					Utils.forEachArray(provider, macro, arrayName, (object) -> {
						String element = (String) object;
						int    macroid = provider.getMacroEngine().getMacroIdForName(string);
						if (macroid > 0) {
							provider.actionStopMacros(macro, macroid);
						}
					});
					
				}
				return null;
			}
			
			provider.getMacroEngine().getExecutingMacroStatus()
					.forEach(
							macroStatus -> {
								String macroID = macroStatus.getMacro().getDisplayName();
								if (macroID.matches(string)) {
									provider.actionStopMacros(macro, macroStatus.getMacro().getID());
								}
							}
							);
			
		} else {
			provider.actionStopMacros(macro, macro.getID());
		}
		
		return null;
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "STOP([array|regex])";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Stops the current macro, or macros matching the regex or values of the array";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "";
	}
}
