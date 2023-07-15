package me.spthiel.klacaiba.module.actions.base;

import net.eq2online.macros.scripting.api.IScriptAction;
import net.eq2online.macros.scripting.parser.ScriptAction;

import java.util.function.Consumer;

public interface IMultipleScriptAction {
	
	void registerAdditionalActions(Consumer<IScriptAction> actionConsumer);
	
}
