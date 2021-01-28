package me.spthiel.klacaiba.module.actions.mod;

import net.eq2online.macros.core.MacroTemplate;
import net.eq2online.macros.core.Macros;
import net.eq2online.macros.gui.designable.DesignableGuiControl;
import net.eq2online.macros.gui.designable.DesignableGuiControls;
import net.eq2online.macros.gui.designable.DesignableGuiLayout;
import net.eq2online.macros.gui.designable.LayoutManager;
import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;

import java.awt.*;
import java.util.Collection;
import java.util.stream.Collectors;

import me.spthiel.klacaiba.base.BaseScriptAction;

public class CreateControl extends BaseScriptAction {
	
	
	public CreateControl() {
		
		super("createcontrol");
	}
	
	@Override
	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		if (params.length == 1 && params[0].equalsIgnoreCase("types") || params[0].equalsIgnoreCase("layouts")) {
			
			LayoutManager    manager = getLayoutManager(provider, macro);
			ReturnValueArray out     = new ReturnValueArray(false);
			if (params[0].equalsIgnoreCase("layouts")) {
				out.putStrings(manager.getLayoutNames());
			} else {
				out.putStrings(manager
									   .getControls()
									   .getAvailableControlTypes()
									   .stream()
									   .map(controlType -> controlType.type)
									   .collect(Collectors.toList()));
			}
			return out;
			
		}
		
		if (params.length < 4) {
			return new ReturnValue("Invalid amount of arguments, 4 are required at least");
		}
		
		String layoutName  = provider.expand(macro, params[0], false);
		String elementType = provider.expand(macro, params[1], false);
		int    row         = getIntOrDefault(provider, macro, params[2], -1);
		if (row < 0) {
			return new ReturnValue("Invalid value for row");
		}
		int column = getIntOrDefault(provider, macro, params[3], -1);
		if (column < 0) {
			return new ReturnValue("Invalid value for column");
		}
		
		LayoutManager manager = getLayoutManager(provider, macro);
		if (!manager.layoutExists(layoutName)) {
			return new ReturnValue("Invalid layout name, valid layouts are " + String.join(", ", manager.getLayoutNames()));
		}
		DesignableGuiLayout                           layout = manager.getLayout(layoutName);
		Collection<DesignableGuiControls.ControlType> types  = manager.getControls().getAvailableControlTypes();
		if (types.stream().noneMatch(type -> type.type.equalsIgnoreCase(elementType))) {
			return new ReturnValue("Invalid control type, valid control types are" + types
					.stream()
					.map(controlType -> controlType.type)
					.collect(Collectors.joining(", ")));
		}
		
		if (layout.isCellOccupied(new Point(column, row))) {
			return new ReturnValue("Cell is occupied");
		}
		
		DesignableGuiControl control = layout.addControl(elementType, row, column);
		
		return new ReturnValue(control.getName());
	}
	
	private LayoutManager getLayoutManager(IScriptActionProvider provider, IMacro macro) {
		
		IMacroTemplate template = macro.getTemplate();
		return template instanceof MacroTemplate ? ((MacroTemplate) template)
				.getMacroManager()
				.getLayoutManager() : ((Macros) provider.getMacroEngine()).getLayoutManager();
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "createcontrol(<screenname|\"layouts\"|\"types\">,[element type],[row],[column])";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Creates a control on the specified screen at row and column position. Use createcontrol(\"layouts\") to get all layout names and createcontrol(\"types\") to get all control types as array";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Name of the control (to be used in setproperty etc) or error message. May be used to get layoutnames and controltypes.";
	}
}
