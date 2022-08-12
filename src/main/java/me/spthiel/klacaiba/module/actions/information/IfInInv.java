package me.spthiel.klacaiba.module.actions.information;

import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.variable.ItemID;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.base.BaseConditionalOperator;

public class IfInInv extends BaseConditionalOperator {
	
	public IfInInv() {
		
		super("ifininv");
	}
	
	@Override
	public boolean executeConditional(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		if (params.length == 0) {
			return false;
		}
		
		int count = 1;
		int first = 1;
		String param0 = provider.expand(macro, params[0], false);
		
		if (param0.equalsIgnoreCase("all")) {
			count = params.length-1;
		} else if (param0.matches("\\d+")) {
			count = Integer.parseInt(param0);
		} else if (param0.matches("\\d+%")) {
			count = ((params.length-1)*100)/Integer.parseInt(param0);
		} else if (!param0.equalsIgnoreCase("any")){
			first = 0;
		}
		
		ItemID[] items = new ItemID[params.length-first];
		for (int i = first ; i < params.length ; i++) {
			items[i-first] = tryParseItemID(provider.expand(macro, params[i] , false));
		}
		
		int matched = 0;
		
		for(Slot slot : Minecraft.getMinecraft().player.inventoryContainer.inventorySlots) {
			for (int i = 0 ; i < items.length ; i++) {
				ItemID id = items[i];
				if (stackMatchesID(id, slot.getStack())) {
					matched++;
					items[i] = null;
					if (matched >= count) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	private static boolean stackMatchesID(ItemID itemId, ItemStack slotStack) {
		return itemId != null && itemId.item != null && slotStack != null && slotStack.getItem() == itemId.item && (itemId.damage == -1 || itemId.damage == slotStack.getMetadata());
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "ifininv([type],<...items>)";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Checks whether a set amount of itemtypes is contained in the inventory. Type is 'any' by default and can be 'any', 'all', a number or a percentage";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "";
	}
}
