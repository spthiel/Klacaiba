package me.spthiel.nei.newactions;

import me.spthiel.nei.JSON.JSONArray;
import me.spthiel.nei.JSON.JSONObject;
import me.spthiel.nei.actions.IDocumentable;

import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class GetJsonAsArray extends ScriptAction implements IDocumentable {

	public GetJsonAsArray() {
		super(ScriptContext.MAIN, "getjsonasarray");
	}

	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {

		if(params.length > 0) {

			String json = provider.expand(macro, params[0],false);

			if(json.startsWith("[")) {
				JSONArray array = new JSONArray(json);
				ArrayList<String> out = new ArrayList<>();

				out.add("ARRAY");

				for(Object object : array.toList()) {
					out.add(object.toString());
				}

				return returnValue(provider, macro, out);

			}

			JSONObject object = new JSONObject(json);
			ArrayList<String> out = new ArrayList<>();

			out.add("OBJECT");

			for(String key : object.keySet()) {
				out.add(key + ":" + object.get(key).toString());
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
		
		return "getjsonasarray(<json>)";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Returns the input json as array";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Array of elements each entry being in the format KEY:VALUE";
	}
}
