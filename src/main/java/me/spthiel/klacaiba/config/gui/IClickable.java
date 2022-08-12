package me.spthiel.klacaiba.config.gui;

public interface IClickable<T extends IClickable> {
	public void click();
	public T onClick(Runnable callback);
}
