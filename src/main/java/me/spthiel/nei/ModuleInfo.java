package me.spthiel.nei;

import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.ScriptCore;

import java.lang.reflect.Field;

/**
 * Shared information for this module
 */
public abstract class ModuleInfo {
    
    /**
     * Target API version
     */
    public static final int API_VERSION = 26;

    /**
     * Private ctor, no instances 
     */
    private ModuleInfo() { }

}
