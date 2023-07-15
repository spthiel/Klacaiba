package me.spthiel.klacaiba.module.actions.base;

import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;

import me.spthiel.klacaiba.config.ConfigGroups;
import me.spthiel.klacaiba.config.ConfigOptionList;
import me.spthiel.klacaiba.config.configOptions.Checkbox;

public abstract class BaseScriptAction extends ScriptAction implements IDocumentable, BaseAction, IConfigurable {
    
    public BaseScriptAction(String actionName) {
        
        super(ScriptContext.MAIN, actionName);
        
        
    }
    
    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        
        return this.run(provider, macro, instance, rawParams, params);
    }
    
    public abstract IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params);
    
    
    @Override
    public ConfigOptionList getOptions() {
        
        ConfigOptionList options = new ConfigOptionList();
        
        options.addOption(new Checkbox(0, "Enable " + getName(), false));
        
        return options;
    }
}
