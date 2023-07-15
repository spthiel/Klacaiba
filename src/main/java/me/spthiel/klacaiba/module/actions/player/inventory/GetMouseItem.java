package me.spthiel.klacaiba.module.actions.player.inventory;

import net.eq2online.macros.scripting.api.*;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.config.ConfigGroups;
import me.spthiel.klacaiba.module.actions.base.BaseScriptAction;
import me.spthiel.klacaiba.utils.Utils;

public class GetMouseItem extends BaseScriptAction {
    
    public GetMouseItem() {
        super("getmouseitem");
    }
    
    @Override
    public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
    
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
    
    @Override
    public ConfigGroups getGroup() {
        
        return ConfigGroups.PLAYER;
    }
}
