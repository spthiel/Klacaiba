package me.spthiel.nei.newactions.with;

import me.spthiel.nei.actions.BaseScriptAction;
import me.spthiel.nei.actions.IDocumentable;
import me.spthiel.nei.utils.FilePath;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.HashMap;

public class MkDir extends BaseScriptAction {

	public MkDir() {
		super("mkdir");
	}

	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {

		if(params.length == 1) {
			String path = provider.expand(macro, params[0], false);
			File dir = FilePath.getFile(path);
			if(dir.exists()) {
				return new ReturnValue("EXISTS");
			}
			if(!dir.getParentFile().exists()) {
				if(!dir.mkdirs()) {
					return new ReturnValue("ERROR");
				}
				return new ReturnValue("CREATEDWITHPARENTS");
			}
			if(!dir.mkdir()) {
				return new ReturnValue("ERROR");
			}
			return new ReturnValue("CREATED");
		}
		return new ReturnValue("ERROR");
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "mkdir(<path>)";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Creates the specified directory";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Various useful status codes for debugging. ERROR if the folder doesn't exist afterwards";
	}
}
