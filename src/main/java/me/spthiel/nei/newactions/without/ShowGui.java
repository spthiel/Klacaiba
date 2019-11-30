package me.spthiel.nei.newactions.without;

import net.eq2online.macros.scripting.actions.ScriptActionShowGui;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.ScriptCore;

public class ShowGui extends ScriptAction {

	public ShowGui() {
		super(ScriptContext.MAIN, "showgui");
	}

	public boolean isThreadSafe() {
		return false;
	}

	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		if (params.length > 0) {
			String screenName = provider.expand(macro, params[0], false);
			String backScreenName = null;
			boolean enableTriggers = params.length > 2 && ScriptCore.parseBoolean(provider.expand(macro, params[2], false));
			if (params.length > 1) {
				backScreenName = provider.expand(macro, params[1], false);
			}

			provider.actionDisplayCustomScreen(screenName, backScreenName, enableTriggers);
		} else {
			provider.actionDisplayCustomScreen(null, null, false);
		}

		return null;
	}
}