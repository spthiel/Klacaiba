package me.spthiel.klacaiba.base.managers;

import net.eq2online.macros.scripting.ScriptedIterator;
import net.eq2online.macros.scripting.api.IScriptedIterator;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.ScriptCore;

import java.util.Map;

import me.spthiel.klacaiba.module.iterators.IteratorPlayers;
import me.spthiel.klacaiba.utils.ReflectionUtils;

public class IteratorManager {
	
	public Map<String, Class<? extends IScriptedIterator>> iterators;
	public IteratorManager() {
		ScriptCore core = ScriptContext.MAIN.getCore();
		
		this.iterators = ReflectionUtils.getValue(core, "iterators");
	}
	
	public void registerIterator(String iteratorName, Class<? extends ScriptedIterator> iteratorClass) {
	
		this.iterators.put(iteratorName, iteratorClass);
	}
}
