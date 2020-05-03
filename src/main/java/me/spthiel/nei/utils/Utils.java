package me.spthiel.nei.utils;

import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.api.ReturnValue;
import net.eq2online.util.Game;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

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
    
    public static IReturnValue getItemReturnValue(IScriptActionProvider provider, IMacro macro, String[] params, ItemStack slotStack) {
        return getItemReturnValue(provider, macro, params, slotStack, 1);
    }
    
    public static IReturnValue getItemReturnValue(IScriptActionProvider provider, IMacro macro, String[] params, ItemStack slotStack, int start) {
    
        String itemID = "unknown";
        int stackSize = 0;
        int damage = 0;
        String tag = "null";
        if (slotStack == null) {
            itemID = Game.getItemName((Item)null);
        } else {
            itemID = Game.getItemName(slotStack.getItem());
            stackSize = slotStack.getCount();
            damage = slotStack.getMetadata();
            NBTTagCompound tagCompound = slotStack.getTagCompound();
            tag = (tagCompound == null ? "null" : tagCompound.toString());
        }
        
        Object[] out = {
            itemID,
            stackSize,
            damage,
            tag
        };
        
        setParams(macro, provider, params, out, start);
    
        return new ReturnValue(itemID);
    }
    
    public static void setParams(IMacro macro, IScriptActionProvider provider, String[] params, Object[] value, int start) {
        for(int i = start; i < params.length; i++) {
            int idx = i-start;
            Object obj;
            if(idx >= value.length) {
                obj = "INVALID_ARG";
            } else {
                obj = value[idx];
            }
            if(obj instanceof Integer) {
                provider.setVariable(macro, provider.expand(macro, params[i], false), (int)obj);
            } else {
                provider.setVariable(macro, provider.expand(macro, params[i], false), obj.toString());
            }
        }
    }
}
