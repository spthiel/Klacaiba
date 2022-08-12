package me.spthiel.klacaiba.config.gui;

import net.eq2online.macros.compatibility.I18n;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import me.spthiel.klacaiba.config.configOptions.*;

public class GuiKlacaibaConfigPanel {
	
	@SuppressWarnings("rawtypes")
	private final List<OptionGroup> configElements = new ArrayList<>();
	private       int               width;
	private       int               height;
	
	public GuiKlacaibaConfigPanel() {
		
		this(Minecraft.getMinecraft(), null);
	}
	
	GuiKlacaibaConfigPanel(Minecraft minecraft, GuiKlacaibaConfig parentScreen) {
		
		configElements.add(new Checkbox(4, 18, "Test", false));
		configElements.add(new Checkbox(4, 18, "Test", true));
		configElements.add(new NumberField(4, 80, 16, 20, 0, "Number field", 0, 4));
		configElements.add(new TextField(4, 80, 16, 20, "Test", "Text field"));
	}
	
	public int getContentHeight() {
		
		return this.configElements.stream()
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
		this.configElements.forEach(element -> element.draw(mouseX, mouseY, y.getAndAdd(element.getDisplayHeight())));
		this.configElements.forEach(element -> element.postRender(mouseX, mouseY));
	}
	
	public void mousePressed(GuiKlacaibaConfig host, int mouseX, int mouseY, int mouseButton) {
		
		for (OptionGroup element : this.configElements) {
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
		
		return this.configElements.stream()
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
