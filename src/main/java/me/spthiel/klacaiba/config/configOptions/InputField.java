package me.spthiel.klacaiba.config.configOptions;

import net.eq2online.macros.gui.controls.GuiTextFieldEx;
import net.minecraft.client.Minecraft;

public abstract class InputField<T> extends OptionGroup<T> {
	protected final GuiTextFieldEx textField;
	protected final Label label;
	
	public InputField(int xPosition, int width, int height, int displayHeight, T value, String label, String allowedCharacters, int maxlength, int displayColor) {
		super(displayHeight);
		this.textField = new GuiTextFieldEx(1, Minecraft.getMinecraft().fontRenderer, xPosition, 0, width, height, value.toString(), allowedCharacters, maxlength);
		this.label = new Label(label, xPosition + width + 10, height, displayColor);
	}
	
	public void setLabelText(String text) {
		this.label.setText(text);
	}
	
	public GuiTextFieldEx getTextField() {
		return this.textField;
	}
	
	public void updateCursorCounter() {
		this.textField.updateCursorCounter();
	}
	
	public int draw(int mouseX, int mouseY, int yPosition) {
		this.textField.drawTextBoxAt(yPosition);
		this.label.draw(mouseX, mouseY, yPosition);
		return super.draw(mouseX, mouseY, yPosition);
	}
	
	public boolean keyTyped(char keyChar, int keyCode) {
		this.textField.textboxKeyTyped(keyChar, keyCode);
		if (this.textField.isFocused()) {
			this.execute(this.getValue());
			return true;
		}
		return false;
	}
	
	public abstract T getValue();
	
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		this.textField.mouseClicked(mouseX, mouseY, mouseButton);
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}