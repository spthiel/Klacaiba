package me.spthiel.klacaiba.actions;

import net.eq2online.macros.scripting.actions.lang.ScriptActionElseIf;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IMacroActionStackEntry;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptContext;

import javax.annotation.Nonnull;

public abstract class ElseIfBase extends ScriptActionElseIf implements IDocumentable {
	
	protected enum Combiners {
		
		CONTAINS("contains the"),
		BEGINS("starts with"),
		ENDS("ends with");
		
		private final String descriptionCombiner;
		private Combiners(String descriptionCombiner) {
			this.descriptionCombiner = descriptionCombiner;
		}
		
		@Override
		public String toString() {
			
			return descriptionCombiner;
		}
	}
	
	private final String descriptionCombiner;
	
	public ElseIfBase(String actionName, String descriptionCombiner) {
		super(ScriptContext.MAIN, actionName);
		this.descriptionCombiner = descriptionCombiner;
	}
	
	public ElseIfBase(String actionName, Combiners descriptionCombiner) {
		super(ScriptContext.MAIN, actionName);
		this.descriptionCombiner = descriptionCombiner.toString();
	}
	
	public void executeConditionalElse(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params, IMacroActionStackEntry top) {
		if (top.getIfFlag()) {
			top.setConditionalFlag(false);
		} else if (params.length > 1) {
			String haystack = provider.expand(macro, params[0], false);
			String needle = provider.expand(macro, params[1], false);
			top.setConditionalFlag(check(haystack, needle));
			top.setIfFlag(top.getIfFlag() | top.getConditionalFlag());
		} else {
			top.setConditionalFlag(!top.getConditionalFlag());
			top.setElseFlag(true);
		}
	}
	
	protected abstract boolean check(String haystack, String needle);
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return getName() + "(<haystack>,<needle>)";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "The actions following this action will only be executed when the <haystack> " + descriptionCombiner + " <needle>.";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "";
	}
}
