package me.spthiel.klacaiba.module.actions.information;

import net.eq2online.macros.scripting.Variable;
import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.base.BaseScriptAction;
import me.spthiel.klacaiba.utils.MathUtils;

public class Calc extends BaseScriptAction {
	
	public Calc()
	{
		super("calc");
	}
	
	public void onInit()
	{
		this.context.getCore().registerScriptAction(this);
	}
	
	public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params)
	{
		
		if (params.length == 0) {
			return new ReturnValue("Invalid arguments");
		}
		
		String varName;
		String eval;
		
		if (params.length > 1) {
			varName = Variable.getValidVariableOrArraySpecifier(params[0]);
			eval = provider.expand(macro, params[1], false);
		} else {
			varName = null;
			eval = provider.expand(macro, params[0], false);
		}
		
		double evaluated = MathUtils.eval(eval);
		
		if (varName != null) {
			provider.setVariable(macro, varName, evaluated + "", (int) evaluated, evaluated != 0);
		}
		
		String outvar = instance.getOutVarName();
		
		if (Variable.couldBeBoolean(outvar)) {
			return new ReturnValue(evaluated != 0);
		} else if (Variable.couldBeInt(outvar)) {
			return new ReturnValue((int) evaluated);
		}
		
		return new ReturnValue(evaluated + "");
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "[[&#]result =] eval(<[&#]result>,<expression>)";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Evaluates an expression with float values";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "";
	}
}
