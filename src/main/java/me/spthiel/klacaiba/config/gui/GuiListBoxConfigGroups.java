package me.spthiel.klacaiba.config.gui;

import net.eq2online.macros.compatibility.I18n;
import net.eq2online.macros.core.Macros;
import net.eq2online.macros.gui.controls.GuiDropDownMenu;
import net.eq2online.macros.gui.controls.GuiListBox;
import net.eq2online.macros.gui.list.ListEntry;
import net.eq2online.macros.res.ResourceLocations;
import net.minecraft.client.Minecraft;

import java.util.LinkedList;
import java.util.List;

import me.spthiel.klacaiba.config.ConfigGroups;

public class GuiListBoxConfigGroups extends GuiListBox<ConfigGroups> implements IClickable<GuiListBoxConfigGroups> {
	
	private final List<Runnable> callbacks = new LinkedList<>();
	
	public GuiListBoxConfigGroups(Minecraft minecraft, int controlId, int controlWidth, int controlHeight) {
		this(minecraft, controlId, 202, 21, controlWidth, controlHeight);
	}
	
	public GuiListBoxConfigGroups(Minecraft minecraft, int controlId, int xPos, int yPos, int controlWidth, int controlHeight) {
		super(minecraft, controlId, xPos, yPos, controlWidth, controlHeight, 16, true, false, false);
		this.iconSpacing = 18;
		int id = 0;
		for (ConfigGroups configGroup : ConfigGroups.values()) {
			this.addItem(new ListEntry<>(id++, configGroup.getName(), configGroup));
		}
	}
	
	@Override
	public void click() {
		this.callbacks.forEach(Runnable::run);
	}
	
	@Override
	public GuiListBoxConfigGroups onClick(Runnable callback) {
		this.callbacks.add(callback);
		return this;
	}
}
