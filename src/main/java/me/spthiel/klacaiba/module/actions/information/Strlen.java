package me.spthiel.klacaiba.module.actions.information;

import net.eq2online.macros.scripting.api.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.base.BaseScriptAction;

public class Strlen extends BaseScriptAction {
	
	public Strlen() {
		super("strlen");
	}
	
	@Override
	public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		if (params.length == 0) {
			return new ReturnValue(0);
		}
		String str = provider.expand(macro, params[0], false);
		return new ReturnValue(str.length());
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "strlen(<string>)";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Returns the length of the string";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Length of string or 0 if none is present";
	}
}
