package me.spthiel.nei.newactions.with;

import net.eq2online.macros.compatibility.I18n;
import net.eq2online.macros.scripting.actions.lang.ScriptActionIf;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

import me.spthiel.nei.actions.BaseConditionalOperator;
import me.spthiel.nei.utils.Utils;

public class IfEnchanted extends BaseConditionalOperator {
    
    public IfEnchanted() {
        
        super("ifenchanted");
    }
    
    public String getExpectedPopCommands() {
        return I18n.get("script.error.stackhint", this, "ELSEIF\u00A7c, \u00A7dELSE\u00A7c or \u00A7dENDIF");
    }
    
    public boolean isConditionalOperator() {
        return true;
    }
    
    @Override
    public boolean executeConditional(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
    
        ItemStack slotStack = null;
        if (params.length > 0) {
            int slotId = Math.max(0, ScriptCore.tryParseInt(provider.expand(macro, params[0], false), 0));
            slotStack = this.slotHelper.getSlotStack(slotId);
        }
        Utils.getItemReturnValue(provider, macro, params, slotStack);
        return slotStack != null && slotStack.isItemEnchanted();
    }
    
    @Nonnull
    @Override
    public String getUsage() {
        
        return "ifenchanted(<slotid>,[&item],[#stacksize],[#datavar],[&nbt])";
    }
    
    @Nonnull
    @Override
    public String getDescription() {
        
        return "Checks if the item is enchanted";
    }
    
    @Nonnull
    @Override
    public String getReturnType() {
        
        return "";
    }
}
