package me.spthiel.klacaiba.module.actions.player;

import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.variable.ItemID;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.config.ConfigGroups;
import me.spthiel.klacaiba.module.actions.base.BaseScriptAction;

public class GetBreakTime extends BaseScriptAction {
	
	public GetBreakTime() {
		super("getbreaktime");
	}
	
	@Override
	public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		if (params.length == 0) {
			return new ReturnValue(0);
		}
		
		ItemID      i     = tryParseItemID(provider.expand(macro, params[0], false));
		IBlockState state = Block.getBlockFromItem(i.item).getBlockState().getBaseState();
		
		float damagePerTick = state.getPlayerRelativeBlockHardness(Minecraft.getMinecraft().player, Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getPosition());
		
		return new ReturnValue((int)Math.ceil(1/damagePerTick));
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "getbreakspeed(<blockid>)";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Returns the amount of time required to break a block";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "ticks to break the block or 0 if unbreakable";
	}
	
	@Override
	public ConfigGroups getGroup() {
		
		return ConfigGroups.WORLD;
	}
}
