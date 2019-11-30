package me.spthiel.nei.newactions.with;

import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;

import me.spthiel.nei.actions.BaseScriptAction;

public class Countup extends BaseScriptAction {
    
    public Countup() {
        super("countup");
    }
    
    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        
        return new ReturnValue(Counter.registerCounter(new me.spthiel.nei.utils.Countup()));
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
}
