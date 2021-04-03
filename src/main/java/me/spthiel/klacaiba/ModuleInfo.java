package me.spthiel.klacaiba;

/**
 * Shared information for this module
 */
public abstract class ModuleInfo {
    
    /**
     * Target API version
     */
    public static final int API_VERSION = 26;
    
    public static final short PATCH = 0;
    public static final short MINOR = 2;
    public static final short MAJOR = 2;
    
    public static final int VERSION = MAJOR*100000 + MINOR * 1000 + PATCH;

    /**
     * Private ctor, no instances 
     */
    private ModuleInfo() { }

}
