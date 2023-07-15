package me.spthiel.klacaiba.module.actions.information.external;

import me.spthiel.klacaiba.JSON.JSONArray;
import me.spthiel.klacaiba.JSON.JSONException;
import me.spthiel.klacaiba.JSON.JSONObject;
import me.spthiel.klacaiba.config.ConfigGroups;
import me.spthiel.klacaiba.module.actions.base.BaseScriptAction;
import me.spthiel.klacaiba.utils.JsonUtils;

import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;

public class JsonGet extends BaseScriptAction {

	public JsonGet() {
		super("jsonget");
	}

	public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		if(params.length > 1) {
			String json = provider.expand(macro, params[0],false);
			String key = provider.expand(macro, params[1],false);
			Object object;
			try {
				if (json.startsWith("[")) {
					object = new JSONArray(json);
				} else if (json.startsWith("{")){
					object = new JSONObject(json);
				} else {
					throw new JSONException((String)null);
				}
			} catch(JSONException e) {
				try {
					if (key.startsWith("[")) {
						object = new JSONArray(key);
					} else {
						object = new JSONObject(key);
					}
					key = json;
				} catch (JSONException e1) {
					return new ReturnValue("ERROR_INVALID_JSON");
				}
			}
			
			try {
				return new ReturnValue(JsonUtils.getJsonFromPath(object, key).toString());
			} catch (JSONException e) {
				return new ReturnValue(e.getMessage());
			}
			
		} else {
			return new ReturnValue("ERROR_TOO_FEW_ARGUMENTS");
		}
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "jsonget(<key>,<json>)";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Returns the value of the specified key from the json";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Value of specified key of the json";
	}
	
	@Override
	public ConfigGroups getGroup() {
		
		return ConfigGroups.EXTERNALS;
	}
}
