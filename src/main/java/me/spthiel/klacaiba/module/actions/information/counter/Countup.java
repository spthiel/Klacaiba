package me.spthiel.klacaiba.module.actions.information.counter;

import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.config.ConfigGroups;
import me.spthiel.klacaiba.module.actions.base.BaseScriptAction;

public class Countup extends BaseScriptAction {
    
    public Countup() {
        super("countup");
    }
    
    @Override
    public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        
        return new ReturnValue(Counter.registerCounter(new me.spthiel.klacaiba.utils.Countup()));
    }
    
    @Nonnull
    @Override
    public String getUsage() {
        
        return "countup()";
    }
    
    @Nonnull
    @Override
    public String getDescription() {
        
        return "Starts a countup";
    }
    
    @Nonnull
    @Override
    public String getReturnType() {
        
        return "Integer id of the counter. Use counter(<id>) to get the value";
    }
    
    @Override
    public ConfigGroups getGroup() {
        
        return ConfigGroups.UTILITIES;
    }
}
