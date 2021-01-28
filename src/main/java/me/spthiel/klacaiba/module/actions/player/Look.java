package me.spthiel.klacaiba.module.actions.player;

import net.eq2online.macros.scripting.Direction;
import net.eq2online.macros.scripting.DirectionInterpolator;
import net.eq2online.macros.scripting.FloatInterpolator;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.minecraft.client.entity.EntityPlayerSP;

import java.util.regex.Pattern;

public class Look extends ScriptAction {
	protected FloatInterpolator.InterpolationType interpolationType;
	protected static int activeInterpolatorId = 0;

	public Look() {
		super(ScriptContext.MAIN,"look");
		this.interpolationType = FloatInterpolator.InterpolationType.Linear;
	}

	public boolean isThreadSafe() {
		return false;
	}

	public boolean isClocked() {
		return false;
	}

	public boolean isPermissable() {
		return true;
	}

	public String getPermissionGroup() {
		return "input";
	}

	public boolean canExecuteNow(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		EntityPlayerSP thePlayer = this.mc.player;
		if (params.length > 1 && thePlayer != null) {
			Direction newDirection;
			if (instance.getState() == null) {
				Direction currentDirection = new Direction(thePlayer.rotationYaw, thePlayer.rotationPitch);
				newDirection = this.getDirection(provider, macro, params, currentDirection);
				instance.setState(new DirectionInterpolator(currentDirection, newDirection, this.interpolationType, ++activeInterpolatorId));
			}

			DirectionInterpolator state = instance.getState();
			if (state.getId() >= activeInterpolatorId) {
				newDirection = state.interpolate();
				provider.actionSetEntityDirection(thePlayer, newDirection.yaw, newDirection.pitch);
				return state.isFinished();
			}
		}

		return true;
	}

	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		EntityPlayerSP thePlayer = this.mc.player;
		if (params.length > 0 && thePlayer != null && instance.getState() == null) {
			Direction currentDirection = new Direction(thePlayer.rotationYaw, thePlayer.rotationPitch);
			Direction targetDirection = this.getDirection(provider, macro, params, currentDirection);
			if (!targetDirection.isEmpty()) {
				provider.actionSetEntityDirection(thePlayer, targetDirection.yaw, targetDirection.pitch);
			}
		}

		return null;
	}

	protected Direction getDirection(IScriptActionProvider provider, IMacro macro, String[] params, Direction initialDirection) {
		Direction dir = initialDirection.cloneDirection();
		String[] parsedParams = new String[params.length];

		for(int i = 0; i < params.length; ++i) {
			parsedParams[i] = provider.expand(macro, params[i], false).trim();
		}

		if (parsedParams.length > 0) {
			int delayParam = 1;
			if (parsedParams[0].equalsIgnoreCase("north")) {
				dir.setYawAndPitch(180.0F, 0.0F);
			} else if (parsedParams[0].equalsIgnoreCase("east")) {
				dir.setYawAndPitch(270.0F, 0.0F);
			} else if (parsedParams[0].equalsIgnoreCase("south")) {
				dir.setYawAndPitch(0.0F, 0.0F);
			} else if (parsedParams[0].equalsIgnoreCase("west")) {
				dir.setYawAndPitch(90.0F, 0.0F);
			} else if (parsedParams[0].equalsIgnoreCase("near")) {
				int near = 0;
				if (initialDirection.yaw >= 45.0F && initialDirection.yaw < 135.0F) {
					near = 90;
				}

				if (initialDirection.yaw >= 135.0F && initialDirection.yaw < 225.0F) {
					near = 180;
				}

				if (initialDirection.yaw >= 225.0F && initialDirection.yaw < 315.0F) {
					near = 270;
				}
				System.out.println(initialDirection.yaw + " " + initialDirection.pitch + " " + near);
				dir.setYawAndPitch((float)near, 0.0F);
			} else {
				delayParam = 2;
				float pitchOffset;
				if (Pattern.matches("^([\\+\\-]?)[0-9]+$", parsedParams[0])) {
					pitchOffset = ScriptCore.tryParseFloat(parsedParams[0], 0.0F);
					if (!parsedParams[0].startsWith("+") && !parsedParams[0].startsWith("-")) {
						dir.setYaw(pitchOffset + 180.0F);
					} else {
						dir.setYaw(initialDirection.yaw + pitchOffset);
					}
				}

				if (parsedParams.length > 1 && Pattern.matches("^([\\+\\-]?)[0-9]+$", parsedParams[1])) {
					pitchOffset = ScriptCore.tryParseFloat(parsedParams[1], 0.0F);
					if (!parsedParams[1].startsWith("+") && !parsedParams[1].startsWith("-")) {
						dir.setPitch(pitchOffset);
					} else {
						dir.setPitch(initialDirection.pitch + pitchOffset);
					}
				}
			}

			if (parsedParams.length > delayParam) {
				dir.setDuration((long)(ScriptCore.tryParseFloat(parsedParams[delayParam], 0.0F) * 1000.0F));
			}
		}

		return dir;
	}
}