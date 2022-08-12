package me.spthiel.klacaiba.config.configOptions;

import net.eq2online.macros.compatibility.I18n;
import net.eq2online.macros.gui.controls.GuiCheckBox;
import net.minecraft.client.Minecraft;

public class Checkbox extends OptionGroup<Boolean> {
	
	private final GuiCheckBox checkbox;
	
	public Checkbox(int xPosition, int groupHeight, String displayText, boolean checked) {
		super(groupHeight);
		this.checkbox = new GuiCheckBox(Minecraft.getMinecraft(), 1, xPosition, 0, displayText, checked);
	}
	
	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (this.checkbox.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY)) {
			this.execute(this.checkbox.checked);
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public int draw(int mouseX, int mouseY, int yPosition) {
		this.checkbox.drawCheckboxAt(Minecraft.getMinecraft(), mouseX, mouseY, yPosition, 0);
		return super.draw(mouseX, mouseY, yPosition);
	}
	
}
