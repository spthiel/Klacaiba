package me.spthiel.klacaiba.newactions.with;

import me.spthiel.klacaiba.actions.ElseIfBase;

public class ElseIfEndsWith extends ElseIfBase {
	
	public ElseIfEndsWith() {
		super("elseifendswith", Combiners.ENDS);
	}
	
	@Override
	protected boolean check(String haystack, String needle) {
		
		return haystack.endsWith(needle);
	}
}
