package me.spthiel.klacaiba.module.actions.information.external;

import net.eq2online.macros.scripting.actions.lang.ScriptActionLcase;
import net.eq2online.macros.scripting.actions.lang.ScriptActionUcase;
import net.eq2online.macros.scripting.actions.lang.ScriptActionWait;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import me.spthiel.klacaiba.base.BaseScriptAction;
import me.spthiel.klacaiba.base.IDocumentable;


public class Http extends ScriptAction implements IDocumentable {
	
	public Http() {
		super(ScriptContext.MAIN, "http");
	}

	public boolean canExecuteNow(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {

		if (instance.getState() != null) {
			return !instance.getState().equals(true);
		}
		
		int index = 0;
		String action = "GET";
		String url = "";
		String output = "";
		HashMap<String,String> header = new HashMap<>();
		boolean requiresExtra = false;
		boolean hasEverything = true;
		int timeout = -1;



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
		} else if (params.length > index){
			try {
				timeout = Integer.parseInt(provider.expand(macro, params[index], false));
			} catch (NumberFormatException ignored) {}
		}
		
		if (requiresExtra && params.length > index) {
			try {
				timeout = Integer.parseInt(provider.expand(macro, params[index], false));
			} catch (NumberFormatException ignored) {}
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
			String finalUrl = url;
			String finalAction = action;
			String finalOutput = output;
			int finalTimeout = timeout;
			instance.setState(true);
			Thread request = new Thread(() -> {
				String response = executeRequest(finalUrl, finalAction, finalOutput, header, finalTimeout);
				if (response == null) {
					provider.setVariable(macro, "&httpdebug1", "Last Request >> " + finalAction + ": " + finalUrl);
					provider.setVariable(macro, "&httpdebug2", "Last Request >> " + finalOutput);
					provider.setVariable(macro, "&httpdebug3", "Last Request >> " + header);
					
					instance.setState("Error");
				} else {
					instance.setState(response);
				}
			});
			
			request.start();
			return false;
		}

		provider.setVariable(macro, "&httpdebug1", "Last Request >> " + action + ": " + url);
		provider.setVariable(macro, "&httpdebug2", "Last Request >> " + output);
		provider.setVariable(macro, "&httpdebug3", "Last Request >> " + header);

		instance.setState("Error");
		
		return true;
	}
	
	@Override
	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		return new ReturnValue(instance.getState());
	}
	
	/* Partially from http://www.xyzws.com/Javafaq/how-to-use-httpurlconnection-post-data-to-web-server/139 */
	public static String executeRequest(String targetURL, String method, String output,  HashMap<String,String> header, int timeout) {
		HttpURLConnection connection = null;
		method = method.toUpperCase();

		try {
			//Create connection
			URL url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			if (timeout != -1) {
				connection.setConnectTimeout(timeout);
				connection.setReadTimeout(timeout);
			}
			connection.setRequestMethod(method);

			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36");

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
		
		return "http([GET|POST|PUT|DELETE],<url>,[output stream],[headers|connection-timeout],[connection-timeout])";
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
