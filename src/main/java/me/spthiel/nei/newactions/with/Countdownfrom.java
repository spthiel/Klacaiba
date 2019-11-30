package me.spthiel.nei.newactions.with;

import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import me.spthiel.nei.actions.BaseScriptAction;

public class Countdownfrom extends BaseScriptAction {
    
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static String            timeRegex     = "\\d\\d:\\d\\d:\\d\\d";
    
    public Countdownfrom() {
        
        super("countdownfrom");
    }
    
    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
    
        if(params.length == 0) {
            return null;
        }
    
        String    time       = params[0];
        LocalTime targettime = LocalTime.parse(time, timeFormatter);
        LocalDateTime target    = LocalDateTime.now()
                                                .plusHours(targettime.getHour())
                                                .plusMinutes(targettime.getMinute())
                                                .plusSeconds(targettime.getSecond());
    
        return new ReturnValue(Counter.registerCounter(new me.spthiel.nei.utils.Countdown(target)));
    }
    
    @Nonnull
    @Override
    public String getUsage() {
        
        return "countdownfrom(<start>)";
    }
    
    @Nonnull
    @Override
    public String getDescription() {
        
        return "Creates a countdown from the specified time. Time has to be in format hh:mm:ss";
    }
    
    @Nonnull
    @Override
    public String getReturnType() {
        
        return "Id of the countdown. Use counter(<id>) to get the value";
    }
}
