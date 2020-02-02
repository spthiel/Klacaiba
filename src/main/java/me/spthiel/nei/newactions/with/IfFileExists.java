package me.spthiel.nei.newactions.with;

import me.spthiel.nei.actions.BaseConditionalOperator;
import me.spthiel.nei.actions.BaseScriptAction;
import me.spthiel.nei.actions.IDocumentable;
import me.spthiel.nei.utils.FilePath;
import net.eq2online.macros.compatibility.I18n;
import net.eq2online.macros.scripting.api.IExpressionEvaluator;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

public class IfFileExists extends BaseConditionalOperator {

	public IfFileExists() {
		super("iffileexists");
	}

	public String getExpectedPopCommands() {
		return I18n.get("script.error.stackhint", this, "ELSEIF\u00A7c, \u00A7dELSE\u00A7c or \u00A7dENDIF");
	}

	public boolean isConditionalOperator() {
		return true;
	}

	public boolean executeConditional(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {

		if(params.length == 0) {
			return false;
		}

		File file = FilePath.getFile(params[0]);

		if(file.exists()) {
			return true;
		}

		if(params.length > 1) {
			String condition = params[1];
			IExpressionEvaluator evaluator = provider.getExpressionEvaluator(macro, provider.expand(macro, condition, true));
			try {
				return evaluator.evaluate() != 0 && ((file.getParentFile().exists() && file.createNewFile()) || (file.getParentFile().mkdirs() && file.createNewFile()));
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "iffileexists(<path>,[expression if file should be created if missing])";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Checks if the file exists and/or created the file if it doesn't. Files can start with ~ to be relative to the minecraft directory.";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "";
	}
}
