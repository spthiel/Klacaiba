package me.spthiel.klacaiba.module.actions.information.external;

import net.eq2online.macros.scripting.Variable;
import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import me.spthiel.klacaiba.config.ConfigGroups;
import me.spthiel.klacaiba.module.actions.base.BaseScriptAction;

public class Char extends BaseScriptAction {
	
	public Char() {
		super("char");
	}
	
	@Override
	public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		if (params.length == 0) {
			return new ReturnValue("Invalid Arguments");
		}
		
		final String returnVar;
		String chars;
		if (params.length > 1) {
			returnVar = provider.expand(macro, params[0], false);
			chars = provider.expand(macro, params[1], false);
		} else {
			returnVar = null;
			chars = provider.expand(macro, params[0], false);
		}
		
		List<String> out;
		List<String> in = new ArrayList<>();
		
		boolean array = false;
		
		if (Variable.isValidVariableName(chars) && Variable.couldBeArraySpecifier(chars)) {
			int size = provider.getArraySize(macro, chars);
			for (int i = 0; i < size; i++) {
				in.add(provider.getArrayElement(macro, chars, i).toString());
			}
			array = true;
		} else {
			in.add(provider.expand(macro, chars, false));
		}
		
		out = in.stream().map(this::evaluate).map(Object::toString).collect(Collectors.toList());
		
		if (returnVar != null) {
			if (Variable.couldBeArraySpecifier(returnVar)) {
				provider.clearArray(macro, returnVar);
				out.forEach(element -> provider.pushValueToArray(macro, returnVar, element));
			} else if (Variable.isValidVariableName(returnVar)){
				provider.setVariable(macro, returnVar, out.size() > 0 ? out.get(0) : "");
			}
		}
		
		if (array) {
			ReturnValueArray returnValueArray = new ReturnValueArray(false);
			returnValueArray.putStrings(out);
			return returnValueArray;
		} else {
			return new ReturnValue(out.size() > 0 ? out.get(0) : "");
		}
	}
	
	private char evaluate(String expression) {
		int value = 0;
		expression = expression.toLowerCase();
		try {
			if (expression.matches("\\\\[xu](?:[0-9a-f]{2}){1,2}")) {
				value = Integer.parseInt(expression.substring(2), 16);
			} else if (expression.matches("\\d+")) {
				value = Integer.parseInt(expression);
			}
		} catch (NumberFormatException ignored) {}
		return (char)value;
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "[&result|&result[] =] char([&result|&result[]],<char expression|&expressions[]>)";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Returns the characters of the character expression. Either a decimal for ascii lookup, \\xYY for basic hex lookup or \\uYYYY for basic unicode lookup";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Char or chars from the expression";
	}
	
	@Override
	public ConfigGroups getGroup() {
		
		return ConfigGroups.UTILITIES;
	}
}
