package me.spthiel.nei.newactions.with;

import net.eq2online.macros.scripting.actions.game.ScriptActionGetSlotItem;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.eq2online.util.Game;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

import me.spthiel.nei.actions.BaseScriptAction;
import me.spthiel.nei.actions.IDocumentable;

public class GetSlotItem extends BaseScriptAction {

	public GetSlotItem() {
		super("getslotitem");
	}

	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		String itemID = "unknown";
		int stackSize = 0;
		int damage = 0;
		String tag = "null";
		if (params.length > 0) {
			int slotId = Math.max(0, ScriptCore.tryParseInt(provider.expand(macro, params[0], false), 0));
			ItemStack slotStack = this.slotHelper.getSlotStack(slotId);
			if (slotStack == null) {
				itemID = Game.getItemName((Item)null);
			} else {
				itemID = Game.getItemName(slotStack.getItem());
				stackSize = slotStack.getCount();
				damage = slotStack.getMetadata();
				NBTTagCompound tagCompound = slotStack.getTagCompound();
				tag = (tagCompound == null ? "null" : tagCompound.toString());
			}
		}

		ReturnValue retVal = new ReturnValue(itemID);
		if (params.length > 1) {
			provider.setVariable(macro, provider.expand(macro, params[1], false), itemID);
		}

		if (params.length > 2) {
			provider.setVariable(macro, provider.expand(macro, params[2], false), stackSize);
		}

		if (params.length > 3) {
			provider.setVariable(macro, provider.expand(macro, params[3], false), damage);
		}

		if (params.length > 4) {
			provider.setVariable(macro, provider.expand(macro, params[4], false), tag);
		}

		return retVal;
	}

	public boolean isPermissable() {
		return true;
	}

	public String getPermissionGroup() {
		return "inventory";
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "getslot(<slotid>,<&idvar>,[#stacksizevar],[#datavar],[&nbt])";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Gets information about the item in the specified slot";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Item ID of the item in the slot";
	}
}
