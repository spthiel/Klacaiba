package me.spthiel.klacaiba.module.actions.information.external;

import net.eq2online.macros.scripting.ReturnValueLogTo;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.util.Util;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class LogTo extends ScriptAction {

	private File logsFolder;

	public LogTo() {
		super(ScriptContext.MAIN, "LogTo");
	}

	private void initLogsFolder(IScriptActionProvider provider) {
		if (this.logsFolder == null) {
			this.logsFolder = provider.getMacroEngine().getFile("logs");

			try {
				if (!this.logsFolder.exists()) {
					//noinspection ResultOfMethodCallIgnored
					this.logsFolder.mkdirs();
				}
			} catch (Exception ignored) {
			}
		}
		
	}

	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		if (params.length > 1) {
			String target = provider.expand(macro, params[0], false);
			String fileName;
			if (!target.contains(".")) {
				fileName = Util.convertAmpCodes(provider.expand(macro, params[1], false));
				return new ReturnValueLogTo(fileName, target);
			}

			fileName = sanitiseFileName(target);
			this.initLogsFolder(provider);

			try {
				if (fileName != null && this.logsFolder != null && this.logsFolder.exists()) {
					File logFile = new File(this.logsFolder, fileName);
					PrintWriter printWriter = new PrintWriter(new FileWriter(logFile, true));
					printWriter.println(provider.expand(macro, params[1], false));
					printWriter.close();
				}
			} catch (Exception ignored) {

			}
		}

		return null;
	}

	private static String sanitiseFileName(String fileName) {
		if (fileName.length() > 0) {
			return fileName;
		}

		return null;
	}
}
