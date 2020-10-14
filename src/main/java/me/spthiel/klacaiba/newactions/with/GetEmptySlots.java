package me.spthiel.klacaiba.newactions.with;

import net.eq2online.macros.scripting.api.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.actions.BaseScriptAction;

public class GetEmptySlots extends BaseScriptAction {
    
    public GetEmptySlots() {
        super("getemptyslots");
    }
    
    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
    
        boolean includeNonFull = false;
        
        if(params.length > 0) {
            includeNonFull = provider.getExpressionEvaluator(macro, provider.expand(macro, params[0], false)).evaluate() != 0;
        }
        
        return new ReturnValue(calculateEmptyInventorySlots(includeNonFull));
    }
    
    private int calculateEmptyInventorySlots(boolean includeNonFullStacks) {
        InventoryPlayer inventory      = Minecraft.getMinecraft().player.inventory;
        return (int)inventory.mainInventory.stream()
                .filter(stack -> stack.isEmpty() || (includeNonFullStacks && stack.getMaxStackSize() > stack.getCount()))
                .count();
    }
    
    @Nonnull
    @Override
    public String getUsage() {
        
        return "getemptyslots([include non full slots])";
    }
    
    @Nonnull
    @Override
    public String getDescription() {
        
        return "Returns the amount of empty (or optionally non full slots) of the player inventory";
    }
    
    @Nonnull
    @Override
    public String getReturnType() {
        
        return "Amount of empty or non full slots";
    }
}
