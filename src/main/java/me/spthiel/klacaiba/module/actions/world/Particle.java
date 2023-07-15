package me.spthiel.klacaiba.module.actions.world;

import net.eq2online.macros.scripting.api.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.EnumParticleTypes;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import me.spthiel.klacaiba.config.ConfigGroups;
import me.spthiel.klacaiba.module.actions.base.BaseScriptAction;

public class Particle extends BaseScriptAction {
	
	public Particle() {
		
		super("particle");
	}
	
	// particlename, x, y, z, dx, dy, dz, count, mode
	@Override
	public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		EnumParticleTypes particle     = EnumParticleTypes.REDSTONE;
		boolean           colormode    = false;
		EntityPlayerSP    player       = Minecraft.getMinecraft().player;
		double            x            = player.posX, y = player.posY, z = player.posZ, dx = 0, dy = 0, dz = 0;
		int               count        = 1;
		int               currentIndex = 0;
		List<String>      errors       = new LinkedList<>();
		if(params.length > currentIndex) {
			String particlename = provider.expand(macro, params[currentIndex++], false);
			EnumParticleTypes particletype = EnumParticleTypes.getByName(particlename);
			if(particletype != null) {
				particle = particletype;
			} else {
				errors.add("ERROR_PARTICLE_NOT_FOUND: " + particlename);
			}
		} else {
			errors.add("ERROR_MISSING_PARTICLE_ARG");
		}
		
		if(params.length > currentIndex) {
			x = getValue(provider, macro, params[currentIndex++]);
		} else {
			errors.add("ERROR_MISSING_X_ARG");
		}
		
		if(params.length > currentIndex) {
			y = getValue(provider, macro, params[currentIndex++]);
		} else {
			errors.add("ERROR_MISSING_Y_ARG");
		}
		
		if(params.length > currentIndex) {
			z = getValue(provider, macro, params[currentIndex++]);
		} else {
			errors.add("ERROR_MISSING_Z_ARG");
		}
		
		if(params.length > currentIndex) {
			dx = getValue(provider, macro, params[currentIndex++]);
		} else {
			errors.add("ERROR_MISSING_DX_ARG");
		}
		
		if(params.length > currentIndex) {
			dy = getValue(provider, macro, params[currentIndex++]);
		} else {
			errors.add("ERROR_MISSING_DY_ARG");
		}
		
		if(params.length > currentIndex) {
			dz = getValue(provider, macro, params[currentIndex++]);
		} else {
			errors.add("ERROR_MISSING_DZ_ARG");
		}
		
		if(params.length > currentIndex) {
			count = getIntOrDefault(provider, macro, params[currentIndex++], count);
		}
		
		if(params.length > currentIndex) {
			String part = provider.expand(macro, params[currentIndex], false);
			colormode = part.toLowerCase().matches("true|1|t");
		}
		
		if(colormode) {
			dx = dx/255;
			dy = dy/255;
			dz = dz/255;
		}
		
		player.world.spawnParticle(particle, true, x, y, z, dx, dy, dz);
		
		ReturnValueArray out = new ReturnValueArray(false);
		out.putStrings(errors);
		return out;
	}
	
	private float getValue(IScriptActionProvider provider, IMacro macro, String param) {
		String expanded = provider.expand(macro, param, false);
		if(expanded.matches("-?\\d+")) {
			return Long.parseLong(expanded) + 0.5f;
		}
		return (float) Arrays.stream(expanded.split("\\+")).mapToDouble(this::parse).sum();
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
		
		return "[&errors[] =] particle(<particlename>, <x>, <y>, <z>, <dx>, <dy>, <dz>, [count], [mode])";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Spawns particles similar to the vanilla command (consult minecraft wiki or youtube) mode can be set to true to divide dx, dy and dz by 255 for easier colored particles";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Optional errors of the action";
	}
	
	@Override
	public ConfigGroups getGroup() {
		
		return ConfigGroups.WORLD;
	}
}
