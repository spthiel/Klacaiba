package me.spthiel.klacaiba.module.actions.player;

import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.variable.ItemID;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.base.BaseConditionalOperator;
import me.spthiel.klacaiba.base.BaseScriptAction;

public class IfCanHarvestBlock extends BaseConditionalOperator {
	
	public IfCanHarvestBlock() {
		super("ifcanharvestblock");
	}
	
	@Override
	public boolean executeConditional(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		if (params.length == 0) {
			return false;
		}
		
		ItemID      i     = tryParseItemID(provider.expand(macro, params[0], false));
		IBlockState state = Block.getBlockFromItem(i.item).getBlockState().getBaseState();
		
		return Minecraft.getMinecraft().player.canHarvestBlock(state);
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "ifcanharvestblock(<blockid[:datavar]>)";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Checks if the block of &blockid can currently be harvested";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "";
	}
}
