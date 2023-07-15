package me.spthiel.klacaiba.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionUtils {
	
	public static void setFinalStatic(Field field, Object newValue) throws Exception {
		
		field.setAccessible(true);
		
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		
		field.set(null, newValue);
	}
	
	public static <T> T getValue(Object object, String fieldName) {
		
		try {
			Field field = object.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return (T)field.get(object);
		} catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
			e.printStackTrace();
			return null;
		}
	}
}
