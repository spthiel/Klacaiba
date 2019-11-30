package me.spthiel.nei.newactions.with;

import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;

import java.time.Duration;

import me.spthiel.nei.actions.BaseScriptAction;
import me.spthiel.nei.utils.Utils;

public class SecToTime extends BaseScriptAction {
    
    public SecToTime() {
        
        super("sectotime");
    }
    
    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        
        if(params.length == 0) {
            return new ReturnValue("00:00:00");
        }
        
        int seconds = getIntOrDefault(provider, macro, params[0], 0);
        
        String format = "hh:mm:ss";
        if(params.length > 2) {
            format = provider.expand(macro, params[1], false);
        }
    
        Duration duration = Duration.ofSeconds(seconds);
    
        return new ReturnValue(Utils.formatTime(duration, format));
    }
    
    @Nonnull
    @Override
    public String getUsage() {
        
        return "&time = secToTime(<seconds>,[format])";
    }
    
    @Nonnull
    @Override
    public String getDescription() {
        
        return "Formats the amount of seconds to a humanly readable time. hh:mm:ss by default";
    }
    
    @Nonnull
    @Override
    public String getReturnType() {
        
        return "Time as String";
    }
}
