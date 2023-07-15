package me.spthiel.klacaiba.module.actions.information.external;

import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.config.ConfigGroups;
import me.spthiel.klacaiba.module.actions.base.BaseScriptAction;

public class Unix extends BaseScriptAction {
	
	public Unix() {
		super("unix");
	}
	
	@Override
	public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		if (params.length == 0) {
			return new ReturnValue("Invalid arguments");
		}
		
		String secondsVar = null;
		String msVar;
		
		if (params.length > 1) {
			secondsVar = provider.expand(macro, params[0], false);
			msVar = provider.expand(macro, params[1], false);
		} else {
			msVar = provider.expand(macro, params[0], false);
		}
		
		provider.setVariable(macro, msVar, (int)(System.currentTimeMillis() % 1000));
		if (secondsVar != null) {
			provider.setVariable(macro, secondsVar, (int) (System.currentTimeMillis() / 1000));
		}
		return null;
		
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "unix([#seconds],<#milliseconds>)";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Gets the current time with milliseconds";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "";
	}
	
	@Override
	public ConfigGroups getGroup() {
		
		return ConfigGroups.EXTERNALS;
	}
}
