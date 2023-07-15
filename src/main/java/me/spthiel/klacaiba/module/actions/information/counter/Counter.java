package me.spthiel.klacaiba.module.actions.information.counter;

import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;

import java.time.Duration;
import java.util.HashMap;

import me.spthiel.klacaiba.module.actions.base.BaseScriptAction;
import me.spthiel.klacaiba.module.actions.base.IConfigurable;
import me.spthiel.klacaiba.config.ConfigGroups;
import me.spthiel.klacaiba.config.ConfigOptionList;
import me.spthiel.klacaiba.utils.Counting;
import me.spthiel.klacaiba.utils.Utils;

public class Counter extends BaseScriptAction implements IConfigurable {
    
    private static HashMap<Integer, Counting> counters = new HashMap<>();
    private static int                        id       = 0;
    
    public static int registerCounter(Counting counter) {
        
        counters.put(id++, counter);
        return id - 1;
    }
    
    public Counter() {
        
        super("counter");
    }
    
    @Override
    public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        
        if (params.length == 0) {
            return new ReturnValue("-1");
        }
        int id;
        try {
            id = Integer.parseInt(provider.expand(macro, params[0], false));
        } catch (NumberFormatException e) {
            return new ReturnValue("-1");
        }
        
        if (!counters.containsKey(id)) {
            return new ReturnValue("-1");
        }
        
        long value = counters.get(id).getValue();
        if (value < 0) {
            counters.remove(id);
        }
        
        String format = "hh:mm:ss";
        
        if(params.length > 1) {
            format = provider.expand(macro, params[1], false);
        }
        
        Duration duration = Duration.ofSeconds(value);
        
        return new ReturnValue(Utils.formatTime(duration, format));
    }
    
    @Nonnull
    @Override
    public String getUsage() {
        
        return "counter(<id>,[timeformat])";
    }
    
    @Nonnull
    @Override
    public String getDescription() {
        
        return "returns the value of the counter or -1 if finished or invalid. Format: dd for day, hh for hour, mm for minutes, ss for second";
    }
    
    @Nonnull
    @Override
    public String getReturnType() {
        
        return "Formatted time or hh:mm:ss if none was given";
    }
    
    @Override
    public ConfigGroups getGroup() {
        
        return ConfigGroups.UTILITIES;
    }
    
    @Override
    public ConfigOptionList getOptions() {
        
        return null;
    }
}
