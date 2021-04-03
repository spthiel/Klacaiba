package me.spthiel.klacaiba.module.events;

import net.minecraft.world.World;

public interface IWorldChangeListener {
	
	void onWorldChange(World lastWorld, World newWorld);
	
}
