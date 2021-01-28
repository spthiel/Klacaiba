package me.spthiel.klacaiba.module.iterators;

import net.eq2online.macros.scripting.ScriptedIterator;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.IMerchant;
import net.minecraft.village.MerchantRecipeList;

public class IteratorTrades extends ScriptedIterator {
    
    public IteratorTrades(IScriptActionProvider provider, IMacro macro) {
        
        super(provider, macro);
        Minecraft mc  = Minecraft.getMinecraft();
        GuiScreen gui = mc.currentScreen;
        if (gui == null || !(gui instanceof GuiMerchant)) {
            return;
        }
    
        IMerchant          merchant = ((GuiMerchant) gui).getMerchant();
        MerchantRecipeList recipes  = merchant.getRecipes(mc.player);
        
        if(recipes == null) {
            return;
        }
        
        recipes.forEach(recipe -> {
            this.begin();
            this.add("TRADEBUYITEM", recipe.getItemToBuy().getUnlocalizedName());
            this.add("TRADEBUYITEMAMOUNT", recipe.getItemToBuy().getCount());
    
            // second item e.g. book in enchanting books with emeralds trade
            this.add("TRADEBUYITEM2", recipe.getSecondItemToBuy().getUnlocalizedName());
            this.add("TRADEBUYITEM2AMOUNT", recipe.getSecondItemToBuy().getCount());
    
            this.add("TRADESELLITEM", recipe.getItemToSell().getUnlocalizedName());
            this.add("TRADESELLITEMAMOUNT", recipe.getItemToSell().getCount());
    
            this.add("TRADEUSES", recipe.getToolUses());
            this.add("TRADEMAXUSES", recipe.getMaxTradeUses());
            this.end();
        });
    }
}
