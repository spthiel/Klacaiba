package me.spthiel.klacaiba.module.events;

import net.eq2online.macros.compatibility.I18n;
import net.eq2online.macros.scripting.actions.lang.ScriptActionNext;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import me.spthiel.klacaiba.module.actions.base.BaseCustomEvent;
import me.spthiel.klacaiba.module.actions.base.IDocumentable;
import me.spthiel.klacaiba.module.actions.base.IMultipleScriptAction;
import me.spthiel.klacaiba.utils.PacketListener;

public class PollEvent extends ScriptAction implements IDocumentable, IMultipleScriptAction {
	
	private final LinkedList<BaseCustomEvent<?>> events;
	
	public PollEvent() {
		
		super(ScriptContext.MAIN, "pollevent");
		events = new LinkedList<>();
		setupEvents();
	}
	
	private BaseCustomEvent<?> getIteratorBy(String iteratorName) {
		return events.stream().filter(iterator -> iterator.getIteratorName().equalsIgnoreCase(iteratorName)).findFirst().orElse(null);
	}
	
	@Override
	public boolean isThreadSafe() {
		
		return false;
	}
	
	@Override
	public boolean isClocked() {
		
		return true;
	}
	
	public String getExpectedPopCommands() {
		return I18n.get("script.error.stackhint", this, "NEXT");
	}
	
	private void setupEvents() {
		
		registerEvent(new EventOnBlockBreak());
		registerEvent(new EventOnSound());
		registerEvent(new EventOnBossBar());
		registerEvent(new EventOnChat());
		registerEvent(new EventOnPlayerJoined());
		PacketListener.getInstance().registerOwnEvents(this.events);
		PacketListener.getInstance().start();
	}
	
	public void registerEvent(BaseCustomEvent<?> event) {
		this.events.add(event);
	}
	
	@Override
	public boolean isStackPushOperator() {
		
		return true;
	}
	
	@Override
	public boolean executeStackPush(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		IScriptedIterator state = getState(macro);
		if (params.length == 0) {
			Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Available events: " + this.events.stream().map(BaseCustomEvent::getIteratorName).collect(Collectors.joining(","))));
			return false;
		}
		if (state == null) {
			String iteratorName = provider.expand(macro, params[0], false);
			String[] iteratorParams = Arrays.copyOfRange(params, 1, params.length);
			BaseCustomEvent<?> iterator = getIteratorBy(iteratorName);
			if (iterator != null) {
				iterator = iterator.make(iteratorParams);
				macro.registerVariableProvider(iterator);
				iterator.increment();
				setState(macro, iterator);
			} else {
				return false;
			}
		} else if (state.isActive()) {
			state.increment();
		} else {
			this.setState(macro, null);
			return false;
		}
		return true;
	}
	
	public boolean executeStackPop(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params, IMacroAction popAction) {
		
		return false;
	}
	
	public boolean canBePoppedBy(IScriptAction action) {
		return action instanceof ScriptActionNext;
	}
	
	@Override
	public void onStopped(IScriptActionProvider provider, IMacro macro, IMacroAction instance) {
		terminate(macro);
	}
	
	public boolean canBreak(IMacroActionProcessor processor, IScriptActionProvider provider, IMacro macro, IMacroAction instance, IMacroAction breakAction) {
		return terminate(macro);
	}
	
	private boolean terminate(IMacro macro) {
		IScriptedIterator state = getState(macro);
		if (state != null) {
			state.terminate();
			macro.unregisterVariableProvider(state);
			return true;
		} else {
			return false;
		}
	}
	
	private IScriptedIterator getState(IMacro macro) {
		return macro.getState("pollevent");
	}
	
	private void setState(IMacro macro, IScriptedIterator iterator) {
		macro.setState("pollevent", iterator);
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "pollevent([eventname])";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Creates a constant stream of the event. Think of it as an infinite foreach. Use await before the next to not create infinite loops example: pollevent(\"blockbreak\");log(\"There was an event\");await;next";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "";
	}
	
	@Override
	public void registerAdditionalActions(Consumer<IScriptAction> actionConsumer) {
		actionConsumer.accept(new Await());
	}
}
