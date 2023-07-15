package me.spthiel.klacaiba.module.actions.information.external;

import me.spthiel.klacaiba.JSON.JSONArray;
import me.spthiel.klacaiba.JSON.JSONObject;
import me.spthiel.klacaiba.config.ConfigGroups;
import me.spthiel.klacaiba.module.actions.base.BaseScriptAction;

import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class GetJsonAsArray extends BaseScriptAction {

	public GetJsonAsArray() {
		super("getjsonasarray");
	}

	public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {

		if(params.length > 0) {

			String format = "$key:$value";
			
			if(params.length > 1) {
				format = provider.expand(macro, params[1], false);
			}
			
			String json = provider.expand(macro, params[0],false);

			if(json.startsWith("[")) {
				JSONArray array = new JSONArray(json);
				ArrayList<String> out = new ArrayList<>();

				out.add("ARRAY");
				
				for(Object object : array) {
					out.add(object.toString());
				}

				return returnValue(provider, macro, out);

			}

			JSONObject object = new JSONObject(json);
			ArrayList<String> out = new ArrayList<>();

			out.add("OBJECT");

			for(String key : object.keySet()) {
				out.add(format.replace("$key", key).replace("$value", object.get(key).toString()));
			}

			return returnValue(provider, macro, out);

		} else {
			return returnValue(provider, macro,"ERROR_TOO_FEW_ARGUMENTS");
		}

	}

	private static IReturnValue returnValue(IScriptActionProvider provider, IMacro macro, String value) {
		ArrayList<String> ret = new ArrayList<>();
		ret.add(value);
		return returnValue(provider, macro, ret);
	}

	private static IReturnValue returnValue(IScriptActionProvider provider, IMacro macro, ArrayList<String> value) {
		ReturnValueArray returnValueArray = new ReturnValueArray(false);
		returnValueArray.putStrings(value);
		return returnValueArray;
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "getjsonasarray(<json>,[format])";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Returns the input json as array. With the specified format default $key:$value";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Array of elements each entry being in the format KEY:VALUE";
	}
	
	@Override
	public ConfigGroups getGroup() {
		
		return ConfigGroups.EXTERNALS;
	}
}
