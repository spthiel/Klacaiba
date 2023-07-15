package me.spthiel.klacaiba.base.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.spthiel.klacaiba.config.ConfigGroups;
import me.spthiel.klacaiba.config.configOptions.OptionGroup;
import me.spthiel.klacaiba.config.gui.GuiKlacaibaConfigPanel;

public class ConfigurationManager {
	
	private       ConfigGroups                                selected       = ConfigGroups.values()[0];
	private final HashMap<ConfigGroups, List<OptionGroup<?>>> configElements = new HashMap<>();
	private       GuiKlacaibaConfigPanel                      configPanel;
	
	public ConfigurationManager() {
	
	}
	
	public void registerConfigPanel(GuiKlacaibaConfigPanel configPanel) {
		
		this.selected = ConfigGroups.values()[0];
		this.configPanel = configPanel;
	}
	
	public void addOptionGroup(ConfigGroups group, OptionGroup<?> optionGroup) {
		this.configElements.putIfAbsent(group, new ArrayList<>());
		this.configElements.get(group).add(optionGroup);
	}
	
	public Object getValue(String key) {
		
		return null;
	}
	
	public boolean getBool(String key) {
		
		return true;
	}
	
	public ConfigGroups getSelected() {
		
		return selected;
	}
	
	public void setSelected(ConfigGroups selected) {
		
		this.selected = selected;
	}
	
	public HashMap<ConfigGroups, List<OptionGroup<?>>> getConfigElements() {
		
		return configElements;
	}
}
