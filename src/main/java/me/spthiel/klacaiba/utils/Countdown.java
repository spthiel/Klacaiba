package me.spthiel.klacaiba.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Countdown implements Counting {
    
    private LocalDateTime target;
    
    public Countdown(LocalDateTime target) {
        this.target = target;
    }
    
    @Override
    public long getValue() {
        
        return LocalDateTime.now().until(target, ChronoUnit.SECONDS);
    }
}
