package me.spthiel.klacaiba.module.actions.player;

import net.eq2online.macros.scripting.FloatInterpolator;
import net.eq2online.macros.scripting.actions.game.ScriptActionLook;
import net.eq2online.macros.scripting.parser.ScriptContext;

public class Looks extends ScriptActionLook {

	public Looks() {
		super(ScriptContext.MAIN, "looks");
		this.interpolationType = FloatInterpolator.InterpolationType.Smooth;
	}
}
