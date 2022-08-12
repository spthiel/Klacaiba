package me.spthiel.klacaiba.config.gui;

import net.eq2online.macros.gui.GuiControl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.LinkedList;
import java.util.List;

public class KlacaibaGuiButton extends GuiControl implements IClickable<KlacaibaGuiButton> {
	
	private final List<Runnable> callbacks           = new LinkedList<>();
	private       GuiScreen      parent;
	private       Alignment      verticalAlignment;
	private       Alignment      horizontalAlignment;
	private       int            dX;
	private       int            dY;
	
	public KlacaibaGuiButton(int width, int height, String displayText, GuiScreen parent) {
		
		this(Alignment.START, width, height, displayText, parent);
	}
	
	public KlacaibaGuiButton(Alignment verticalAlignment, int width, int height, String displayText, GuiScreen parent) {
		
		this(verticalAlignment, Alignment.START, width, height, displayText, parent);
		
	}
	
	public KlacaibaGuiButton(Alignment verticalAlignment, Alignment horizontalAlignment, int width, int height, String displayText, GuiScreen parent) {
		
		this(verticalAlignment, horizontalAlignment, 0, 0, width, height, displayText, parent);
		
	}
	
	public KlacaibaGuiButton(int xPosition, int yPosition, int controlWidth, int controlHeight, String displayText, GuiScreen parent) {
		
		this(Alignment.START, xPosition, yPosition, controlWidth, controlHeight, displayText, parent);
		
	}
	
	public KlacaibaGuiButton(Alignment verticalAlignment, int xPosition, int yPosition, int controlWidth, int controlHeight, String displayText, GuiScreen parent) {
		
		this(verticalAlignment, Alignment.START, xPosition, yPosition, controlWidth, controlHeight, displayText, parent);
		
	}
	
	public KlacaibaGuiButton(Alignment verticalAlignment, Alignment horizontalAlignment, int xPosition, int yPosition, int controlWidth, int controlHeight, String displayText, GuiScreen parent) {
		
		super(0, xPosition, yPosition, controlWidth, controlHeight, displayText);
		
		this.verticalAlignment = verticalAlignment;
		this.horizontalAlignment = horizontalAlignment;
		this.parent = parent;
		this.dX = xPosition;
		this.dY = yPosition;
	}
	
	public void setBounds(int x, int y, int width, int height) {
		
		this.dX = x;
		this.dY = y;
		this.setWidth(width);
		this.height = height;
	}
	
	@Override
	public GuiControl setPosition(int newXPosition, int newYPosition) {
		
		this.dX = newXPosition;
		this.dY = newYPosition;
		return this;
	}
	
	public void setAlignment(Alignment verticalAlignment, Alignment horizontalAlignment) {
		this.verticalAlignment = verticalAlignment;
		this.horizontalAlignment = horizontalAlignment;
	}
	
	@Override
	public void drawButton(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
		
		this.x = verticalAlignment.calculatePosition(dX, this.width, parent.width);
		this.y = horizontalAlignment.calculatePosition(dY, this.height, parent.height);
		super.drawButton(minecraft, mouseX, mouseY, partialTicks);
		
	}
	
	@Override
	public void click() {
		
		callbacks.forEach(Runnable :: run);
	}
	
	@Override
	public KlacaibaGuiButton onClick(Runnable callback) {
		
		callbacks.add(callback);
		return this;
	}
}
