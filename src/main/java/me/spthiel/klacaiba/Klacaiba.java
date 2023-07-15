package me.spthiel.klacaiba;

import net.eq2online.macros.compatibility.AllowedCharacters;
import net.eq2online.macros.core.Macros;
import net.eq2online.macros.scripting.api.IScriptAction;
import net.minecraft.world.World;

import me.spthiel.klacaiba.base.managers.ConfigurationManager;
import me.spthiel.klacaiba.base.managers.ActionManager;
import me.spthiel.klacaiba.base.managers.IteratorManager;
import me.spthiel.klacaiba.config.ConfigOptionList;
import me.spthiel.klacaiba.config.gui.GuiKlacaibaConfig;
import me.spthiel.klacaiba.module.actions.base.IConfigurable;
import me.spthiel.klacaiba.module.events.base.IWorldChangeListener;
import me.spthiel.klacaiba.module.iterators.*;
import me.spthiel.klacaiba.module.variableprovider.VariableProviderKlacaiba;
import me.spthiel.klacaiba.utils.ReflectionUtils;

public class Klacaiba implements IWorldChangeListener {
	
	private static Klacaiba klacaiba;
	
	public static boolean loaded     = false;
	public static boolean documented = false;
	
	public static Klacaiba getKlacaiba() {
		
		return klacaiba;
	}
	
	private final ConfigurationManager configurationManager;
	private final ActionManager   scriptActionManager;
	private final IteratorManager iteratorManager;
	
	public Klacaiba() {
		
		klacaiba = this;
		this.configurationManager = new ConfigurationManager();
		this.scriptActionManager = new ActionManager();
		this.iteratorManager = new IteratorManager();
		
		Macros.getInstance()
			  .getCustomScreenManager()
			  .registerCustomScreen("Klacaiba Settings", GuiKlacaibaConfig.class);
		
		VariableProviderKlacaiba.registerWorldChangeListener(this);
		
	}
	
	public ConfigurationManager getConfigurationManager() {
		
		return configurationManager;
	}
	
	@Override
	public void onWorldChange(World lastWorld, World newWorld) {
		
		if (loaded) {
			return;
		}
		loaded = true;
		
		addActions();
		addConfiguration();
		addDocumentation();
	}
	
	public void addActions() {
		
		IScriptAction[] actions = Actions.ACTIONS;
		
		for (IScriptAction action : actions) {
			scriptActionManager.registerAction(action);
		}
		
	}
	
	private void addEvents() {
		
		iteratorManager.registerIterator("players", IteratorPlayers.class);
		iteratorManager.registerIterator("teams", IteratorTeams.class);
		iteratorManager.registerIterator("objectives", IteratorObjectives.class);
		iteratorManager.registerIterator("scores", IteratorScores.class);
		iteratorManager.registerIterator("trades", IteratorTrades.class);
		iteratorManager.registerIterator("inventory", IteratorInventory.class);
	}
	
	public void addConfiguration() {
		
		IScriptAction[] actions = Actions.ACTIONS;
		
		for (IScriptAction action : actions) {
			if (action instanceof IConfigurable) {
				IConfigurable    configurable = (IConfigurable) action;
				ConfigOptionList options      = configurable.getOptions();
				if (options != null) {
					options.getOptions()
						   .forEach(option -> configurationManager.addOptionGroup(configurable.getGroup(), option));
				}
			}
		}
	}
	
	public void addDocumentation() {
	
	}
	
	private void addUnicodeString() {
		
		try {
			ReflectionUtils.setFinalStatic(AllowedCharacters.class.getField("CHARACTERS"), buildUnicodeString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String unicodeString = null;
	
	private static String buildUnicodeString() {
		
		if (unicodeString != null) {
			return unicodeString;
		}
		StringBuilder out = new StringBuilder();
		out.append(AllowedCharacters.CHARACTERS);
		for (int i = 256 ; i <= 0xFFFF ; i++) {
			out.append((char) i);
		}
		return (unicodeString = out.toString());
	}
	
}
