package me.spthiel.klacaiba.config.gui;

import com.mumfrey.liteloader.gl.GL;
import com.mumfrey.liteloader.gl.GLClippingPlanes;
import net.eq2online.macros.compatibility.I18n;
import net.eq2online.macros.core.Macros;
import net.eq2online.macros.gui.GuiControl;
import net.eq2online.macros.gui.GuiScreenEx;
import net.eq2online.macros.gui.controls.GuiCheckBox;
import net.eq2online.macros.gui.controls.GuiScrollBar;
import net.eq2online.macros.gui.layout.PanelManager;
import net.eq2online.macros.input.InputHandler;
import net.eq2online.macros.interfaces.IListEntry;
import net.eq2online.macros.res.ResourceLocations;
import net.eq2online.macros.scripting.ModuleLoader;
import net.eq2online.macros.scripting.actions.game.ScriptActionClearCrafting;
import net.eq2online.macros.scripting.actions.game.ScriptActionCraft;
import net.eq2online.macros.scripting.api.IScriptAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import javax.annotation.Nonnull;

import java.io.IOException;

import me.spthiel.klacaiba.config.ConfigGroups;
import me.spthiel.klacaiba.config.ConfigManager;

public class GuiKlacaibaConfig extends GuiScreenEx {
	
	private final Macros                 macros;
	private final GuiScreenEx            parentScreen;
	private final GuiKlacaibaConfigPanel configPanel;
	private final int                    sneakCode;
	private final GuiScrollBar           scrollBar     = new GuiScrollBar(this.mc, 0, this.width - 24, 40, 20, this.height - 70, 0, 1000, GuiScrollBar.ScrollBarOrientation.VERTICAL);
	private final GuiListBoxConfigGroups configGroups  = new GuiListBoxConfigGroups(this.mc, 1, 4, 40, 174, this.height - 94);
	private final KlacaibaGuiButton      buttonConfirm = new KlacaibaGuiButton(Alignment.END, Alignment.END, -4, -4,60, 20, I18n.get("gui.ok"), this);
	private final KlacaibaGuiButton      buttonExit    = new KlacaibaGuiButton(Alignment.END, Alignment.END,-68, -4,60, 20, I18n.get("gui.cancel"), this);
	
	private int suspendInput = 4;
	private int lastMouseX;
	private int lastMouseY;
	
	public GuiKlacaibaConfig(Macros macros, Minecraft minecraft, GuiScreenEx parentScreen) {
		
		super(minecraft);
		this.zLevel = 999.0F;
		this.macros = macros;
		this.parentScreen = parentScreen;
		this.configPanel = new GuiKlacaibaConfigPanel();
		this.sneakCode = this.macros.getInputHandler()
									.getSneakKeyCode();
		
		this.configGroups.onClick(() -> {
			configPanel.select(configGroups.getSelectedItem().getData());
		});
		
		initListeners();
	}
	
	public void initListeners() {
		
		this.buttonConfirm.onClick(this :: submit);
		
		this.buttonExit.onClick(() -> this.mc.displayGuiScreen(this.parentScreen));
	}
	
	protected void submit() {
		this.configPanel.onPanelHidden();
		this.mc.displayGuiScreen(this.parentScreen);
	}
	
	@Override
	public void initGui() {
		
		this.suspendInput = 4;
		
		this.configGroups.setSize(174, this.height - 74);
		this.scrollBar.setSizeAndPosition(this.width - 24, 40, 20, this.height - 70);
		this.addControl(this.configGroups);
		this.addControl(this.scrollBar);
		this.addControl(this.buttonExit);
		this.configPanel.onPanelResize(this);
		this.scrollBar.setMax(Math.max(0, this.configPanel.getContentHeight() - (this.height - 70)));
		this.addControl(this.buttonConfirm);
		super.initGui();
	}
	
	@Override
	public void updateScreen() {
		
		if (this.suspendInput > 0) {
			--this.suspendInput;
		}
		
		this.configPanel.onTick(this);
		super.updateScreen();
	}
	
