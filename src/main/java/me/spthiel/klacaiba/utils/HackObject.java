package me.spthiel.klacaiba.utils;

import net.eq2online.macros.scripting.ModuleLoader;
import net.eq2online.macros.scripting.api.IScriptAction;
import net.eq2online.macros.scripting.api.IScriptedIterator;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.ScriptCore;

import java.lang.reflect.Field;
import java.sql.Ref;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class HackObject {


	public HackObject() {
	}
	
	public IScriptAction getScriptActionFor(String name) {
		return this.actions.get(name);
	}
	
	/**
	 * Overwrite current action in macromod if it exists or add it as new one
	 * @param action Action to add or overwrite with
	 */
	public void addOrPut(IScriptAction action) {
		String name = action.getName();
		if(!actions.containsKey(name)) {
			actionsList.add(action);
			updateScriptActionRegex();
		} else {
			for(int i = 0; i < actionsList.size(); i++) {
				if(actionsList.get(i).getName().equalsIgnoreCase(name)) {
					actionsList.set(i, action);
				}
			}
		}
		actions.put(name,action);
	}
	
	public void addOrPut(String iteratorKey, Class<? extends IScriptedIterator> iterator) {
		iterators.put(iteratorKey, iterator);
	}

}
