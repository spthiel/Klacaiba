package me.spthiel.klacaiba.base;

import net.eq2online.macros.scripting.api.IReturnValue;

public class ReturnValueDouble implements IReturnValue {
	
	private double value;
	
	public ReturnValueDouble(double value) {
		this.value = value;
	}
	
	public void setValue(float value) {
		
		this.value = value;
	}
	
	@Override
	public boolean isVoid() {
		
		return false;
	}
	
	@Override
	public boolean getBoolean() {
		
		return value != 0;
	}
	
	@Override
	public int getInteger() {
		
		return (int) value;
	}
	
	@Override
	public String getString() {
		
		return value + "";
	}
	
	@Override
	public String getLocalMessage() {
		
		return null;
	}
	
	@Override
	public String getRemoteMessage() {
		
		return null;
	}
}
