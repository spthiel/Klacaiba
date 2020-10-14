package me.spthiel.klacaiba.newactions.with;

import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.variable.ItemID;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.actions.BaseScriptAction;

public class CountInvItem extends BaseScriptAction {
	
	public CountInvItem() {
		
		super("countiteminv");
	}
	
	@Override
	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		ReturnValue retVal = new ReturnValue(-1);
		if (params.length > 0) {
			retVal.setInt(this.findItem(provider, macro, params[0]));
		}
		
		return retVal;
	}
	
	private int findItem(IScriptActionProvider provider, IMacro macro, String unparsedId) {
		ItemID itemId    = tryParseItemID(provider.expand(macro, unparsedId, false));
		
		return itemId.isValid() ? searchInventoryFor(itemId, Minecraft.getMinecraft().player.inventoryContainer) : -1;
	}
	
	private int searchInventoryFor(ItemID itemId, Container inventorySlots) {
		
		return inventorySlots.inventorySlots
				.stream()
				.map(Slot :: getStack)
				.filter(slotStack -> stackMatchesID(itemId, slotStack))
				.mapToInt(ItemStack :: getCount)
				.sum();
	}
	
	private static boolean stackMatchesID(ItemID itemId, ItemStack slotStack) {
		return slotStack == null && itemId.item == null || slotStack != null && slotStack.getItem() == itemId.item && (itemId.damage == -1 || itemId.damage == slotStack.getMetadata());
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "#count = countiteminv(<item[:damage]>)";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Gets the amount of items of the specified type in your survival inventory";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Amount of items";
	}
}
