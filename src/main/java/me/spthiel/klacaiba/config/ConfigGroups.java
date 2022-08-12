package me.spthiel.klacaiba.config;

public enum ConfigGroups {

	UTILITIES("Utility functions"),
	EXTERNALS("External data access"),
	MOD("Mod interactions"),
	PLAYER("Player interactions"),
	WORLD("World interactions"),
	EVENTS("Events"),
	VARIABLES("Variables"),
	;
	
	private String name;
	
	ConfigGroups(String name) {
		
		this.name = name;
	}
	
	public String getName() {
		
		return name;
	}
}
