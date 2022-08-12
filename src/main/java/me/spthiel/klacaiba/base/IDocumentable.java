package me.spthiel.klacaiba.base;

import javax.annotation.Nonnull;

public interface IDocumentable {
    
    @Nonnull String getUsage();
    @Nonnull String getDescription();
    @Nonnull String getReturnType();
    
}
