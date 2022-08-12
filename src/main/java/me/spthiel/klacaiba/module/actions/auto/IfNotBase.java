package me.spthiel.klacaiba.module.actions.auto;

import net.eq2online.macros.scripting.actions.lang.ScriptActionIf;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IMacroActionStackEntry;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptContext;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.base.IDocumentable;

public class IfNotBase extends ScriptActionIf implements IDocumentable {
	
	private final ScriptActionIf parent;
	private final IDocumentable parentDocs;
	
	public IfNotBase(ScriptActionIf parent) {
		super(ScriptContext.MAIN, "ifnot" + parent.getName().replaceFirst("if", ""));
		this.parent = parent;
		this.parentDocs = parent instanceof IDocumentable ? (IDocumentable) parent : null;
	}
	
	@Override
	public boolean executeConditional(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		return !parent.executeConditional(provider, macro, instance, rawParams, params);
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		if (parentDocs == null) {
			return "Missing";
		}
		return "ifnot" + parentDocs.getUsage().replaceFirst("if", "");
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		if (parentDocs == null) {
			return "Check the documentation of " + parent.getName() + " for more elaborate info";
		}
		return "Opposit of:\n" + parentDocs.getDescription();
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		if (parentDocs == null) {
			return "Missing";
		}
		return parentDocs.getReturnType();
	}
}
