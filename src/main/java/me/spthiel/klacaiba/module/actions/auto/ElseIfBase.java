package me.spthiel.klacaiba.module.actions.auto;

import net.eq2online.macros.scripting.actions.lang.ScriptActionElseIf;
import net.eq2online.macros.scripting.actions.lang.ScriptActionIf;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IMacroActionStackEntry;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptContext;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.base.IDocumentable;

public class ElseIfBase extends ScriptActionElseIf implements IDocumentable {
	
	private final ScriptActionIf parent;
	private final IDocumentable parentDocs;
	
	public ElseIfBase(ScriptActionIf parent) {
		super(ScriptContext.MAIN, "else" + parent.getName());
		this.parent = parent;
		this.parentDocs = parent instanceof IDocumentable ? (IDocumentable) parent : null;
	}
	
	public void executeConditionalElse(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params, IMacroActionStackEntry top) {
		if (top.getIfFlag()) {
			top.setConditionalFlag(false);
		} else {
			boolean parentBool = parent.executeConditional(provider, macro, instance, rawParams, params);
			
			top.setConditionalFlag(parentBool);
			top.setIfFlag(top.getIfFlag() | top.getConditionalFlag());
		}
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		if (parentDocs == null) {
			return "Missing";
		}
		return "else" + parentDocs.getUsage();
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		if (parentDocs == null) {
			return "Check the documentation of " + parent.getName() + " for more elaborate info";
		}
		return parentDocs.getDescription();
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
