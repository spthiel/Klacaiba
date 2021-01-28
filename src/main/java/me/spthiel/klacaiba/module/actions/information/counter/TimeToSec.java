package me.spthiel.klacaiba.module.actions.information.counter;

import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.base.BaseScriptAction;

public class TimeToSec extends BaseScriptAction {
    
    public TimeToSec() {
        
        super("timetosec");
    }
    
    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        
        if(params.length == 3) {
            return new ReturnValue(getIntOrDefault(provider, macro, params[0], 0) * 3600 +
                                   getIntOrDefault(provider, macro, params[1], 0) * 60 +
                                   getIntOrDefault(provider, macro, params[2], 0));
        }
        
        if(params.length == 0) {
            return new ReturnValue(0);
        }
        
        String value = provider.expand(macro, params[0], false);
        if(value.matches("\\d+:\\d\\d:\\d\\d")) {
            String[] splitted = value.split(":");
            int multi = 3600;
            int sum = 0;
            for(int i = 0; i < splitted.length; i++) {
                sum += Integer.parseInt(splitted[i]) * multi;
                multi /= 60;
            }
            return new ReturnValue(sum);
        }
        
        return new ReturnValue(0);
    }
    
    @Nonnull
    @Override
    public String getUsage() {
        
        return "timeToSec(<hh:mm:ss|hour>,[minute],[seconds])";
    }
    
    @Nonnull
    @Override
    public String getDescription() {
        
        return "Returns count of seconds of the specified time";
    }
    
    @Nonnull
    @Override
    public String getReturnType() {
        
        return "Count of seconds";
    }
}
