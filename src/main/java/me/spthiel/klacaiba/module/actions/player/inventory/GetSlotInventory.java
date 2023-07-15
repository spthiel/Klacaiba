package me.spthiel.klacaiba.module.actions.player.inventory;

import net.eq2online.macros.scripting.Variable;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.eq2online.macros.scripting.variable.ItemID;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

import me.spthiel.klacaiba.config.ConfigGroups;
import me.spthiel.klacaiba.module.actions.base.BaseScriptAction;

public class GetSlotInventory extends BaseScriptAction {

	public GetSlotInventory() {
		super("getslotinv");
	}
	
	public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		ReturnValue retVal = new ReturnValue(-1);
		if (params.length > 1) {
			String varName = provider.expand(macro, params[1], false).toLowerCase();
			if (!Variable.couldBeInt(varName)) {
				return retVal;
			}
			
			int slotContaining = this.findItem(provider, macro, provider.expand(macro, params[0], false), params.length > 2 ? provider.expand(macro, params[2], false) : null);
			if (slotContaining > -1) {
				provider.setVariable(macro, varName, slotContaining);
				retVal.setInt(slotContaining);
				return retVal;
			}
			
			provider.setVariable(macro, varName, -1);
		} else if (params.length > 0) {
			retVal.setInt(this.findItem(provider, macro, params[0], null));
		}
		
		return retVal;
	}
	
	private int findItem(IScriptActionProvider provider, IMacro macro, String unparsedId, String unparsedStart) {
		ItemID itemId    = tryParseItemID(provider.expand(macro, unparsedId, false));
		int    startSlot = 0;
		if (unparsedStart != null) {
			startSlot = Math.max(0, ScriptCore.tryParseInt(provider.expand(macro, unparsedStart, false), 0));
		}
		
		return itemId.isValid() ? searchInventoryFor(itemId, startSlot, Minecraft.getMinecraft().player.inventoryContainer) : -1;
	}
	
	private int searchInventoryFor(ItemID itemId, int startSlot, Container inventorySlots) {
		List<Slot> itemStacks = inventorySlots.inventorySlots;
		
		for(int slotContaining = startSlot; slotContaining < itemStacks.size(); ++slotContaining) {
			ItemStack slotStack = itemStacks.get(slotContaining).getStack();
			if (stackMatchesID(itemId, slotStack)) {
				return slotContaining;
			}
		}
		
		return -1;
	}
	
	private static boolean stackMatchesID(ItemID itemId, ItemStack slotStack) {
		return slotStack == null && itemId.item == null || slotStack != null && slotStack.getItem() == itemId.item && (itemId.damage == -1 || itemId.damage == slotStack.getMetadata());
	}

	@Nonnull
	@Override
	public String getUsage() {
		
		return "getslotinv(<item[:damage]>,<#idvar>,[startfromslotid])";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Gets the id of the slot containing an item matching the specified item id from your inventory (at all time), returns -1 if item not found";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Slot ID containing a matching item";
	}
	
	@Override
	public ConfigGroups getGroup() {
		
		return ConfigGroups.PLAYER;
	}
}
