package me.spthiel.klacaiba.base;

import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;

public abstract class BaseScriptAction extends ScriptAction implements IDocumentable, BaseAction {
    
    public BaseScriptAction(String actionName) {
        
        super(ScriptContext.MAIN, actionName);
    }
    
    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        
        return this.run(provider, macro, instance, rawParams, params);
    }
    
    public abstract IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params);
}
