package me.spthiel.klacaiba.base;

import me.spthiel.klacaiba.config.ConfigGroups;
import me.spthiel.klacaiba.config.ConfigOptionList;

public interface Configurable {
	
	public ConfigGroups getGroup();
	public ConfigOptionList getOptions();
	
}
