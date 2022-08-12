package me.spthiel.klacaiba.module.actions.information;

import net.eq2online.macros.scripting.api.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.base.BaseScriptAction;

public class GetChestName extends BaseScriptAction {
    
    public GetChestName() {
        super("getchestname");
    }
    
    @Override
    public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
    
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if(player.openContainer instanceof ContainerChest) {
            ContainerChest chest = (ContainerChest)player.openContainer;
            IInventory inv = chest.getLowerChestInventory();
            return new ReturnValue(inv.getName());
        }
        return new ReturnValue("null");
    }
    
    @Nonnull
    @Override
    public String getUsage() {
        
        return "getchestname()";
    }
    
    @Nonnull
    @Override
    public String getDescription() {
        
        return "Returns the name of the open chest";
    }
    
    @Nonnull
    @Override
    public String getReturnType() {
        
        return "Name of chest or null";
    }
}
