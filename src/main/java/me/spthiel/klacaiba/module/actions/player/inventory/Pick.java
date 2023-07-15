package me.spthiel.klacaiba.module.actions.player.inventory;

import net.eq2online.macros.scripting.Variable;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.variable.ItemID;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import me.spthiel.klacaiba.config.ConfigGroups;
import me.spthiel.klacaiba.module.actions.base.BaseScriptAction;

public class Pick extends BaseScriptAction {
	
	public Pick() {
		
		super("pick");
	}
	
	public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		ReturnValue retVal = new ReturnValue(-1);
		boolean     isDone = false;
		
		List<String> parsedParams = new ArrayList<>();
		for (String param : params) {
			if (Variable.couldBeArraySpecifier(param)) {
				int itemCount = provider.getArraySize(macro, param);
				for (int i = 0 ; i < itemCount ; i++) {
					Object element = provider.getArrayElement(macro, param, i);
					parsedParams.add(element.toString());
				}
			} else {
				parsedParams.add(provider.expand(macro, param, false));
			}
		}
		
		for (String parsedParam : parsedParams) {
			ItemID itemId = tryParseItemID(parsedParam);
			
			if (itemId.identifier.equals("air")) {
				boolean found = false;
				for (int slot = 0 ; slot <= 8 ; ++slot) {
					ItemStack itemStack = mc.player.inventory.getStackInSlot(slot);
					if (itemStack.isEmpty()) {
						provider.actionInventorySlot(slot + 1);
						retVal.setString(itemId.identifier);
						found = true;
						break;
					}
				}
				if (found) {
					break;
				}
			}
			
			if (itemId.isValid() && provider.actionInventoryPick(itemId.identifier, itemId.damage)) {
				retVal.setString(itemId.identifier);
				break;
			}
		}
		
		return retVal;
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "pick(<item[:damage]|&items[]>,[item[:damage]|&items[]],...)";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Selects the specified item ID if it is on the hotbar. Specify multiple items to pick, in order of preference. Returns the selected item name.";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Item ID of the item picked. -1 if none of the items was found.";
	}
	
	@Override
	public ConfigGroups getGroup() {
		
		return ConfigGroups.MOD;
	}
}
