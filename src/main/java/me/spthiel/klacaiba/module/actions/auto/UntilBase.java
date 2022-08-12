package me.spthiel.klacaiba.module.actions.auto;

import net.eq2online.macros.scripting.ScriptActionBase;
import net.eq2online.macros.scripting.actions.lang.ScriptActionIf;
import net.eq2online.macros.scripting.actions.lang.ScriptActionUntil;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptContext;

import javax.annotation.Nonnull;

import java.lang.reflect.Field;

import me.spthiel.klacaiba.base.IDocumentable;

public class UntilBase extends ScriptActionUntil implements IDocumentable {
	
	private final ScriptActionIf parent;
	private final IDocumentable parentDocs;
	
	public UntilBase(ScriptActionIf parent) {
		super(ScriptContext.MAIN);
		this.parent = parent;
		this.parentDocs = parent instanceof IDocumentable ? (IDocumentable) parent : null;
		try {
			Field f = ScriptActionBase.class.getDeclaredField("actionName");
			f.setAccessible(true);
			f.set(this, "until" + parent.getName().replaceAll("^if", ""));
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public boolean executeConditional(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		return parent.executeConditional(provider, macro, instance, rawParams, params);
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		if (parentDocs == null) {
			return "Missing";
		}
		return "until" + parentDocs.getUsage();
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
