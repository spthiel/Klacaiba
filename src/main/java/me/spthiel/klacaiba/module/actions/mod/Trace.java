package me.spthiel.klacaiba.module.actions.mod;

import net.eq2online.macros.scripting.actions.game.ScriptActionTrace;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.eq2online.macros.scripting.variable.providers.VariableProviderTrace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.RayTraceResult;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.base.BaseScriptAction;
import me.spthiel.klacaiba.utils.EntityUtilities;

public class Trace extends BaseScriptAction {
	
	public Trace() {
		
		super("trace");
	}
	
	@Override
	public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		if (params.length > 0) {
			float traceDistance = Math.min(Math.max(ScriptCore.tryParseFloat(provider.expand(macro, params[0], false), 0), 3), 256);
			boolean includeEntities = false;
			float yaw = Minecraft.getMinecraft().player.rotationYaw;
			float pitch = Minecraft.getMinecraft().player.rotationPitch;
			if (params.length > 1) {
				String traceEntitiesArg = provider.expand(macro, params[1], false);
				includeEntities = "true".equalsIgnoreCase(traceEntitiesArg) || "1".equals(traceEntitiesArg);
			}
			if (params.length > 2) {
				yaw = ScriptCore.tryParseFloat(provider.expand(macro, params[2], false), yaw);
			}
			if (params.length > 3) {
				pitch = ScriptCore.tryParseFloat(provider.expand(macro, params[3], false), yaw);
			}
			if (params.length > 4 && provider.expand(macro,params[4],false).startsWith("m")) {
				yaw -= 180;
			}
			yaw += 180;
			yaw %= 360;
			yaw -= 180;
			pitch %= 360;
			
			EntityPlayerSP thePlayer = this.mc.player;
			if (thePlayer != null) {
				RayTraceResult        ray       = EntityUtilities.rayTraceFromEntity(thePlayer, traceDistance, this.mc.getRenderPartialTicks(), yaw, pitch, includeEntities);
				VariableProviderTrace traceVars = macro.getState("trace");
				if (traceVars == null) {
					traceVars = new VariableProviderTrace(this.mc, ray);
					macro.setState("trace", traceVars);
					macro.registerVariableProvider(traceVars);
				} else {
					traceVars.update(ray);
				}
				
				return new ReturnValue(traceVars.getType());
			}
		}
		
		return new ReturnValue("NONE");
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "trace(<distance>,[entities],[yaw],[pitch],[mode])";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Performs a ray trace operation which sets the raytrace variables in the local scope.\n" +
			"\n" +
			"<distance> can be between 3 and 256\n" +
			"\n" +
			"[entities] can be true, if you want to include entities in your trace. [yaw] and [pitch] can be set to trace from different angles from player and [mode] can be set to cardinal or macromod to change between -180 <= yaw <= 180 and 0 <= yaw <= 360 (default cardinal yaw)";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Returns the type of the result, which can be one of the following values: TILE, PLAYER; ENTITY, NONE";
	}
}
