package me.spthiel.klacaiba.config.configOptions;

import net.minecraft.client.Minecraft;

import java.util.LinkedList;
import java.util.function.Consumer;

import me.spthiel.klacaiba.config.gui.GuiKlacaibaConfigPanel;

public abstract class OptionGroup<T> {
	
	private final int displayHeight;
	protected     GuiKlacaibaConfigPanel  parent;
	protected final Minecraft minecraft;
	private final LinkedList<Consumer<T>> listeners = new LinkedList<>();
	
	public OptionGroup(int displayHeight) {
		this.displayHeight = displayHeight;
		this.minecraft = Minecraft.getMinecraft();
	}
	
	public void setParent(GuiKlacaibaConfigPanel panel) {
		this.parent = panel;
	}
	
	public int getDisplayHeight() {
		return this.displayHeight;
	}
	
	public void onChange(Consumer<T> listener) {
		listeners.add(listener);
	}
	
	protected void execute(T value) {
		this.listeners.forEach(consumer -> consumer.accept(value));
	}
	
	public int draw(int mouseX, int mouseY, int yPosition) {
		return yPosition + this.displayHeight;
	}
	
	public void postRender(int mouseX, int mouseY) {
	
	}
	
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		return false;
	}
	
	public int getContainerWidth() {
		return this.parent.getContainerWidth();
	}
	
	public int getContainerHeight() {
		return this.parent.getContainerHeight() - 32;
	}
}
