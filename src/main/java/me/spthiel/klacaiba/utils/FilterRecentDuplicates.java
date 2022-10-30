package me.spthiel.klacaiba.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class FilterRecentDuplicates<T> {
	
	private final HashMap<T, Long> cache = new HashMap<>();
	private final long             recentLength;
	
	public FilterRecentDuplicates(long recentLength) {
		
		this.recentLength = recentLength;
	}
	
	public boolean add(T value) {
		this.clear();
		if (cache.containsKey(value)) {
			return false;
		}
		cache.put(value, System.currentTimeMillis());
		return true;
	}
	
	private void clear() {
		
		ArrayList<T> toRemove = new ArrayList<>();
		for (T key : cache.keySet()) {
			long value = cache.get(key);
			if (value < System.currentTimeMillis() - recentLength) {
				toRemove.add(key);
			}
		}
		toRemove.forEach(cache::remove);
	}
	
}
