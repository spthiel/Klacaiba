package me.spthiel.klacaiba.config.configOptions;

import net.eq2online.macros.gui.controls.GuiTextFieldEx;

public class TextField extends InputField<String> {
	
	public TextField(int xPosition, int width, int height, int displayHeight, String value, String label) {
		super(xPosition, width, height, displayHeight, value, label,null, 65536, 0xffffff);
	}
	
	public String getValue() {
		return this.textField.getText();
	}
}
