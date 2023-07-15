package me.spthiel.klacaiba.config.gui;

import net.eq2online.macros.compatibility.I18n;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import me.spthiel.klacaiba.Klacaiba;
import me.spthiel.klacaiba.base.managers.ConfigurationManager;
import me.spthiel.klacaiba.config.ConfigGroups;
import me.spthiel.klacaiba.config.configOptions.*;

public class GuiKlacaibaConfigPanel {
	
	private       int                                         width;
	private       int                                         height;
	private ConfigurationManager configurationManager = Klacaiba.getKlacaiba()
																.getConfigurationManager();
	
	private ConfigGroups selected = configurationManager.getSelected();
	private final HashMap<ConfigGroups, List<OptionGroup<?>>> configElements = configurationManager.getConfigElements();
	
	public GuiKlacaibaConfigPanel() {
		configurationManager.registerConfigPanel(this);
	}
	
	public void select(ConfigGroups configGroup) {
		
		this.selected = configGroup;
	}
	
	public int getContentHeight() {
		
		return this.configElements.get(selected)
								  .stream()
								  .mapToInt(OptionGroup :: getDisplayHeight)
								  .sum();
	}
	
	public void onPanelShown(GuiKlacaibaConfig host) {
		
		this.width = host.width;
	}
	
	public void onPanelResize(GuiKlacaibaConfig host) {
		
		this.width = host.width;
	}
	
	public void onPanelHidden() {
	
	}
	
	public void onTick(GuiKlacaibaConfig host) {
	
	}
	
	public void drawPanel(GuiKlacaibaConfig host, int mouseX, int mouseY, float partialTicks) {
		
		AtomicInteger y = new AtomicInteger(4);
		this.configElements.get(selected)
						   .forEach(element -> element.draw(mouseX, mouseY, y.getAndAdd(element.getDisplayHeight())));
		this.configElements.get(selected)
						   .forEach(element -> element.postRender(mouseX, mouseY));
	}
	
	public void mousePressed(GuiKlacaibaConfig host, int mouseX, int mouseY, int mouseButton) {
		
		for (OptionGroup<?> element : this.configElements.get(selected)) {
			if (element.mouseClicked(mouseX, mouseY, mouseButton)) {
				break;
			}
		}
	}
	
	public void mouseReleased(GuiKlacaibaConfig host, int mouseX, int mouseY, int mouseButton) {
	
	}
	
	public void mouseMoved(GuiKlacaibaConfig host, int mouseX, int mouseY) {
	
	}
	
	public void keyPressed(GuiKlacaibaConfig host, char keyChar, int keyCode) {
		
		this.handleKeyPress(host, keyChar, keyCode);
	}
	
	public boolean handleKeyPress(GuiKlacaibaConfig host, char keyChar, int keyCode) {
		//		if (keyCode == 1 && this.parentScreen == null) {
		//			this.saveChanges = false;
		//			host.close();
		//		}
		
		//		if (keyCode == 15) {
		//			this.selectNextField();
		//			return true;
		//		} else {
		
		return this.configElements.get(selected)
								  .stream()
								  .filter(InputField.class :: isInstance)
								  .map(InputField.class :: cast)
								  .anyMatch(inputField -> inputField.keyTyped(keyChar, keyCode));
		//		}
	}
	
	public int getContainerWidth() {
		
		return 0;
	}
	
	public int getContainerHeight() {
		
		return this.getContentHeight() - 32;
	}
}
