package me.spthiel.klacaiba.base;

import net.eq2online.macros.scripting.api.*;

import me.spthiel.klacaiba.config.ConfigOptionList;

public abstract class SimpleToggleableAction extends BaseScriptAction implements Configurable {
	
	private IScriptAction parent;
	
	public SimpleToggleableAction(String actionName) {
		
		super(actionName);
	}
	
	private void setParent(IScriptAction parent) {
		this.parent = parent;
	}
	
	@Override
	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		return this.run(provider, macro, instance, rawParams, params);
	}
	
	@Override
	public ConfigOptionList getOptions() {
		
		return null;
	}
}
