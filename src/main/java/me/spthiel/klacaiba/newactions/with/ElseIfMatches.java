package me.spthiel.klacaiba.newactions.with;

import net.eq2online.macros.scripting.actions.lang.ScriptActionElse;
import net.eq2online.macros.scripting.actions.lang.ScriptActionElseIf;
import net.eq2online.macros.scripting.actions.lang.ScriptActionIf;
import net.eq2online.macros.scripting.actions.lang.ScriptActionIfMatches;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.ScriptCore;

import javax.annotation.Nonnull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import me.spthiel.klacaiba.actions.BaseScriptAction;
import me.spthiel.klacaiba.actions.IDocumentable;

public class ElseIfMatches extends ScriptActionElseIf implements IDocumentable {
	
	public ElseIfMatches() {
		super(ScriptContext.MAIN, "elseifmatches");
	}
	
	public void executeConditionalElse(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params, IMacroActionStackEntry top) {
		if (top.getIfFlag()) {
			top.setConditionalFlag(false);
		} else if (params.length > 0) {
			String subject = provider.expand(macro, params[0], false);
			String pattern = provider.expand(macro, params[1], false);
			
			boolean value = false;
			
			try {
				Pattern regex   = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
				Matcher matcher = regex.matcher(subject);
				if (matcher.find()) {
					if (params.length > 2) {
						int grpVar = params.length > 3 ? ScriptCore.tryParseInt(provider.expand(macro, params[3], false), 0) : 0;
						int groupNumber = Math.min(Math.max(grpVar, 0), matcher.groupCount());
						provider.setVariable(macro, params[2], matcher.group(groupNumber));
					}
					
					value = true;
				}
			} catch (PatternSyntaxException var12) {
				this.displayErrorMessage(provider, macro, instance, var12, "script.error.badregex");
			} catch (IllegalArgumentException ignored) {
			}
			top.setConditionalFlag(value);
			top.setIfFlag(top.getIfFlag() | top.getConditionalFlag());
		} else {
			top.setConditionalFlag(!top.getConditionalFlag());
			top.setElseFlag(true);
		}
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "ELSEIFMATCHES(<subject>,<pattern>,[&target],[group])";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "The actions following this action will only be executed when the <subject> matches the <pattern>.\n\nOptionally the whole match (or only a group specified by [group]) can be saved into [&target].";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "";
	}
}
