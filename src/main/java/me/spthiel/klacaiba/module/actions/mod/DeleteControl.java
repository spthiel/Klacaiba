package me.spthiel.klacaiba.module.actions.mod;

import net.eq2online.macros.core.MacroTemplate;
import net.eq2online.macros.core.Macros;
import net.eq2online.macros.gui.designable.DesignableGuiLayout;
import net.eq2online.macros.gui.designable.LayoutManager;
import net.eq2online.macros.scripting.api.*;

import me.spthiel.klacaiba.base.BaseScriptAction;

import javax.annotation.Nonnull;
import java.util.Optional;

public class DeleteControl extends BaseScriptAction {
	
	public DeleteControl() {
		
		super("deletecontrol");
	}
	
	@Override
	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		if(params.length < 1) {
			return new ReturnValue("Invalid amount of arguments, require 1 argument at least");
		}
		
		String controlName = provider.expand(macro, params[0], false);
		
		LayoutManager manager = getLayoutManager(provider, macro);
		
		Optional<DesignableGuiLayout> optional = manager.getLayoutNames().stream()
														.map(manager :: getLayout)
														.filter(layout -> layout.getControl(controlName) != null)
														.findFirst();
		if(!optional.isPresent()) {
			return new ReturnValue("Control name not found");
		}
		DesignableGuiLayout layout = optional.get();
		layout.removeControl(layout.getControl(controlName).getId());
		
		return new ReturnValue("");
	}
	
	private LayoutManager getLayoutManager(IScriptActionProvider provider, IMacro macro) {
		IMacroTemplate template = macro.getTemplate();
		return template instanceof MacroTemplate ? ((MacroTemplate)template).getMacroManager().getLayoutManager() : ((Macros)provider.getMacroEngine()).getLayoutManager();
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "deletecontrol(<Controlname>)";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Deletes a control by name from any gui";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Nothing or error message";
	}
}
