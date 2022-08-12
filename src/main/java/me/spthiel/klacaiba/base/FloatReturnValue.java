package me.spthiel.klacaiba.base;

import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.ReturnValue;

public class FloatReturnValue implements IReturnValue {
	
	private final float value;
	
	public FloatReturnValue(float value) {
		this.value = value;
	}
	
	@Override
	public boolean isVoid() {
		
		return false;
	}
	
	@Override
	public boolean getBoolean() {
		
		return false;
	}
	
	@Override
	public int getInteger() {
		
		return (int)this.value;
	}
	
	@Override
	public String getString() {
		
		return this.value + "";
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
