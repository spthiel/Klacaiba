package me.spthiel.klacaiba.newactions.with;

import me.spthiel.klacaiba.base.BaseScriptAction;
import me.spthiel.klacaiba.utils.FilePath;

import net.eq2online.macros.scripting.Variable;
import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Readfile extends BaseScriptAction {

	public Readfile() {
		super("readfile");
	}

	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {

		String filename;
		String arrayName = null;

		if(params.length > 1) {
			arrayName = Variable.getValidVariableOrArraySpecifier(params[0]);
			filename = provider.expand(macro, params[1], false);
		} else {
			filename = provider.expand(macro, params[0], false);
		}

		File file = FilePath.getFile(filename);

		if(!file.exists()) {
			return returnValue(provider, macro, arrayName, "ERROR_FILE_DOES_NOT_EXIST");
		}

		ArrayList<String> list = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				list.add(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
			return returnValue(provider, macro, arrayName, "ERROR_IO");
		}
		return returnValue(provider, macro, arrayName, list);
	}

	private static IReturnValue returnValue(IScriptActionProvider provider, IMacro macro, @Nullable String arrayname, String value) {
		ArrayList<String> ret = new ArrayList<>();
		ret.add(value);
		return returnValue(provider, macro, arrayname, ret);
	}

	private static IReturnValue returnValue(IScriptActionProvider provider, IMacro macro, @Nullable String arrayname, ArrayList<String> value) {
		if(arrayname != null) {
			writeArray(provider, macro, arrayname, value);
		}
		ReturnValueArray returnValueArray = new ReturnValueArray(false);
		returnValueArray.putStrings(value);
		return returnValueArray;
	}

	private static void writeArray(IScriptActionProvider provider, IMacro macro, @Nonnull String arrayname, ArrayList<String> value) {

		provider.clearArray(macro, arrayname);
		for (String item : value) {
			provider.pushValueToArray(macro, arrayname, item);
		}
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "readfile([&content[]],<path>)";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Read the file specified by the path and returns the content of it";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Content of the file as array";
	}
}
