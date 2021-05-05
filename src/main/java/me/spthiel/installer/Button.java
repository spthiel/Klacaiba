package me.spthiel.installer;

import javax.swing.*;
import java.awt.*;

public class Button extends JButton {
	
	public Button(String text) {
		super(text);
		
		this.setForeground(Color.lightGray);
		this.setFocusPainted(false);
		this.setBorderPainted(false);
		this.setBackground(new Color(0, 0, 0, 0));
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
		g.setColor(Color.darkGray);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		super.paintComponent(g);
	}
}
