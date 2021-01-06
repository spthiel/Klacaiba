package me.spthiel.klacaiba.newactions.with;

import net.eq2online.macros.scripting.actions.lang.ScriptActionElseIf;
import net.eq2online.macros.scripting.actions.lang.ScriptActionIfContains;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IMacroActionStackEntry;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.ScriptCore;

import javax.annotation.Nonnull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import me.spthiel.klacaiba.actions.ElseIfBase;
import me.spthiel.klacaiba.actions.IDocumentable;

public class ElseIfContains extends ElseIfBase {
	
	public ElseIfContains() {
		super("elseifcontains", Combiners.CONTAINS);
	}
	
	@Override
	protected boolean check(String haystack, String needle) {
		
		return haystack.contains(needle);
	}
}
