package me.spthiel.nei.newactions.with;

import me.spthiel.nei.actions.BaseScriptAction;
import me.spthiel.nei.actions.IDocumentable;
import me.spthiel.nei.utils.FilePath;
import net.eq2online.macros.scripting.Variable;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.util.ArrayList;

public class WriteFile extends BaseScriptAction {

	public WriteFile() {
		super("writefile");
	}

	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {

		if(params.length < 2) {
			return new ReturnValue("Too few arguments");
		}
		
		String filename = provider.expand(macro, params[0], false);
		String arrayName = provider.expand(macro, params[1], false);
		if(provider.getArrayExists(macro, filename)) {
			String store = arrayName;
			arrayName = Variable.getValidVariableOrArraySpecifier(filename);
			filename = store;
		} else {
			arrayName = Variable.getValidVariableOrArraySpecifier(arrayName);
		}
		
		boolean append = params.length > 2 && provider.expand(macro, params[2], false).toLowerCase().matches("true|1|t");

		if(!provider.getArrayExists(macro, arrayName)) {
			return new ReturnValue("ERROR_ARG_NOT_ARRAY");
		}

		ArrayList<String> toWrite = new ArrayList<>();
		for(int i = 0; i < provider.getArraySize(macro, arrayName);i++) {
			toWrite.add((String)provider.getArrayElement(macro, arrayName, i));
		}

		File file = FilePath.getFile(filename);

		if(!file.exists()) {
			try {
				if(!file.getParentFile().mkdirs() || !file.createNewFile()) {
					return new ReturnValue("ERROR_UNABLE_TO_CREATE");
				}
			} catch (IOException e) {
				e.printStackTrace();
				return new ReturnValue("ERROR_IO_WHILE_CREATING_FILE");
			}
		}

		try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file,append))) {

			for(String line : toWrite) {
				bufferedWriter.write(line + "\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
			return new ReturnValue("ERROR_IO_WHILE_WRITING");
		}

		return new ReturnValue(file.getAbsolutePath());

	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "writefile(<path>,<&array[]>,[append])";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Writes the array into the file specified with the path. Optionally appending it";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Error code or file path on success";
	}
}
