package me.spthiel.klacaiba.module.actions.world;

import net.eq2online.macros.scripting.Variable;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

import java.util.Arrays;

import me.spthiel.klacaiba.base.BaseScriptAction;
import me.spthiel.klacaiba.base.FloatReturnValue;

public class CalcYawTo extends BaseScriptAction {

	public CalcYawTo() {
		super("calcyawto");
	}

	public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		FloatReturnValue retVal = new FloatReturnValue(0);
		if (params.length > 1 && this.mc != null && this.mc.player != null) {
			if (params.length > 2 && !Variable.isValidVariableName(provider.expand(macro, params[2], false))) {
				float xPos = getValue(provider, macro, params[0]);
				float yPos = getValue(provider, macro, params[1]);
				float zPos = getValue(provider, macro, params[2]);
				double deltaX = (double) xPos - this.mc.player.posX;
				double deltaY = (double) yPos - this.mc.player.posY;
				double deltaZ = (double) zPos - this.mc.player.posZ;
				double distance = MathHelper.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
				
				double dyFromEyes = deltaY - this.mc.player.getEyeHeight();
				double pitchFromPlayer = (Math.atan2(dyFromEyes, Math.sqrt(deltaX * deltaX + deltaZ * deltaZ)) * 180.0D / Math.PI);
				while (pitchFromPlayer < 0)
					pitchFromPlayer += 360;

				String varName;
				if (params.length > 3) {

					double yaw;
					yaw = (Math.atan2(deltaZ, deltaX) * 180.0D / Math.PI - 90.0D);
					while (yaw < 0) {
						yaw += 360;
					}

					setVariableFloat(provider, macro, params[3], yaw);
				}

				if (params.length > 4) {
					setVariableFloat(provider, macro, params[4], distance);
				}

				if (params.length > 5) {
					setVariableFloat(provider, macro, params[5], 360-pitchFromPlayer);
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

				retVal.setValue(yaw);
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
	
	private void setVariableFloat(IScriptActionProvider provider, IMacro macro, String variable, double value) {
		String varname = provider.expand(macro, variable, false);
		if (Variable.couldBeInt(variable)) {
			provider.setVariable(macro, variable, (int)value);
		} else {
			provider.setVariable(macro, variable, Float.toString((float)value));
		}
	}
	
	private float getValue(IScriptActionProvider provider, IMacro macro, String param) {
		String expanded = provider.expand(macro, param, false);
		if(expanded.matches("-?\\d+")) {
			return Long.parseLong(expanded) + 0.5f;
		}
		return (float)Arrays.stream(expanded.split("\\+")).mapToDouble(this::parse).sum();
	}
	
	private double parse(String s) {
		s = s.trim();
		if (s.matches("-?\\d+")) {
			return Long.parseLong(s);
		} else if(s.matches("-?\\d+\\.\\d+")) {
			return Double.parseDouble(s);
		}
		return 0;
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
