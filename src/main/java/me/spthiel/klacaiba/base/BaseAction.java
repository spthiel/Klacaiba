package me.spthiel.klacaiba.base;

import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptActionProvider;

public interface BaseAction {
    
    default int getIntOrDefault(IScriptActionProvider provider, IMacro macro, String param, int def) {
        try {
            return Integer.parseInt(provider.expand(macro, param, false));
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
