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

public class IfEnchanted extends ScriptActionIf {
    
    protected IfEnchanted() {
        
        super(ScriptContext.MAIN, "ifenchanted");
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
        return slotStack != null && slotStack.isItemEnchanted();
    }
    
    @Override
    public void onInit() {
        this.context.getCore().registerScriptAction(this);
    }
}
