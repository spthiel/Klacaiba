package me.spthiel.nei.newactions.with;

import net.eq2online.macros.scripting.api.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.projectile.EntityFishHook;

import javax.annotation.Nonnull;

import me.spthiel.nei.actions.BaseScriptAction;
import me.spthiel.nei.utils.Utils;

public class GetFishHook extends BaseScriptAction {
    
    public GetFishHook() {
        super("getfishhook");
    }
    
    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        
        EntityFishHook bobber = getFishHook();
        Object[] out;
        if(bobber == null) {
            out = new Object[]{
                    -1,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1
            };
        } else {
            out = new Object[]{
                    (int) bobber.posX,
                    getPrecision(bobber.posX),
                    (int) bobber.posY,
                    getPrecision(bobber.posY),
                    (int) bobber.posZ,
                    getPrecision(bobber.posZ)
            };
        }
    
        Utils.setParams(macro, provider, params, out, 0);
        
        return bobber == null ? new ReturnValue(-1) : new ReturnValue((int)(bobber.posY*1000));
    }
    
    private int getPrecision(double position) {
        position = position%1;
        
        return (int)Math.abs((position*1000)%1000);
    }
    
    private EntityFishHook getFishHook() {
        return Minecraft.getMinecraft().player.fishEntity;
    }
    
    @Nonnull
    @Override
    public String getUsage() {
        
        return "[#ytotal =] getfishhook([#x],[#xprecision],[#y],[#yprecision],[#z],[#zprecision])";
    }
    
    @Nonnull
    @Override
    public String getDescription() {
        
        return "Get the x, y and z and the first 3 decimal digits of the bobber or -1 for all of them if there is no fishhook";
    }
    
    @Nonnull
    @Override
    public String getReturnType() {
        
        return "y * 1000 or -1";
    }
}
