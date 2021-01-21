package me.spthiel.klacaiba.vars;

import net.eq2online.macros.core.Macros;
import net.eq2online.macros.core.bridge.EntityUtil;
import net.eq2online.macros.core.mixin.IRenderGlobal;
import net.eq2online.macros.gui.designable.DesignableGuiLayout;
import net.eq2online.macros.gui.helpers.SlotHelper;
import net.eq2online.macros.gui.screens.GuiCustomGui;
import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.variable.BlockPropertyTracker;
import net.eq2online.macros.scripting.variable.VariableCache;
import net.eq2online.macros.scripting.variable.providers.VariableProviderPlayer;
import net.eq2online.util.Game;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import me.spthiel.klacaiba.ModuleInfo;
import me.spthiel.klacaiba.actions.ScriptActionHack;
import me.spthiel.klacaiba.utils.HackObject;

@APIVersion(ModuleInfo.API_VERSION)
public class VariableProviderHit extends VariableCache {
	
	@Override
	public void updateVariables(boolean clock) {
		
		if (!clock) {
			return;
		}
		
		Minecraft mc = Minecraft.getMinecraft();
		RayTraceResult objectHit   = mc.objectMouseOver;
		int            blockDamage = 0;
		boolean clearSign = true;
		if (objectHit != null && objectHit.typeOfHit == RayTraceResult.Type.ENTITY && objectHit.entityHit != null) {
			BlockPos entityPosition = objectHit.entityHit.getPosition();
			
			this.storeVariable("EHITX", entityPosition.getX());
			this.storeVariable("EHITY", entityPosition.getY());
			this.storeVariable("EHITZ", entityPosition.getZ());
		} else {
			this.storeVariable("EHITX", 0);
			this.storeVariable("EHITY", 0);
			this.storeVariable("EHITZ", 0);
		}
	}
	
	@Override
	public void onInit() {
		
		ScriptContext.MAIN.getCore().registerVariableProvider(this);
	}
	
	@Override
	public Object getVariable(String variableName) {
		return this.getCachedValue(variableName);
	}
}
