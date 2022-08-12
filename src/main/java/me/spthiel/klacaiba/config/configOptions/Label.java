package me.spthiel.klacaiba.config.configOptions;

import net.eq2online.macros.compatibility.I18n;
import net.minecraft.client.Minecraft;

public class Label extends OptionGroup<Object> {
	private final int xPosition;
	private String displayText;
	private final int displayColour;
	
	public Label(String text, int xPosition, int height, int displayColour) {
		super(height);
		this.xPosition = xPosition;
		this.displayText = text;
		this.displayColour = displayColour;
	}
	
	public void setText(String text) {
		this.displayText = text;
	}
	
	@Override
	public int draw(int mouseX, int mouseY, int yPosition) {
		this.minecraft.fontRenderer.drawString(this.displayText, this.xPosition, yPosition, this.displayColour);
		return super.draw(mouseX, mouseY, yPosition);
	}
}
