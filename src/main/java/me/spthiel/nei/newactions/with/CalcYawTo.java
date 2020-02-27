package me.spthiel.nei.newactions.with;

import net.eq2online.macros.scripting.actions.lang.ScriptActionCalcYawTo;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

import me.spthiel.nei.actions.BaseScriptAction;
import me.spthiel.nei.actions.IDocumentable;

public class CalcYawTo extends BaseScriptAction {

	public CalcYawTo() {
		super("calcyawto");
	}

	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		ReturnValue retVal = new ReturnValue(0);
		if (params.length > 1 && this.mc != null && this.mc.player != null) {
			if (params.length > 2 && provider.expand(macro, params[2], false).trim().matches("-?\\d+")) {
				float xPos = (float) ScriptCore.tryParseInt(provider.expand(macro, params[0], false), 0) + 0.5F;
				float yPos = (float) ScriptCore.tryParseInt(provider.expand(macro, params[1], false), 0) + 0.5F;
				float zPos = (float) ScriptCore.tryParseInt(provider.expand(macro, params[2], false), 0) + 0.5F;
				double deltaX = (double) xPos - this.mc.player.posX;
				double deltaY = (double) yPos - this.mc.player.posY;
				double deltaZ = (double) zPos - this.mc.player.posZ;
				double distance = MathHelper.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);


				double dyFromEyes = deltaY + this.mc.player.getEyeHeight();
				double pitchFromPlayer = (Math.atan2(dyFromEyes, Math.sqrt(deltaX * deltaX + deltaZ * deltaZ)) * 180.0D / Math.PI);
				while (pitchFromPlayer < 0)
					pitchFromPlayer += 360;

				String varName;
				if (params.length > 3) {

					int yaw;
					yaw = (int) (Math.atan2(deltaZ, deltaX) * 180.0D / Math.PI - 90.0D);
					while (yaw < 0) {

						yaw += 360;
					}

					varName = provider.expand(macro, params[3], false);
					provider.setVariable(macro, varName, yaw);
				}

				if (params.length > 4) {
					varName = provider.expand(macro, params[4], false);
					provider.setVariable(macro, varName, MathHelper.floor(distance));
				}

				if (params.length > 5) {
					varName = provider.expand(macro, params[5], false);
					provider.setVariable(macro, varName, MathHelper.floor(pitchFromPlayer));
				}
			} else {
				float xPos = (float)ScriptCore.tryParseInt(provider.expand(macro, params[0], false), 0) + 0.5F;
				float zPos = (float)ScriptCore.tryParseInt(provider.expand(macro, params[1], false), 0) + 0.5F;
				double deltaX = (double)xPos - this.mc.player.posX;
				double deltaZ = (double)zPos - this.mc.player.posZ;
				double distance = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ);

				int yaw;
				yaw = (int)(Math.atan2(deltaZ, deltaX) * 180.0D / 3.141592653589793D - 90.0D);
				while (yaw < 0) {
					yaw += 360;
				}

				retVal.setInt(yaw);
				String varName;
				if (params.length > 2) {
					varName = provider.expand(macro, params[2], false);
					provider.setVariable(macro, varName, yaw);
				}

				if (params.length > 3) {
					varName = provider.expand(macro, params[3], false);
					provider.setVariable(macro, varName, MathHelper.floor(distance));
				}
			}
		}

		return retVal;
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "calcyawto(<xpos>,<ypos>,<zpos>,[#yaw],[#dist],[#pitch])";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Calculates yaw and pitch to a position.";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Yaw value to coordinates";
	}
}
