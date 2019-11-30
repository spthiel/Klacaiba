package me.spthiel.nei.actions;

import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;

public abstract class BaseScriptAction extends ScriptAction implements IDocumentable {
    
    public BaseScriptAction(String actionName) {
        
        super(ScriptContext.MAIN, actionName);
    }
    
    protected final int getIntOrDefault(IScriptActionProvider provider, IMacro macro, String param, int def) {
        try {
            return Integer.parseInt(provider.expand(macro, param, false));
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
