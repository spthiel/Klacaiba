package net.eq2online.example.actions;

import net.eq2online.example.base.BaseScriptAction;
import net.eq2online.example.ModuleInfo;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.variable.ItemID;
import net.eq2online.util.Game;
import net.minecraft.item.ItemStack;
import java.util.Arrays;
import javax.annotation.Nonnull;

@APIVersion(ModuleInfo.API_VERSION)
public class Pick extends BaseScriptAction {

    public Pick() {
        super("pick");
    }

    public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        ReturnValue retVal = new ReturnValue(-1);
        boolean isDone = false;

        // Splitting item IDs by commas and |;
        String[] parsedParamsArray = new String[0];
        for (String param : params) {
            if (param.contains(",")||param.contains("|")) {
                String[] splitParams = param.split("[,|]");
                parsedParamsArray = Arrays.copyOf(parsedParamsArray, parsedParamsArray.length + splitParams.length);
                System.arraycopy(splitParams, 0, parsedParamsArray, parsedParamsArray.length - splitParams.length, splitParams.length);
            } else {
                parsedParamsArray = Arrays.copyOf(parsedParamsArray, parsedParamsArray.length + 1);
                parsedParamsArray[parsedParamsArray.length - 1] = param;
            }
        }

        // If the game is in Creative Mode, keep it as MKB does by default;
        if (mc.player.isCreative()) {
            for (String parsedParam : parsedParamsArray) {
                ItemID itemId = tryParseItemID(parsedParam);
                if (itemId.isValid() && provider.actionInventoryPick(itemId.identifier, itemId.damage)) {
                    retVal.setString(itemId.identifier);
                    break;
                }
            }
        } else {
            for (String parsedParam : parsedParamsArray) {
                // Find the first slot from the hotbar that contains an item from the inputs;
                // These changes also include 'air' as a possible entry;

                ItemID itemId = tryParseItemID(parsedParam);
                int itemDamage = itemId.damage;
                if (itemDamage == -1)
                    itemDamage = 0;

                for (int slot = 0; slot <= 8; ++slot) {
                    ItemStack itemStack = mc.player.inventory.getStackInSlot(slot);
                    if ((itemStack.isEmpty() && itemId.identifier.equals("air")) || (itemId.isValid() && (itemId.identifier.equals(Game.getItemName(itemStack.getItem())) && (itemDamage == itemStack.getMetadata())))) {
                        provider.actionInventorySlot(slot + 1);
                        retVal.setString(itemId.identifier);
                        isDone = true;
                        break;
                    }
                }
                if (isDone)
                    break;
            }
        }
        return retVal;
    }

    public boolean isThreadSafe() {
        return false;
    }

    public boolean isPermissable() {
        return true;
    }

    public String getPermissionGroup() {
        return "inventory";
    }

    @Nonnull
    //@Override
    public String getUsage() {
        return "pick(<item[:damage]>,[item[:damage]],...)";
    }

    @Nonnull
    //@Override
    public String getDescription() {
        return "Selects the specified item ID if it is on the hotbar. Specify multiple items to pick, in order of preference. Returns the selected item name.";
    }

    @Nonnull
    //@Override
    public String getReturnType() {
        return "Item ID of the item picked. -1 if none of the items was found.";
    }
}
