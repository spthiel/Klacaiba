package me.spthiel.klacaiba.module.actions.base;

import me.spthiel.klacaiba.config.ConfigGroups;
import me.spthiel.klacaiba.config.ConfigOptionList;

public interface IConfigurable {
	
	ConfigGroups getGroup();
	ConfigOptionList getOptions();
	
}
