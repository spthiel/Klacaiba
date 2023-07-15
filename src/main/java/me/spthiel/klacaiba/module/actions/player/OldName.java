package me.spthiel.klacaiba.module.actions.player;

import net.eq2online.macros.scripting.Variable;
import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import me.spthiel.klacaiba.config.ConfigGroups;
import me.spthiel.klacaiba.module.actions.base.BaseScriptAction;
import me.spthiel.klacaiba.utils.Json;

public class OldName extends BaseScriptAction {
	
	public OldName() {
		super("oldname");
	}
	
	@Override
	public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		if (params.length == 0) {
			return new ReturnValue("Invalid arguments");
		}
		
		String arrayName;
		String input;
		if (params.length > 1) {
			arrayName = Variable.getValidVariableOrArraySpecifier(params[0]);
			input = provider.expand(macro, params[1], false);
		} else {
			arrayName = null;
			input = provider.expand(macro, params[0], false);
		}
		
		String[] names;
		try {
			String uuid = "";
			if (!input.matches("[0-9a-f]{8}-?[0-9a-f]{4}-?[0-9a-f]{4}-?[0-9a-f]{4}-?[0-9a-f]{12}")) {
				uuid = uuid(sendGet("https://api.mojang.com/users/profiles/minecraft/" + input));
				if (uuid == null) {
					uuid = uuid(sendGet("https://api.mojang.com/users/profiles/minecraft/" + input + "?at=0"));
					if (uuid == null) {
						uuid = uuid(sendGet("https://api.mojang.com/users/profiles/minecraft/" + input + "?at=" + System.currentTimeMillis()/1000));
					}
				}
			} else {
				uuid = input.replace("-","");
			}
			Json json = new Json(sendGet("https://api.mojang.com/user/profiles/" + uuid + "/names"));
			
			names = json.getAll("name");
		} catch (Exception e) {
			e.printStackTrace();
			names = new String[]{"Error while accessing the mojang api"};
		}
		provider.clearArray(macro, arrayName);
		for (String name : names) {
			provider.pushValueToArray(macro, arrayName, name);
		}
		ReturnValueArray returnValueArray = new ReturnValueArray(false);
		returnValueArray.putStrings(Arrays.asList(names));
		return returnValueArray;
	}
	
	private static String sendGet(String url) throws Exception {
		URL               obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		con.setRequestMethod("GET");
		
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		
		StringBuilder response = new StringBuilder();
		String        inputLine;
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		return response.toString();
	}
	
	public static String uuid(String json) {
		int idx = json.indexOf("\"id\":");
		if (idx != -1) {
			char[] charJson = json.toCharArray();
			char[] charOutput = new char[charJson.length];
			int counter = 0;
			int i = 0;
			while (counter < 4) {
				if (charJson[idx] == '"') {
					counter++;
				}
				if ((counter == 3) && (charJson[idx] != '"')) {
					charOutput[(i++)] = charJson[idx];
				}
				idx++;
			}
			StringBuilder output = new StringBuilder();
			for (i = 0; i < charOutput.length; i++) {
				if (charOutput[i] != charOutput[(charOutput.length - 1)]) {
					output.append(charOutput[i]);
				}
			}
			return output.toString();
		}
		return null;
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "[&result =] OLDNAME([&result[]],<name|uuid>)";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Returns the previous names of the player with this name or uuid";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Array with old names of playerr";
	}
	
	@Override
	public ConfigGroups getGroup() {
		
		return ConfigGroups.UTILITIES;
	}
}
