package me.spthiel.klacaiba.newactions.with;

import me.spthiel.klacaiba.JSON.JSONException;
import me.spthiel.klacaiba.JSON.JSONObject;
import me.spthiel.klacaiba.actions.IDocumentable;

import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;

import javax.annotation.Nonnull;

public class JsonGet extends ScriptAction implements IDocumentable {

	public JsonGet() {
		super(ScriptContext.MAIN, "jsonget");
	}

	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {

		if(params.length > 1) {
			
			String json = provider.expand(macro, params[0],false);
			String key = provider.expand(macro, params[1],false);
			JSONObject object;
			try {
				object = new JSONObject(json);
			} catch(JSONException e) {
				try {
					object = new JSONObject(key);
					key = json;
				} catch (JSONException e1) {
					return new ReturnValue("ERROR_INVALID_JSON");
				}
			}

			if(!object.has(key)) {
				return new ReturnValue("ERROR_JSON_WITHOUT_KEY");
			}

			return new ReturnValue(object.get(key).toString());

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
}
