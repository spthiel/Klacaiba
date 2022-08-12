package me.spthiel.klacaiba.module.actions.information;

import net.eq2online.macros.scripting.api.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

import javax.annotation.Nonnull;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import me.spthiel.klacaiba.base.BaseScriptAction;

public class FormatNumber extends BaseScriptAction {
	
	public FormatNumber() {
		
		super("formatnumber");
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "&number = formatnumber(<number>,<format>)";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Format the input number along the given format. Check https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html for more info on how to create a format. Example formats: #,###,##0 or 0,000,000";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Formatted number or false in case of not enough params";
	}
	
	@Override
	public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		if (params.length < 2) {
			return new ReturnValue(false);
		}
		
		int number = getIntOrDefault(provider, macro, params[0], 0);
		String format = provider.expand(macro, params[1], false);
		
		NumberFormat f = new DecimalFormat(format);
		
		return new ReturnValue(f.format(number));
	}
}
