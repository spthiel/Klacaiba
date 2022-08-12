package me.spthiel.klacaiba.config.configOptions;

public class NumberField extends InputField<Integer> {
	private final int defaultValue;
	
	public NumberField(int xPosition, int width, int height, int displayHeight, int value, String label, int defaultValue, int maxDigits) {
		super(xPosition, width, height, displayHeight, value, label, "0123456789", maxDigits, 0xffffff);
		this.defaultValue = defaultValue;
	}
	
	@Override
	public Integer getValue() {
		
		return this.textField.getValueInt(defaultValue);
	}
}
