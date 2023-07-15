package me.spthiel.klacaiba.base.managers;

import net.eq2online.macros.scripting.api.IScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.ScriptCore;

import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import me.spthiel.klacaiba.module.actions.base.IMultipleScriptAction;
import me.spthiel.klacaiba.module.actions.base.IConfigurable;
import me.spthiel.klacaiba.utils.HackObject;
import me.spthiel.klacaiba.utils.ReflectionUtils;

public class ActionManager {
	
	private static final Logger logger = Logger.getLogger(ActionManager.class.getName() );
	
	public  HashMap<String, IScriptAction> actions;
	public  List<IScriptAction>            actionsList;
	public  Pattern                        pattern;
	private HashMap<String, ReplacementScriptAction> replacementActions;
	
	public ActionManager() {
		ScriptCore core = ScriptContext.MAIN.getCore();
		
		this.actions = ReflectionUtils.getValue(core, "actions");
		this.actionsList = ReflectionUtils.getValue(core, "actionsList");
		this.pattern = ReflectionUtils.getValue(core, "actionRegex");
		this.replacementActions = new HashMap<>();
	}
	
	public void registerAction(IScriptAction scriptAction) {
		
		String name = scriptAction.getName();
		
		if (!(scriptAction instanceof IConfigurable)) {
			this.loadAction(scriptAction);
		}
		if (scriptAction instanceof IMultipleScriptAction) {
			((IMultipleScriptAction) scriptAction).registerAdditionalActions(this :: registerAction);
		}
		this.storeAction(scriptAction);
	}
	
	private void storeAction(IScriptAction scriptAction) {
		
		IScriptAction           previous                = this.actions.get(scriptAction.getName());
		ReplacementScriptAction replacementScriptAction = this.replacementActions.get(scriptAction.getName());
		
		if (replacementScriptAction == null) {
			replacementActions.put(scriptAction.getName(), new ReplacementScriptAction(scriptAction, previous));
		} else {
			replacementScriptAction.oldAction = previous;
			replacementScriptAction.klacaibaAction = scriptAction;
		}
	}
	
	public void loadAction(IScriptAction scriptAction) {
		if (scriptAction == null) {
			return;
		}
		
		if (scriptAction instanceof IMultipleScriptAction) {
			((IMultipleScriptAction) scriptAction).registerAdditionalActions(this :: loadAction);
		}
		
		String name = scriptAction.getName();
		
		if (!this.actions.containsKey(name)) {
			actionsList.add(scriptAction);
			updateScriptActionRegex();
		} else {
			for(int i = 0; i < actionsList.size(); i++) {
				if(actionsList.get(i).getName().equalsIgnoreCase(name)) {
					actionsList.set(i, scriptAction);
					break;
				}
			}
		}
		
		actions.put(name, scriptAction);
	}
	
	public void loadAction(String actionName) {
		this.loadAction(this.replacementActions.get(actionName).klacaibaAction);
	}
	
	public void unloadAction(IScriptAction scriptAction) {
		if (scriptAction == null) {
			return;
		}
		
		if (scriptAction instanceof IMultipleScriptAction) {
			((IMultipleScriptAction) scriptAction).registerAdditionalActions(this :: unloadAction);
		}
		
		String name = scriptAction.getName();
		ReplacementScriptAction replacementScriptAction = this.replacementActions.get(name);
		
		if (replacementScriptAction != null) {
			if (replacementScriptAction.oldAction == null) {
				for(int i = 0; i < actionsList.size(); i++) {
					if(actionsList.get(i).getName().equalsIgnoreCase(name)) {
						actionsList.remove(i);
						break;
					}
				}
				updateScriptActionRegex();
			} else {
				for(int i = 0; i < actionsList.size(); i++) {
					if(actionsList.get(i).getName().equalsIgnoreCase(name)) {
						actionsList.set(i, scriptAction);
						break;
					}
				}
			}
			
			actions.remove(name);
		}
	}
	
	public void unloadAction(String actionName) {
		this.unloadAction(this.replacementActions.get(actionName).klacaibaAction);
	}
	
	private void updateScriptActionRegex()
	{
		StringBuilder actionList = new StringBuilder();
		String separator = "";
		
		TreeSet<String> sortedActionNames = new TreeSet<>();
		for (IScriptAction action : this.actions.values())
		{
			sortedActionNames.add(action.toString());
		}
		
		for (String actionName : sortedActionNames)
		{
			actionList.insert(0, actionName + separator);
			separator = "|";
		}
		
		this.pattern = Pattern.compile("(" + actionList + ")(?![a-zA-Z%])([(;]|)", Pattern.CASE_INSENSITIVE);
	}
	
	private static class ReplacementScriptAction {
		
		private       IScriptAction klacaibaAction;
		private       IScriptAction oldAction;
		
		public ReplacementScriptAction(
			IScriptAction klacaibaAction,
			IScriptAction oldAction
		) {
			
			this.klacaibaAction = klacaibaAction;
			this.oldAction = oldAction;
		}
	}
	
}
