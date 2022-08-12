package me.spthiel.klacaiba.module.actions.language;

import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.base.BaseScriptAction;

public class LeftPad extends BaseScriptAction {
    
    public LeftPad() {
        
        super("leftpad");
    }
    
    @Override
    public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        
        if(params.length > 0) {
            int length = 2;
            char character = '0';
            char[] characters = null;
            boolean useArray = false;
            if(params.length > 1) {
                if(params.length > 2) {
                    String value = provider.expand(macro, params[2], false);
                    String lowercase;
                    if(value.length() == 1) {
                        character = value.charAt(0);
                    } else if((lowercase = value.toLowerCase()).matches("(?:\\\\u|u\\+)?[0-9a-f]{4,7}")) {
                        String hexvalue = lowercase.replaceAll("(?:\\\\u|u\\+)?([0-9a-f]{4,7})","$1");
                        characters = Character.toChars(Integer.parseInt(hexvalue, 16));
                        useArray = true;
                    } else if(value.matches("\\d{1,9}")) {
                        characters = Character.toChars(Integer.parseInt(value));
                        useArray = true;
                    }
                }
                String value = provider.expand(macro,  params[1], false);
                try {
                    length = Integer.parseInt(value);
                } catch(NumberFormatException ignored) {
                
                }
                if(length < 0) {
                    length = 2;
                }
            }
            
            String value = provider.expand(macro, params[0], false);
            StringBuilder filling = new StringBuilder();
            for(int i = value.length(); i < length; i++) {
                if(useArray) {
                    filling.append(characters);
                } else {
                    filling.append(character);
                }
            }
            return new ReturnValue(filling.toString() + value);
        } else {
            return new ReturnValue("00");
        }
    }
    
    @Nonnull
    @Override
    public String getUsage() {
        
        return "leftpad(<value>,[tofillup],[character])";
    }
    
    @Nonnull
    @Override
    public String getDescription() {
        
        return "Adds leading <character> (0 by default, supports unicode numbers) to the value until the length is above <tofillup> (2 by default)";
    }
    
    @Nonnull
    @Override
    public String getReturnType() {
        
        return "String with leading <character>";
    }
}
