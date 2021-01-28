package me.spthiel.klacaiba.newactions.with;

import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import me.spthiel.klacaiba.base.BaseScriptAction;


public class Http extends BaseScriptAction {
	
	public Http() {
		super("http");
	}

	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {

		int index = 0;
		String action = "GET";
		String url = "";
		String output = "";
		HashMap<String,String> header = new HashMap<>();
		boolean requiresExtra = false;
		boolean hasEverything = true;



		if(params.length > index) {
			String arg = provider.expand(macro, params[index], false).toUpperCase();
			arg = arg.toUpperCase();
			if(arg.matches("POST|PUT")) {
				action = arg;
				requiresExtra = true;
				index++;
			} else if(arg.matches("GET|DELETE")) {
				action = arg;
				index++;
			}
		}

		if(params.length > index) {
			url = provider.expand(macro, params[index], false);
			index++;
		} else {
			hasEverything = false;
		}

		if(requiresExtra && params.length > index) {
			output = provider.expand(macro, params[index], false).replace("\\n","\n");
			index++;
		} else if(requiresExtra){
			hasEverything = false;
		}

		for(; index < params.length; index++) {
			String arg = provider.expand(macro, params[index], false);
			int split = arg.indexOf(":");
			try {
				header.put(arg.substring(0, split), arg.substring(split + 1));
			} catch (Exception ignored) {
			
			}
		}

		if(hasEverything) {
			return new ReturnValue(executeRequest(url,action,output,header));
		}

		provider.setVariable(macro, "&httpdebug1", "Last Request >> " + action + ": " + url);
		provider.setVariable(macro, "&httpdebug2", "Last Request >> " + output);
		provider.setVariable(macro, "&httpdebug3", "Last Request >> " + header);

		return new ReturnValue("Error");
	}

	/* Partially from http://www.xyzws.com/Javafaq/how-to-use-httpurlconnection-post-data-to-web-server/139 */
	public static String executeRequest(String targetURL, String method, String output,  HashMap<String,String> header) {
		HttpURLConnection connection = null;
		method = method.toUpperCase();

		try {
			//Create connection
			URL url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method);

			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

			connection.setUseCaches(false);
			connection.setDoOutput(method.matches("POST|PUT"));

			for(String key : header.keySet()) {
				connection.addRequestProperty(key, header.get(key));
			}

			if(method.matches("POST|PUT")) {
				DataOutputStream wr = new DataOutputStream(
						connection.getOutputStream());
				wr.writeBytes(output);
				wr.close();
			}

			//Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\n');
			}
			rd.close();
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "http([GET|POST|PUT|DELETE],<url>,[output stream],[headers])";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Performs an http request\nRequest method defaults to GET\nOutput stream does not apply for GET/DELETE\nheaders are defined with key1:value1,key2:value2\nTo debug after an http request the variables &httpdebug[1-3] are available";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Response of the request";
	}
}
