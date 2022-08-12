package me.spthiel.klacaiba.config;

import net.eq2online.macros.core.Macros;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

public class ConfigManager {
	
	private static ConfigManager instance = new ConfigManager();
	
	public static ConfigManager getInstance() {
		return instance;
	}
	
	private HashMap<String, HashMap<String, String>> cache = new HashMap<>();
	
	public HashMap<String, String> getConfig(String path) {
		if (cache.containsKey(path)) {
			return cache.get(path);
		}
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(getConfigFile(path)))) {
			
				HashMap<String, String> out = new HashMap<>();
				
				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					String[] splitted = line.split("=");
					out.put(splitted[0], splitted[1]);
				}
				cache.put(path, out);
				return out;
			
		} catch (IOException e) {
		}
		return null;
	}
	
	private File getConfigFile(String path) {
		return new File(getConfigFolder(), path);
	}
	
	private File getConfigFolder() {
		return new File(String.join(File.separator, new String[]{Macros.getInstance().getMacrosDirectory().getAbsolutePath(), "config", "klacaiba"}));
	}
	
	public void setConfig(String path, HashMap<String, String> config) {
	
	}
	
	public void setKey(String path, String key, Object value) {
	
	}
	
}
