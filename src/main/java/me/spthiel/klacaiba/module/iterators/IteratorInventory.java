package me.spthiel.klacaiba.module.iterators;

import net.eq2online.macros.scripting.ScriptedIterator;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.util.Game;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class IteratorInventory extends ScriptedIterator {
	
	public IteratorInventory(IScriptActionProvider provider, IMacro macro) {
		
		super(provider, macro);
		
		List<Slot> itemStacks = this.mc.player.inventoryContainer.inventorySlots;
		
		for (int i = 0 ; i < itemStacks.size() ; i++) {
			Slot slot = itemStacks.get(i);
			ItemStack itemStack = slot.getStack();
			this.begin();
			this.add("SLOTINDEX", i);
			this.add("SLOTID", Game.getItemName(itemStack.getItem()));
			this.add("SLOTSTACKSIZE", itemStack.getCount());
			this.add("SLOTDATAVAR", itemStack.getMetadata());
			NBTTagCompound tagCompound = itemStack.getTagCompound();
			String tag = (tagCompound == null ? "null" : tagCompound.toString());
			this.add("SLOTTAG", tag);
			this.end();
		}
	}
}
