package me.spthiel.klacaiba.module.actions.information;

import net.eq2online.macros.compatibility.I18n;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.variable.ItemID;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.base.BaseConditionalOperator;

public class IfInvIsFull extends BaseConditionalOperator {

	public IfInvIsFull() {
		super("ifinvisfull");
	}

	public String getExpectedPopCommands() {
		return I18n.get("script.error.stackhint", this, "ELSEIF\u00A7c, \u00A7dELSE\u00A7c or \u00A7dENDIF");
	}

	public boolean isConditionalOperator() {
		return true;
	}

	public boolean executeConditional(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        
		InventoryPlayer inventory = Minecraft.getMinecraft().player.inventory;
		ItemID itemId;
		if(params.length > 0) {
			itemId = tryParseItemID(provider.expand(macro, provider.expand(macro, params[0], false), false));
		} else {
			itemId = null;
		}
		return !inventory.mainInventory.stream()
									  .anyMatch(stack -> stack.isEmpty() || (itemId != null && itemId.equals(stack) && stack.getCount() < stack.getMaxStackSize()));
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "ifinvisfull([item])";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Checks whether the inventory does not contain a slot with air or a slot that could take an item of the specified kind.";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "";
	}
}
