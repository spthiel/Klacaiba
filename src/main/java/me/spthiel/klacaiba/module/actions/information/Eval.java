package me.spthiel.klacaiba.module.actions.information;

import net.eq2online.macros.scripting.Variable;
import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;

import me.spthiel.klacaiba.base.BaseScriptAction;

public class Eval extends BaseScriptAction {
	
	public Eval()
	{
		super("eval");
	}
	
	public void onInit()
	{
		this.context.getCore().registerScriptAction(this);
	}
	
	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params)
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
		
		
		eval = eval.replace("x", "*")
				   .replace(":", "/");
		
		double evaluated = eval(eval);
		
		if (varName != null) {
			if (Variable.couldBeInt(varName)) {
				provider.setVariable(macro, varName, (int)evaluated);
			} else {
				provider.setVariable(macro, varName, evaluated + "");
			}
		}
		
		return new ReturnValue(evaluated + "");
			
		
	}
	
	
	public static double eval(final String str) {
		return new Object() {
			int pos = -1, ch;
			
			void nextChar() {
				ch = (++pos < str.length()) ? str.charAt(pos) : -1;
			}
			
			boolean eat(int charToEat) {
				while (ch == ' ')
					nextChar();
				if (ch == charToEat) {
					nextChar();
					return true;
				}
				return false;
			}
			
			double parse() {
				nextChar();
				double x = parseExpression();
				if (pos < str.length())
					throw new RuntimeException("Unexpected: " + (char) ch);
				return x;
			}
			
			double parseExpression() {
				double x = parseTerm();
				for (;;) {
					if (eat('+'))
						x += parseTerm(); // addition
					else if (eat('-'))
						x -= parseTerm(); // subtraction
					else
						return x;
				}
			}
			
			double parseTerm() {
				double x = parseFactor();
				for (;;) {
					if (eat('*'))
						x *= parseFactor(); // multiplication
					else if (eat('/'))
						x /= parseFactor(); // division
					else
						return x;
				}
			}
			
			double parseFactor() {
				if (eat('+'))
					return parseFactor(); // unary plus
				if (eat('-'))
					return -parseFactor(); // unary minus
				
				double x;
				int startPos = this.pos;
				if (eat('(')) { // parentheses
					x = parseExpression();
					eat(')');
				} else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
					while ((ch >= '0' && ch <= '9') || ch == '.')
						nextChar();
					x = Double.parseDouble(str.substring(startPos, this.pos));
				} else if (ch >= 'a' && ch <= 'z') { // functions
					while (ch >= 'a' && ch <= 'z')
						nextChar();
					String func = str.substring(startPos, this.pos);
					if (func.equals("pi"))
						x = Math.PI;
					else if (func.equals("e"))
						x = Math.E;
					else {
						x = parseFactor();
						switch (func) {
							case "sqrt":
								x = Math.sqrt(x);
								break;
							case "sin":
								x = Math.sin(Math.toRadians(x));
								break;
							case "cos":
								x = Math.cos(Math.toRadians(x));
								break;
							case "tan":
								x = Math.tan(Math.toRadians(x));
								break;
							case "arcsin":
								x = Math.asin(Math.toRadians(x));
								break;
							case "arccos":
								x = Math.acos(Math.toRadians(x));
								break;
							case "arctan":
								x = Math.atan(Math.toRadians(x));
								break;
							case "ran":
								x = Math.random() * x;
								break;
							case "floor":
								x = Math.floor(x);
								break;
							case "ceil":
								x = Math.ceil(x);
								break;
							case "round":
								x = Math.round(x);
								break;
							case "log":
								x = Math.log(x);
								break;
							default:
								throw new RuntimeException("Unknown function: " + func);
						}
						
					}
				} else {
					throw new RuntimeException("Unexpected: " + (char) ch);
				}
				
				if (eat('^'))
					x = Math.pow(x, parseFactor()); // exponentiation
				
				return x;
			}
		}.parse();
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "[&result =] eval(<[#&]result>,<expression>)";
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
