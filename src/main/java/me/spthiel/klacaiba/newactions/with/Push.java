package me.spthiel.klacaiba.newactions.with;

import net.eq2online.macros.scripting.Variable;
import net.eq2online.macros.scripting.actions.lang.ScriptActionPush;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.spthiel.klacaiba.actions.BaseScriptAction;

public class Push extends BaseScriptAction {
	
	public Push() {
		super("push");
	}
	
	public Push(String actionName) {
		super(actionName);
	}
	
	@Override
	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		if (params.length > 1) {
			String arrayName = provider.expand(macro, params[0], false);
			
			for (int i = 1 ; i < params.length ; i++) {
				evaluateParam(provider, macro, provider.expand(macro, params[i], false), (value) -> provider.pushValueToArray(macro, arrayName, value.toString()));
			}
			
		}
		
		return null;
	}
	
	private static final Pattern ARRAYPATTERN = Pattern.compile("(@?(?:|#|&)\\w[\\w\\d_-]+)\\[(\\*|\\d+-\\d+)\\]");
	
	protected void evaluateParam(IScriptActionProvider provider, IMacro macro, String parameter, Consumer<Object> addCallback) {
		Matcher m;
		if ((m = ARRAYPATTERN.matcher(parameter)).find()) {
			String arrayName = m.group(1) + "[]";
			String range = m.group(2);
			if (provider.getArrayExists(macro, arrayName)) {
				int start = 0;
				int end = 0;
				int arraySize = provider.getArraySize(macro, arrayName);
				if (range.equals("*")) {
					end = arraySize;
				} else {
					String[] splitted = range.split("-");
					start = Math.max(Math.min(ScriptCore.tryParseInt(splitted[0], 0), arraySize), 0);
					end = Math.max(Math.min(ScriptCore.tryParseInt(splitted[1], 0), arraySize), 0);
				}
				Minecraft.getMinecraft().player.sendChatMessage("Start: " + start + " End: " + end);
				copyArrayTo(provider, macro, start, end, arrayName, addCallback);
				
			} else {
				Minecraft.getMinecraft().player.sendChatMessage("Invalid array");
			}
		} else {
			addCallback.accept(parameter);
		}
	}
	
	private void copyArrayTo(IScriptActionProvider provider, IMacro macro, int startIndex, int endIndex, String arrayName, Consumer<Object> addCallback) {
		
		for (int i = startIndex ; i < endIndex; i++) {
			Object o = provider.getArrayElement(macro, arrayName, i);
			addCallback.accept(o);
		}
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "PUSH(<array>,<...values>)";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Appends value[s] to the end of array. Values may be constants, array[*] to copy an entire array or array[a-b] to copy a <= indexes < b to new array.";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "";
	}
}
