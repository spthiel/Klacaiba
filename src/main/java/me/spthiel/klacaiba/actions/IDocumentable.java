package me.spthiel.klacaiba.actions;

import javax.annotation.Nonnull;

public interface IDocumentable {
    
    @Nonnull String getUsage();
    @Nonnull String getDescription();
    @Nonnull String getReturnType();
    
}
