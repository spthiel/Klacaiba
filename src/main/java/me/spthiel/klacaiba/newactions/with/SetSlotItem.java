package me.spthiel.klacaiba.newactions.with;

import net.eq2online.macros.scripting.ReturnValueLog;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.eq2online.util.Util;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.CreativeCrafting;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.actions.BaseScriptAction;

public class SetSlotItem extends BaseScriptAction {
	
	public SetSlotItem() {
		
		super("setslotitem");
	}
	
	@Override
	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		EntityPlayerSP     thePlayer        = this.mc.player;
		PlayerControllerMP playerController = this.mc.playerController;
		IReturnValue returnValue = null;
		if (params.length > 0 && thePlayer != null && thePlayer.inventory != null && playerController != null && playerController.isInCreativeMode()) {
			ItemStack itemStack = tryParseItemID(provider.expand(macro, params[0], false)).toItemStack(1);
			int       maxStackSize;
			if (itemStack.getItem() != null) {
				if (params.length > 2) {
					maxStackSize = itemStack.getMaxStackSize();
					itemStack.setCount(Math.min(ScriptCore.tryParseInt(provider.expand(macro, params[2], false), 1), maxStackSize));
					if(params.length > 3) {
						String nbt = Util.convertAmpCodes(provider.expand(macro, params[3], false));
						try {
							NBTTagCompound compound = JsonToNBT.getTagFromJson(nbt);
							System.out.println(compound.toString());
							itemStack.setTagCompound(compound);
						} catch (NBTException e) {
							returnValue = new ReturnValueLog("Invalid NBT for setslotitem");
						}
					}
				}
			} else {
				itemStack = null;
			}
			
			maxStackSize = Math.min(Math.max(params.length > 1 ? ScriptCore.tryParseInt(provider.expand(macro, params[1], false), thePlayer.inventory.currentItem) : thePlayer.inventory.currentItem, 0), 8);
			CreativeCrafting crafting = new CreativeCrafting(this.mc);
			thePlayer.inventoryContainer.addListener(crafting);
			thePlayer.inventory.setInventorySlotContents(maxStackSize, itemStack);
			thePlayer.inventoryContainer.detectAndSendChanges();
			thePlayer.inventoryContainer.removeListener(crafting);
		}
		
		return returnValue;
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "setslotitem(<item[:damage]>,<slot>,[amount],[nbt])";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Creative mode only, set the contents of a hotbar slot";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "";
	}

}
