package me.spthiel.klacaiba.module.actions.information.counter;

import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import me.spthiel.klacaiba.base.BaseScriptAction;
import me.spthiel.klacaiba.base.Configurable;
import me.spthiel.klacaiba.config.ConfigGroups;

public class Countdownfrom extends BaseScriptAction implements Configurable {
    
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static String            timeRegex     = "\\d\\d:\\d\\d:\\d\\d";
    
    public Countdownfrom() {
        
        super("countdownfrom");
    }
    
    @Override
    public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
    
        if(params.length == 0) {
            return null;
        }
    
        String    time       = provider.expand(macro, params[0], false);
        LocalTime targettime = LocalTime.parse(time, timeFormatter);
        LocalDateTime target    = LocalDateTime.now()
                                                .plusHours(targettime.getHour())
                                                .plusMinutes(targettime.getMinute())
                                                .plusSeconds(targettime.getSecond());
    
        return new ReturnValue(Counter.registerCounter(new me.spthiel.klacaiba.utils.Countdown(target)));
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
    
    @Override
    public ConfigGroups getGroup() {
        
        return ConfigGroups.UTILITIES;
    }
}
