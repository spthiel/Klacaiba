package me.spthiel.installer;

import javax.swing.*;

import me.spthiel.klacaiba.ModuleInfo;

public class Main {

    public static void main(String[] args) {
    
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        new Installer(ModuleInfo.NAME).then((System.out :: println));
    }
}
