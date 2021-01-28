package me.spthiel.klacaiba.module.actions.world;

import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.eq2online.util.Game;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

import java.util.Set;

import me.spthiel.klacaiba.base.BaseScriptAction;

public class GetId extends BaseScriptAction {
	
	public GetId() {
		
		super("getid");
	}
	
	public GetId(String actionName) {
		
		super(actionName);
	}
	
	@Override
	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		ReturnValue retVal = new ReturnValue(Game.getBlockName((Block) null));
		if (params.length > 2) {
			WorldClient    theWorld  = this.mc.world;
			EntityPlayerSP thePlayer = this.mc.player;
			if (theWorld != null && thePlayer != null) {
				int         xPos       = this.getPosition(provider, macro, params[0], thePlayer.posX);
				int         yPos       = this.getPosition(provider, macro, params[1], thePlayer.posY);
				int         zPos       = this.getPosition(provider, macro, params[2], thePlayer.posZ);
				BlockPos    blockPos   = new BlockPos(xPos, yPos, zPos);
				IBlockState blockState = theWorld.getBlockState(blockPos);
				Block       block      = blockState.getBlock();
				retVal.setString(Game.getBlockName(block));
				String dmgVarName;
				if (params.length > 3) {
					dmgVarName = provider.expand(macro, params[3], false).toLowerCase();
					provider.setVariable(macro, dmgVarName, Game.getBlockName(block));
				}
				
				if (params.length > 4) {
					dmgVarName = provider.expand(macro, params[4], false).toLowerCase();
					blockState.getProperties();
					int blockDamage = block.damageDropped(blockState);
					provider.setVariable(macro, dmgVarName, blockDamage);
				}
				
				if (params.length > 5) {
					StringBuilder     variant    = new StringBuilder();
					Set<IProperty<?>> properties = blockState.getProperties().keySet();
					for (IProperty<?> property : properties) {
						
						variant.append(property.getName()).append('=');
						if(property instanceof PropertyBool) {
							variant.append(blockState.getValue(property).toString().toLowerCase());
						} else {
							variant.append(blockState
												   .getValue(property)
												   .toString()
												   .replaceAll("([A-Z])", "_$1")
												   .toLowerCase());
						}
						variant.append(',');
					}
					if(variant.length() > 0) {
						variant.deleteCharAt(variant.length() - 1);
					}
					provider.setVariable(
							macro,
							provider.expand(macro, params[5], false).toLowerCase(),
							variant.toString()
										);
				}
			}
		}
		
		return retVal;
	}
	
	private int getPosition(IScriptActionProvider provider, IMacro macro, String param, double currentPos) {
		
		String  sPos         = provider.expand(macro, param, false);
		boolean isRelative   = sPos.startsWith("~");
		int     iCurrentPosX = isRelative ? MathHelper.floor(currentPos) : 0;
		int     xPos         = iCurrentPosX + ScriptCore.tryParseInt(isRelative ? sPos.substring(1) : sPos, 0);
		return xPos;
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "GETID(<x>,<y>,<z>,[&idvar],[#datavar],[&variants])";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Gets the ID and optionally the data value and variants of the block at the specified coordinates in the world.";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Returns the name of the block.";
	}
}
