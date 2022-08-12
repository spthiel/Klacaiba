package me.spthiel.klacaiba.config.configOptions;

import net.eq2online.macros.gui.GuiControl;
import net.minecraft.client.Minecraft;

public class Button extends OptionGroup<Object> {
	private final GuiControl button;
	
	public Button(int xPosition, int width, int height, int displayHeight, String text) {
		super(displayHeight);
		this.button = new GuiControl(1, xPosition, 0, width, height, text);
	}
	
	public GuiControl getButton() {
		return this.button;
	}
	
	@Override
	public int draw(int mouseX, int mouseY, int yPosition) {
		this.button.setYPosition(yPosition);
		this.button.drawButton(this.minecraft, mouseX, mouseY, 0);
		return super.draw(mouseX, mouseY, yPosition);
	}
	
	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (this.button.mousePressed(this.minecraft, mouseX, mouseY)) {
			this.execute(null);
		}
		
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}
