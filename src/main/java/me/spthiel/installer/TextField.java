package me.spthiel.installer;

import javax.swing.*;

public class TextField extends JTextField {
	
	public TextField() {
		super();
		init();
	}
	
	public TextField(String text) {
		super(text);
		init();
	}
	
	private void init() {
		setFocusable(false);
		setEditable(false);
		setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
	}
	
}
