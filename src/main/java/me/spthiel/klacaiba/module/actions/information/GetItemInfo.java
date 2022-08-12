package me.spthiel.klacaiba.module.actions.information;

import net.eq2online.macros.scripting.api.*;
import net.eq2online.util.Game;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.base.BaseScriptAction;

public class GetItemInfo extends BaseScriptAction {
	
	public GetItemInfo() {
		super("getiteminfo");
	}
	
	@Override
	public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		ReturnValue retVal = new ReturnValue("None");
		if (params.length >= 1) {
			ItemStack itemStack = tryParseItemID(provider.expand(macro, params[0], false)).toItemStack(1);
			Item      item      = itemStack.getItem();
			if (item != null) {
				String stackType = "ITEM";
				String idDropped = Game.getItemName(item);
				if (item instanceof ItemBlock) {
					stackType = "TILE";
				}
				
				retVal.setString(itemStack.getDisplayName());
				if (params.length > 1) {
					provider.setVariable(macro, params[1], itemStack.getDisplayName());
				}
				if (params.length > 2) {
					provider.setVariable(macro, params[2], itemStack.getMaxStackSize());
				}
				
				if (params.length > 3) {
					provider.setVariable(macro, params[3], stackType);
				}
				
				if (params.length > 4) {
					provider.setVariable(macro, params[4], idDropped);
				}
				
				if (params.length > 5) {
					provider.setVariable(macro, params[5], item.getMaxDamage());
				}
			} else {
				provider.setVariable(macro, params[1], "None");
				if (params.length > 2) {
					provider.setVariable(macro, params[2], 0);
				}
				
				if (params.length > 3) {
					provider.setVariable(macro, params[3], "NONE");
				}
				
				if (params.length > 4) {
					provider.setVariable(macro, params[4], "");
				}
				
				if (params.length > 5) {
					provider.setVariable(macro, params[4], 0);
				}
			}
		}
		
		return retVal;
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "GETITEMINFO(<item[:damage]>,[&namevar],[#maxstacksize],[&type],[&dropid],[#maxdurability])";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Gets the name and other info for the specified item id";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Returns the item name";
	}
}
