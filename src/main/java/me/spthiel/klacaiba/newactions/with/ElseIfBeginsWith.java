package me.spthiel.klacaiba.newactions.with;

import net.eq2online.macros.scripting.actions.lang.ScriptActionElseIf;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IMacroActionStackEntry;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptContext;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.actions.ElseIfBase;
import me.spthiel.klacaiba.actions.IDocumentable;

public class ElseIfBeginsWith extends ElseIfBase {
	
	public ElseIfBeginsWith() {
		super("elseifbeginswith", Combiners.BEGINS);
	}
	
	@Override
	protected boolean check(String haystack, String needle) {
		
		return haystack.startsWith(needle);
	}
}