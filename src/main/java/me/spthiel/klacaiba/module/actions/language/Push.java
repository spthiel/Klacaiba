package me.spthiel.klacaiba.module.actions.language;

import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptCore;

import javax.annotation.Nonnull;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.spthiel.klacaiba.base.BaseScriptAction;

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
	
	private static final Pattern ARRAYPATTERN = Pattern.compile("(@?(?:|#|&)\\w[\\w\\d_-]+)\\[(\\*|-?\\d+(?::(?:-?\\d+)?){0,2})?]");
	
	protected void evaluateParam(IScriptActionProvider provider, IMacro macro, String parameter, Consumer<Object> addCallback) {
		Matcher m;
		if ((m = ARRAYPATTERN.matcher(parameter)).find()) {
			String arrayName = m.group(1) + "[]";
			String range = m.group(2);
			if (provider.getArrayExists(macro, arrayName)) {
				int start = 0;
				int arraySize = provider.getArraySize(macro, arrayName);
				int end = arraySize;
				int step = 1;
				if (range.matches("-?\\d+")) {
					start = Math.min(Math.max(Integer.parseInt(range), -arraySize), arraySize);
					if (start < 0) {
						start = arraySize + start;
					}
					end = start+1;
				} else if (!range.equals("*")) {
					String[] splitted = range.split(":");
					start = Math.max(Math.min(ScriptCore.tryParseInt(splitted[0], 0), arraySize), -arraySize);
					if (splitted.length > 1 && splitted[1].matches("-?\\d+")) {
						end = Math.max(Math.min(ScriptCore.tryParseInt(splitted[1], 0), arraySize), -arraySize);
					}
					if (splitted.length > 2) {
						step = ScriptCore.tryParseInt(splitted[2], 1);
						if (step == 0) {
							step = 1;
						}
					}
					if (start < 0) {
						start = arraySize + start;
					}
					if (end < 0) {
						end = arraySize + end;
					}
					if (end < start) {
						int store = end;
						end = start;
						start = store;
					}
					if (step < 0) {
						int store = end;
						end = start;
						start = store;
					}
				}
				copyArrayTo(provider, macro, start, end, step, arrayName, addCallback);
			}
		} else {
			addCallback.accept(parameter);
		}
	}
	
	private void copyArrayTo(IScriptActionProvider provider, IMacro macro, int startIndex, int endIndex, int step, String arrayName, Consumer<Object> addCallback) {
		
		if (step > 0) {
			for (int i = startIndex ; i < endIndex ; i += step) {
				Object o = provider.getArrayElement(macro, arrayName, i);
				addCallback.accept(o);
			}
		} else {
			for (int i = startIndex-1 ; i >= endIndex; i += step) {
				Object o = provider.getArrayElement(macro, arrayName, i);
				addCallback.accept(o);
			}
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
		
		return "Appends value[s] to the end of array. Values may be constants, array[*] to copy an entire array or array[a:b:step] to copy a <= indexes < b to new array. Negative values wrap around.";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "";
	}
}
