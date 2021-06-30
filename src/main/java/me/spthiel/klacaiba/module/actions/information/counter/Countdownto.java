package me.spthiel.klacaiba.module.actions.information.counter;

import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import me.spthiel.klacaiba.base.BaseScriptAction;

public class Countdownto extends BaseScriptAction {
    
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static String timeRegex = "\\d\\d:\\d\\d:\\d\\d";
    private static String dateRegex = "\\d\\d\\d\\d-\\d\\d-\\d\\d";
    
    public Countdownto() {
        
        super("countdownto");
    }
    
    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        
        if(params.length == 0) {
            return null;
        }
    
    
        String datetime = provider.expand(macro, params[0], false);
        String date     = "";
        String time     = "";
        if (datetime.matches(".*(" + timeRegex + ").*")) {
            time = datetime.replaceAll(".*(" + timeRegex + ").*", "$1");
        } else {
            time = timeRegex.replace("\\d", "0");
        }
        LocalTime targettime = LocalTime.parse(time, timeFormatter);
        LocalDate targetdate;
        LocalTime current    = LocalTime.now();
        if (datetime.matches(".*(" + dateRegex + ").*")) {
            targetdate = LocalDate.parse(datetime.replaceAll(".*(" + dateRegex + ").*", "$1"), dateFormatter);
        } else {
            if (targettime.isBefore(current)) {
                targetdate = LocalDate.now().plusDays(1);
            } else {
                targetdate = LocalDate.now();
            }
        }

        LocalDateTime target = targetdate.atTime(targettime);
        return new ReturnValue(Counter.registerCounter(new me.spthiel.klacaiba.utils.Countdown(target)));
        
    }
    
    @Nonnull
    @Override
    public String getUsage() {
        
        return "countdownto(<until>)";
    }
    
    @Nonnull
    @Override
    public String getDescription() {
        
        return "Creates a countdown until specified datetime. Time has to be in format hh:mm:ss dates have to be in format yyyy-MM-dd";
    }
    
    @Nonnull
    @Override
    public String getReturnType() {
        
        return "Id of the countdown. Use counter(<id>) to get the value";
    }
}