	@Override
	protected void actionPerformed(@Nonnull GuiButton guiButton) {
		
		if (guiButton instanceof IClickable) {
			((IClickable) guiButton).click();
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float f) {
		
		this.lastMouseX = mouseX;
		this.lastMouseY = mouseY;
		if (this.mc.world == null) {
			this.drawDefaultBackground();
			GL.glClear(256);
		}
		
		this.drawScreenWithEnabledState(mouseX, mouseY, f, true);
	}
	
	@Override
	public void drawScreenWithEnabledState(int mouseX, int mouseY, float partialTicks, boolean enabled) {
		
		if (enabled) {
			this.drawBackground();
		}
		
		this.renderer.drawStringWithEllipsis(this.configGroups.getSelectedItem()
															  .getText(), 186, 7, this.width - 210, 4259648);
		this.drawCenteredString(this.fontRenderer, "Klacaiba Settings", 90, 7, 16776960);
		this.renderer.drawTexturedModalRect(ResourceLocations.MAIN, this.width - 17, 5, this.width - 5, 17, 104, 104, 128, 128);
		this.drawString(this.fontRenderer, I18n.get("options.selectconfig"), 8, 26, 16776960);
		this.drawString(this.fontRenderer, I18n.get("options.option.title"), 188, 26, 16776960);
		this.buttonConfirm.drawButton(this.mc, mouseX, mouseY, partialTicks);
		this.buttonExit.drawButton(this.mc, mouseX, mouseY, partialTicks);
		this.scrollBar.drawButton(this.mc, mouseX, mouseY, partialTicks);
		this.configGroups.drawButton(this.mc, mouseX, mouseY, partialTicks);
		GLClippingPlanes.glEnableVerticalClipping(40, this.height - 30);
		int yPos = this.getScrollPos();
		GL.glPushMatrix();
		GL.glTranslatef(182.0F, (float) yPos, 0.0F);
		this.configPanel.drawPanel(this, mouseX - 182, mouseY - yPos, partialTicks);
		GL.glPopMatrix();
		GLClippingPlanes.glDisableClipping();
		if (!enabled) {
			this.drawBackground();
		}
		
	}
	
	private int getScrollPos() {
		
		return 44 - this.scrollBar.getValue();
	}
	
	private void drawBackground() {
		
		int bgCol1 = this.mc.world != null ? -1342177280 : -1728053248;
		int bgCol2 = -1607257293;
		drawRect(2, 2, 180, 20, bgCol1);
		drawRect(182, 2, this.width - 22, 20, bgCol1);
		drawRect(this.width - 20, 2, this.width - 2, 20, bgCol1);
		drawRect(2, 22, 180, 38, bgCol2);
		drawRect(2, 38, 180, this.height - 28, bgCol1);
		drawRect(182, 22, this.width - 2, 38, bgCol2);
		drawRect(182, 38, this.width - 2, this.height - 28, bgCol1);
		drawRect(2, this.height - 26, this.width - 2, this.height - 2, bgCol1);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
		if (this.suspendInput <= 0) {
			if (mouseX > 182 && mouseY > 40 && mouseY < this.height - 40) {
				this.configPanel.mousePressed(this, mouseX - 182, mouseY - this.getScrollPos(), button);
			}
			
			if (mouseX > this.width - 20 && mouseY < 20) {
				this.actionPerformed(this.buttonExit);
			}
			
			super.mouseClicked(mouseX, mouseY, button);
		}
	}
	
	@Override
	protected void keyTyped(char keyChar, int keyCode) {
		if (keyCode == 1) {
			this.mc.displayGuiScreen(this.parentScreen);
		} else if (keyCode == 28 || keyCode == 156 || keyCode == InputHandler.KEY_ACTIVATE.getKeyCode() && InputHandler.isKeyDown(this.sneakCode)) {
			this.submit();
		} else if (!this.configPanel.handleKeyPress(this, keyChar, keyCode)) {
			if (keyCode == 200) {
				this.configGroups.up();
				this.actionPerformed(this.configGroups);
			} else if (keyCode == 208) {
				this.configGroups.down();
				this.actionPerformed(this.configGroups);
			}
			
		}
	}
}
