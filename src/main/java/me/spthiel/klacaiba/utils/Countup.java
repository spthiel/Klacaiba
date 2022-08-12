package me.spthiel.klacaiba.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Countup implements Counting {
    
    private Instant from;
    
    public Countup() {
        from = Instant.now();
    }
    
    @Override
    public long getValue() {
        
        return from.until(Instant.now(), ChronoUnit.SECONDS);
    }
}
