package me.spthiel.klacaiba.module.actions.information.counter;

import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.variable.ItemID;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.stream.IntStream;

import me.spthiel.klacaiba.base.BaseScriptAction;

public class CountItem extends BaseScriptAction {
	
	public CountItem() {
		
		super("countitem");
	}
	
	@Override
	public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		ReturnValue retVal = new ReturnValue(-1);
		if (params.length > 0) {
			retVal.setInt(this.findItem(provider, macro, params[0]));
		}
		
		return retVal;
	}
	
	private int findItem(IScriptActionProvider provider, IMacro macro, String unparsedId) {
		ItemID itemId = tryParseItemID(provider.expand(macro, unparsedId, false));
		
		if (this.mc.currentScreen instanceof GuiContainer) {
			
			return itemId.isValid() ? searchInventoryFor(itemId, ((GuiContainer) this.mc.currentScreen).inventorySlots) : -1;
		} else if(this.mc.currentScreen == null && this.mc.player != null && this.mc.player.inventory != null && this.mc.player.inventory.mainInventory != null) {
			
			return itemId.isValid() ? searchInventoryFor(itemId, null) : -1;
		}
		
		return -1;
		
	}
	
	private int searchInventoryFor(ItemID itemId, Container inventorySlots) {
		
		if(inventorySlots == null) {
			
			List<ItemStack> slots = this.mc.player.inventory.mainInventory;
			
			return IntStream
					.range(0, 9)
					.mapToObj(slots :: get)
					.filter(itemStack -> stackMatchesID(itemId, itemStack))
					.mapToInt(ItemStack :: getCount)
					.sum();
			
		}
		
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
		
		return "#count = countitem(<item[:damage]>)";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Gets the amount of items of the specified type in your current inventory";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Amount of items";
	}
	
}
