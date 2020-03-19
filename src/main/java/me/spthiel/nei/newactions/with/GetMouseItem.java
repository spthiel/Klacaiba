package me.spthiel.nei.newactions.with;

import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.eq2online.util.Game;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

import me.spthiel.nei.actions.BaseScriptAction;
import me.spthiel.nei.utils.Utils;

public class GetMouseItem extends BaseScriptAction {
    
    public GetMouseItem() {
        super("getmouseitem");
    }
    
    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
    
        ItemStack slotStack = Minecraft.getMinecraft().player.inventory.getItemStack();
    
        return Utils.getItemReturnValue(provider, macro, params, slotStack, 0);
    }
    
    @Nonnull
    @Override
    public String getUsage() {
        
        return "getmouseitem(<&idvar>,[#stacksizevar],[#datavar],[&nbt])";
    }
    
    @Nonnull
    @Override
    public String getDescription() {
        
        return "Gets info about the held item";
    }
    
    @Nonnull
    @Override
    public String getReturnType() {
        
        return "Item ID of the held item";
    }
}
