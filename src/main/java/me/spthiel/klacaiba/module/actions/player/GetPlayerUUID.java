package me.spthiel.klacaiba.module.actions.player;

import com.mojang.authlib.GameProfile;
import com.mumfrey.liteloader.core.LiteLoader;
import net.eq2online.macros.scripting.api.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.base.BaseConditionalOperator;
import me.spthiel.klacaiba.base.BaseScriptAction;

public class GetPlayerUUID extends BaseScriptAction {
	
	public GetPlayerUUID() {
		
		super("getplayeruuid");
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "&uuid = getplayeruuid(<playername>)";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Get the uuid of an online player";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "UUID of the online player or false if no player was found";
	}
	
	@Override
	public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		if (params.length == 0) {
			return new ReturnValue(false);
		}
		
		String playername = provider.expand(macro, params[0], false);
		
		NetworkPlayerInfo playerInfo = Minecraft.getMinecraft().player.connection.getPlayerInfo(playername);
		if (playerInfo == null) {
			return new ReturnValue(false);
		}
		
		return new ReturnValue(playerInfo.getGameProfile().getId().toString());
	}
}
