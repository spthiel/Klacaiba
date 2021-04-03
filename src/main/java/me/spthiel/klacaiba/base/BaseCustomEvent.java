package me.spthiel.klacaiba.base;

import net.eq2online.macros.scripting.ScriptedIterator;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.api.IScriptedIterator;
import net.eq2online.macros.scripting.api.IVariableProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public abstract class BaseCustomEvent<T> implements IScriptedIterator {
	
	private final LinkedList<T> queue;
	private final String iteratorName;
	private final HashMap<String, Object> currentVariables;
	private final String variablePrefix;
	
	public BaseCustomEvent(String iteratorName, String variablePrefix) {
		this.queue = new LinkedList<>();
		this.iteratorName = iteratorName;
		this.currentVariables = new HashMap<>();
		this.variablePrefix = variablePrefix.toUpperCase();
	}
	
	public final BaseCustomEvent<T> make(String[] params) {
		
		try {
			//noinspection unchecked
			BaseCustomEvent<T> iterator = this.getClass().getDeclaredConstructor(String[].class).newInstance((Object) params);
			iterator.init();
			return iterator;
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			try {
				//noinspection unchecked
				BaseCustomEvent<T> iterator = this.getClass().newInstance();
				iterator.init();
				return iterator;
			} catch (InstantiationException | IllegalAccessException e2) {
				throw new RuntimeException(e2);
			}
		}
	}
	
	protected final void populate(T object) {
		this.queue.add(object);
		if (this.queue.size() > 100) {
			this.queue.removeLast();
		}
	}
	
	public String getIteratorName() {
		
		return iteratorName;
	}
	
	protected final void registerVariable(String variableName, Object value) {
		
		this.currentVariables.put(variablePrefix + variableName, value);
	}
	
	@Override
	public Object getVariable(String variableName) {
		
		return currentVariables.get(variableName);
	}
	
	@Override
	public Set<String> getVariables() {
		
		return currentVariables.keySet();
	}
	
	@Override
	public boolean isActive() {
		
		return queue.size() > 0;
	}
	
	@Override
	public void increment() {
		this.currentVariables.clear();
		if (isActive()) {
			this.registerVariablesFor(this.queue.removeLast());
		}
		this.currentVariables.put("POLLALL", this.currentVariables.keySet().toString());
	}
	
	@Override
	public void updateVariables(boolean clock) {}
	
	@Override
	public void reset() {
	
	}
	
	@Override
	public void terminate() {
	
	}
	
	@Override
	public final void onInit() {
	
	}
	
	protected abstract void registerVariablesFor(T object);
	protected abstract void init();
	
	
}
