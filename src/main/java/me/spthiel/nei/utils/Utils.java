package me.spthiel.nei.utils;

import java.time.Duration;

public class Utils {
    
    
    public static String formatTime(Duration duration, String format) {
        
        if (format.contains("dd")) {
            
            long days = duration.toDays();
            long hours = duration.toHours() - days * 24;
            long minutes = duration.toMinutes() - hours * 60 - days * 24 * 60;
            long seconds = duration.getSeconds() - minutes * 60 - hours * 3600 - days * 24 * 3600;
            return replace(format, days, hours, minutes, seconds);
            
        } else if (format.contains("hh")) {
            
            long hours = duration.toHours();
            long minutes = duration.toMinutes() - hours * 60;
            long seconds = duration.getSeconds() - minutes * 60 - hours * 3600;
            return replace(format, 0, hours, minutes, seconds);
            
        } else if (format.contains("mm")) {
            
            long minutes = duration.toMinutes();
            long seconds = duration.getSeconds() - minutes * 60;
            return replace(format, 0, 0, minutes, seconds);
            
        } else if (format.contains("ss")) {
            return replace(format, 0, 0, 0, duration.getSeconds());
        } else {
            return format;
        }
    }
    
    
    private static String replace(String format, long day, long hour, long minute, long second) {
        
        return format.replace("dd", String.format("%02d", day))
                     .replace("hh", String.format("%02d", hour))
                     .replace("mm", String.format("%02d", minute))
                     .replace("ss", String.format("%02d", second));
    }
}
