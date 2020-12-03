package me.spthiel.klacaiba.newactions.with;

import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;

public class GetIdRel extends GetId {
	
	public GetIdRel() {
		super("getidrel");
	}
	
	@Override
	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		if(params.length > 3) {
			params = params.clone();
			params[0] = "~" + params[0];
			params[1] = "~" + params[1];
			params[2] = "~" + params[2];
		}
		return super.execute(provider, macro, instance, rawParams, params);
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "GETIDREL(<x>,<y>,<z>,[&idvar],[#datavar],[&variants])";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Gets the ID and optionally the data value and variants of the block at the specified coordinates relative to the player.";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Returns the name of the block.";
	}
}
