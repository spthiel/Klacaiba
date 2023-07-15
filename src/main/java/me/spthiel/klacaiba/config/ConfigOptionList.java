package me.spthiel.klacaiba.config;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import me.spthiel.klacaiba.config.configOptions.OptionGroup;

public class ConfigOptionList {
	
	private HashMap<OptionGroup<?>, Object> options = new HashMap<>();
	
	public void addOption(OptionGroup<?> option) {
		this.options.put(option, null);
		option.onChange((value) -> {
			options.put(option, value);
		});
	}
	
	public Set<OptionGroup<?>> getOptions() {
		return options.keySet();
	}
	
	public void getValue() {
	
	}
	
}
