package me.spthiel.klacaiba.config.gui;

public enum Alignment {

	START(),
	CENTER(),
	END();
	
	public int calculatePosition(int offset, int elementSize, int availableSpace) {
		switch (this) {
			case START:
				return offset;
			case CENTER:
				return offset + availableSpace/2 - elementSize/2;
			case END:
				return offset + availableSpace - elementSize;
		}
		return 0;
	}

}
