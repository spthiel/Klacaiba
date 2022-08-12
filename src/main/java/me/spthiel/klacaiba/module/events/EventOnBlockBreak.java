package me.spthiel.klacaiba.module.events;

import net.eq2online.macros.scripting.api.*;
import net.eq2online.util.Game;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import me.spthiel.klacaiba.ModuleInfo;
import me.spthiel.klacaiba.base.BaseCustomEvent;
import me.spthiel.klacaiba.module.variableprovider.VariableProviderHack;

public class EventOnBlockBreak extends BaseCustomEvent<EventOnBlockBreak.BreakEventEntry> implements IWorldEventListener, IWorldChangeListener {
	
	private final LinkedList<World> eventListenedWorlds;
	
	public EventOnBlockBreak() {
		this(null);
	}
	
	public EventOnBlockBreak(String[] params) {
		super("blockbreak", "break");
		this.eventListenedWorlds = new LinkedList<>();
		onWorldChange(null, Minecraft.getMinecraft().world);
	}
	
	@Override
	protected void registerVariablesFor(BreakEventEntry entry) {
		
		IBlockState oldState = entry.state;
		BlockPos pos = entry.position;
		int blockMeta = oldState.getBlock().damageDropped(oldState);
		String displayName = oldState.getBlock().getLocalizedName();
		ItemStack item = oldState.getBlock().getItem(Minecraft.getMinecraft().world, pos, oldState);
		try {
			displayName = item.getDisplayName();
		} catch (Exception var15) {
		}
		
		registerVariable("XPOS", pos.getX());
		registerVariable("YPOS", pos.getY());
		registerVariable("ZPOS", pos.getZ());
		registerVariable("ID", Game.getBlockName(oldState.getBlock()));
		registerVariable("DATA", blockMeta);
		registerVariable("NAME", displayName);
		
	}
	
	@Override
	protected void init() {
		
		VariableProviderHack.registerWorldChangeListener(this);
	}
	
	@Override
	public void terminate() {
	
		VariableProviderHack.removeWorldChangeListener(this);
		eventListenedWorlds.forEach(world -> world.removeEventListener(this));
	}
	
	@Override
	public void playEvent(EntityPlayer player, int type, @Nonnull BlockPos pos, int data) {
		
		if (type == 2001) {
			
			populate(new BreakEventEntry(pos, data));
		}
	
	}
	
	@Override
	public void onWorldChange(World lastWorld, World newWorld) {
		if (newWorld != null && !eventListenedWorlds.contains(newWorld)) {
			newWorld.addEventListener(this);
			eventListenedWorlds.add(newWorld);
		}
	}
	
	protected static class BreakEventEntry {
	
		private BlockPos position;
		private IBlockState state;
		
		public BreakEventEntry(BlockPos position, int data) {
			this.position = position;
			this.state = Block.getStateById(data);
		}
	
	}
	
	//region ignored
	
	@Override
	public void notifyBlockUpdate(World worldIn, BlockPos pos, IBlockState oldState, IBlockState newState, int flags) {}
	
	@Override
	public void notifyLightSet(BlockPos pos) { }
	
	@Override
	public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) { }
	
	@Override
	public void playSoundToAllNearExcept(@Nullable EntityPlayer player, SoundEvent soundIn, SoundCategory category, double x, double y, double z, float volume, float pitch) { }
	
	@Override
	public void playRecord(SoundEvent soundIn, BlockPos pos) { }
	
	@Override
	public void spawnParticle(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters) { }
	
	@Override
	public void spawnParticle(int id, boolean ignoreRange, boolean p_190570_3_, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... parameters) { }
	
	@Override
	public void onEntityAdded(Entity entityIn) { }
	
	@Override
	public void onEntityRemoved(Entity entityIn) { }
	
	@Override
	public void broadcastSound(int soundID, BlockPos pos, int data) { }
	
	@Override
	public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) { }
	
	//endregion
}