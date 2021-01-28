package me.spthiel.klacaiba.module.actions.information.external;

import me.spthiel.klacaiba.JSON.JSONObject;
import me.spthiel.klacaiba.base.BaseScriptAction;

import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class GetJsonKeys extends BaseScriptAction {

	public GetJsonKeys() {
		super("getjsonkeys");
	}

	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {

		if(params.length > 0) {

			String json = provider.expand(macro, params[0],false);

			JSONObject object = new JSONObject(json);

			return returnValue(provider, macro, new ArrayList<>(object.keySet()));

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
		
		return "getjsonkeys(<json>)";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Returns the keys of the json";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Array containing all the keys";
	}
}
