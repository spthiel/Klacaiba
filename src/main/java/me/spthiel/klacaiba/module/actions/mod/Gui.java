package me.spthiel.klacaiba.module.actions.mod;

import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;

public class Gui extends ScriptAction {

	public Gui() {
		super(ScriptContext.MAIN, "gui");
	}

	public boolean isThreadSafe() {
		return false;
	}

	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		if (params.length > 0) {
			provider.actionDisplayGuiScreen(provider.expand(macro,params[0],false), this.context);
		} else {
			provider.actionDisplayGuiScreen(null, this.context);
		}

		return null;
	}
}
