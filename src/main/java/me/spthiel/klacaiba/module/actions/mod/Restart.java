package me.spthiel.klacaiba.module.actions.mod;

import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;

import java.lang.reflect.Field;

import me.spthiel.klacaiba.config.ConfigGroups;
import me.spthiel.klacaiba.module.actions.base.BaseScriptAction;

public class Restart extends BaseScriptAction {
	
	public Restart() {
		
		super("restart");
	}
	
	@Override
	public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		IMacroActionProcessor processor = instance.getActionProcessor();
		
		while (processor.popStack());
		
		try {
			
			Field f = processor.getClass().getDeclaredField("pointer");
			f.setAccessible(true);
			f.set(processor, -1);
			f.setAccessible(false);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return null;
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "restart()";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Restarts the current script from the top without invalidating any variables";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "";
	}
	
	@Override
	public ConfigGroups getGroup() {
		
		return ConfigGroups.MOD;
	}
}
