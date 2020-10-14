package me.spthiel.klacaiba.newactions.with;

import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

import java.util.List;

import me.spthiel.klacaiba.actions.BaseScriptAction;
import me.spthiel.klacaiba.utils.Utils;

public class GetSlotItemInventory extends BaseScriptAction {

	public GetSlotItemInventory() {
		super("getslotiteminv");
	}

	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		ItemStack slotStack = null;
		if (params.length > 0) {
			int slotId = Math.max(0, ScriptCore.tryParseInt(provider.expand(macro, params[0], false), 0));
			slotStack = getStackFromSurvivalInventory(slotId, Minecraft.getMinecraft().player.inventoryContainer);
		}
		
		return Utils.getItemReturnValue(provider, macro, params, slotStack);
    }
	/*
	 * Copied from SlotHelper.class
	 */
    private ItemStack getStackFromSurvivalInventory(int slotId, Container survivalInventory) {
		List<Slot> itemStacks = survivalInventory.inventorySlots;
		if (slotId >= 0 && slotId < itemStacks.size()) {
            return itemStacks.get(slotId).getStack();
		}
		
		return null;
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "getslotiteminv(<slotid>,<&idvar>,[#stacksizevar],[#datavar],[&nbt])";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Gets information about the item in the specified slot of the survival inventory (at all time)";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Item ID of the item in the slot";
	}
}
