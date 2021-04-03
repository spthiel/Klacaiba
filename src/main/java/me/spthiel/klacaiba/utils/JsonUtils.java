package me.spthiel.klacaiba.utils;

import net.eq2online.macros.scripting.api.ReturnValue;

import me.spthiel.klacaiba.JSON.JSONArray;
import me.spthiel.klacaiba.JSON.JSONException;
import me.spthiel.klacaiba.JSON.JSONObject;

public class JsonUtils {

	public static Object getJsonFromPath(Object object, String key) throws JSONException {
		
		String[] path;
		if (key.contains(".")) {
			path = key.split("\\.");
		} else {
			path = new String[]{key};
		}
		for (String s : path) {
			if (object instanceof JSONObject) {
				JSONObject casted = (JSONObject)object;
				if(!casted.has(s)) {
					throw new JSONException("ERROR_JSON_WITHOUT_KEY_" + s);
				}
				object = casted.get(s);
			} else if (object instanceof JSONArray) {
				JSONArray arr = (JSONArray)object;
				try {
					int idx = Integer.parseInt(s);
					if (idx >= arr.length()) {
						throw new JSONException("ERROR_INDEX_OUT_OF_BOUNDS_" + idx);
					}
					object = arr.get(idx);
				} catch (NumberFormatException e) {
					throw new JSONException("ERROR_EXPECT_NUMBER_FOR_ARRAY_" + s);
				}
			} else {
				throw new JSONException("ERROR_NOT_JSON_AT_KEY_" + s);
			}
		}
		return object;
	}
	
}
