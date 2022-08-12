package me.spthiel.klacaiba.module.actions.language;

import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.base.BaseScriptAction;

public class Map extends BaseScriptAction {
	
	public Map() {
		
		super("map");
	}
	
	@Override
	public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		if(params.length < 5) {
			return new ReturnValue(0);
		}
		
		int x = getIntOrDefault(provider, macro, params[0], 0);
		int fmin = getIntOrDefault(provider, macro, params[1], 0);
		int fmax = getIntOrDefault(provider, macro, params[2], 0);
		int tmin = getIntOrDefault(provider, macro, params[3], 0);
		int tmax = getIntOrDefault(provider, macro, params[4], 0);
		
		return new ReturnValue((int)(((double)x-fmin)/(fmax-fmin)*((double)tmax-tmin))+tmin);
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "#result = map(<x>,<minfrom>,<maxfrom>,<minto>,<maxto>";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "maps the value x from minfrom-maxfrom to minto-maxto";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "mapped value";
	}
}
