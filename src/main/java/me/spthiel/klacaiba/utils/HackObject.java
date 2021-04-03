package me.spthiel.klacaiba.utils;

import net.eq2online.macros.scripting.api.IScriptAction;
import net.eq2online.macros.scripting.api.IScriptedIterator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class HackObject {

	public HashMap<String, IScriptAction> actions;
	public Map<String, Class<? extends IScriptedIterator>> iterators;
	public List<IScriptAction> actionsList;
	public Pattern pattern;

	public HackObject() {

	}

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

	private void updateScriptActionRegex()
	{
		StringBuilder actionList = new StringBuilder();
		String separator = "";

		TreeSet<String> sortedActionNames = new TreeSet<String>();
		for (IScriptAction action : this.actions.values())
		{
			sortedActionNames.add(action.toString());
		}

		for (String actionName : sortedActionNames)
		{
			actionList.insert(0, actionName + separator);
			separator = "|";
		}

		this.pattern = Pattern.compile("(" + actionList.toString() + ")(?![a-zA-Z%])([(;]|)", Pattern.CASE_INSENSITIVE);
	}

}
