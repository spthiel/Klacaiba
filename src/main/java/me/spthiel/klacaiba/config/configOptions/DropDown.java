package me.spthiel.klacaiba.config.configOptions;

import net.eq2online.macros.compatibility.I18n;
import net.eq2online.macros.gui.controls.GuiDropDownList;
import net.eq2online.macros.interfaces.annotations.DropdownLocalisationRoot;
import net.eq2online.macros.interfaces.annotations.DropdownStyle;
import net.minecraft.client.Minecraft;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;

public class DropDown<T> extends OptionGroup<T> {
	private final GuiDropDownList.GuiDropDownListControl dropDown;
	private   Enum<?>                                defaultEnumValue;
	private   int                                    yPosition;
	
	DropDown(int xPosition, int controlWidth, int controlHeight, int itemHeight, int displayHeight, Enum<?> defaultValue, String label) {
		super(displayHeight);
		this.defaultEnumValue = defaultValue;
		this.dropDown = null;
//		this.dropDown = new GuiDropDownList.GuiDropDownListControl(this, Minecraft.getMinecraft(), 1, xPosition, 0, controlWidth, controlHeight, itemHeight, "");
		
		try {
			Class<? extends Enum<?>> enumClass         = defaultValue.getDeclaringClass();
			Method                   values            = enumClass.getDeclaredMethod("values");
			Enum<?>[]                enumValues        = (Enum[])((Enum[])values.invoke((Object)null));
			DropdownLocalisationRoot locRootAnnotation = (DropdownLocalisationRoot)enumClass.getAnnotation(DropdownLocalisationRoot.class);
			String                   localisationRoot  = locRootAnnotation != null ? locRootAnnotation.value() : null;
			if (localisationRoot != null && !localisationRoot.endsWith(".")) {
				localisationRoot = localisationRoot + ".";
			}
			
			Enum[] var15 = enumValues;
			int var16 = enumValues.length;
			
			for(int var17 = 0; var17 < var16; ++var17) {
				Enum<?>       enumValue     = var15[var17];
				DropdownStyle dropDownStyle = (DropdownStyle)enumValue.getClass().getField(enumValue.name()).getAnnotation(DropdownStyle.class);
				if (dropDownStyle == null || !dropDownStyle.hideInDropdown()) {
					String name = enumValue.toString().toLowerCase();
					String text = localisationRoot != null ? I18n.get(localisationRoot + name) : StringUtils.capitalize(name);
					this.dropDown.addItem(enumValue.name(), text);
				}
			}
		} catch (Exception var22) {
			var22.printStackTrace();
		}
		
//		this.dropDown.selectItemByTag(this.parent.getSetting(this.binding).toString());
	}
	
	public int draw(int mouseX, int mouseY, int yPosition) {
		this.yPosition = yPosition;
		return super.draw(mouseX, mouseY, yPosition);
	}
	
	public void postRender(Minecraft minecraft, int mouseX, int mouseY) {
		this.dropDown.drawControlAt(minecraft, mouseX, mouseY, this.yPosition);
	}
	
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		return this.dropDown.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
	}
	
}
