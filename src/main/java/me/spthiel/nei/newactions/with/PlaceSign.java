package me.spthiel.nei.newactions.with;

import net.eq2online.console.Log;
import net.eq2online.macros.compatibility.I18n;
import net.eq2online.macros.core.Macros;
import net.eq2online.macros.core.mixin.IGuiEditSign;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;

import me.spthiel.nei.actions.BaseScriptAction;
import me.spthiel.nei.actions.IDocumentable;

public class PlaceSign extends BaseScriptAction {
	private boolean handlePlacingSign = false;
	private boolean closeGui = true;
	private int elapsedTicks = 0;
	private String[] signText = new String[4];

	public PlaceSign() {
		super("placesign");
	}

	public boolean isThreadSafe() {
		return false;
	}

	public boolean isPermissable() {
		return true;
	}

	public String getPermissionGroup() {
		return "world";
	}

	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		EntityPlayerSP thePlayer = this.mc.player;
		int signSlotID = -1;
		if (thePlayer != null && thePlayer.inventory != null) {
			InventoryPlayer inventory = thePlayer.inventory;

			for(int i = 0; i < 9; ++i) {
				if (inventory.mainInventory.get(i).getItem() == Items.SIGN) {
					signSlotID = i;
					break;
				}
			}

			if (signSlotID > -1) {
				this.elapsedTicks = 0;
				this.handlePlacingSign = true;
				ItemStack itemstack = inventory.mainInventory.get(signSlotID);
				provider.actionUseItem(this.mc, thePlayer, itemstack, signSlotID);
				this.signText[0] = params.length > 0 ? Macros.replaceInvalidChars(provider.expand(macro, params[0], false)) : "";
				this.signText[1] = params.length > 1 ? Macros.replaceInvalidChars(provider.expand(macro, params[1], false)) : "";
				this.signText[2] = params.length > 2 ? Macros.replaceInvalidChars(provider.expand(macro, params[2], false)) : "";
				this.signText[3] = params.length > 3 ? Macros.replaceInvalidChars(provider.expand(macro, params[3], false)) : "";
				this.closeGui = false;
			} else {
				provider.actionAddChatMessage(I18n.get("script.error.nosign"));
			}
		}

		return null;
	}

	public int onTick(IScriptActionProvider provider) {
		if (this.handlePlacingSign) {
			++this.elapsedTicks;
			if (this.elapsedTicks > 200) {
				this.handlePlacingSign = false;
			} else {
				try {
					GuiScreen currentScreen = this.mc.currentScreen;
					if ((this.closeGui || this.elapsedTicks > 10) && currentScreen instanceof IGuiEditSign) {
						this.handlePlacingSign = false;
						TileEntitySign entitySign = ((IGuiEditSign)currentScreen).getSign();
						if (entitySign != null) {
							for(int i = 0; i < 4; ++i) {
								if (this.signText[i].length() > 15) {
									this.signText[i] = this.signText[i].substring(0, 14);
								}

								entitySign.signText[i] = new TextComponentString(this.signText[i]);
							}

							if (this.closeGui) {
								entitySign.markDirty();
								this.mc.displayGuiScreen((GuiScreen)null);
							}
						}
					}
				} catch (Exception var5) {
					Log.printStackTrace(var5);
				}
			}
		}

		return 0;
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "placesign([line1],[line2],[line3],[line4])";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Places a sign in the world with the specified text (if you have one)";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "";
	}
}
