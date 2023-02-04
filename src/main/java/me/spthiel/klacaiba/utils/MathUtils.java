package me.spthiel.klacaiba.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MathUtils {
	
	private static final List<Integer> superscripts = Arrays.asList(0x2070, 0x00B9, 0x00B2, 0x00B3, 0x2074, 0x2075, 0x2076, 0x2077, 0x2078, 0x2079);
	private static final Map<Integer, Double> fractions = new HashMap<>();
	
	static {
		
		fractions.put(0x2150,1/7d);
		fractions.put(0x2151,1/9d);
		fractions.put(0x2152,1/10d);
		fractions.put(0x2153,1/3d);
		fractions.put(0x2154,2/3d);
		fractions.put(0x2155,1/5d);
		fractions.put(0x2156,2/5d);
		fractions.put(0x2157,3/5d);
		fractions.put(0x2158,4/5d);
		fractions.put(0x2159,1/6d);
		fractions.put(0x215A,5/6d);
		fractions.put(0x215B,1/8d);
		fractions.put(0x215C,3/8d);
		fractions.put(0x215D,5/8d);
		fractions.put(0x215E,7/8d);
		fractions.put(0x00BC,1/4d);
		fractions.put(0x00BD,1/2d);
		fractions.put(0x00BE,3/4d);
		fractions.put(0x2189,0d);
	}
	
	public static void main(String[] args) {
		
		String[] testcases = new String[]{
			"5 > 4",
			"4 > 5",
			"4 == 4",
			"3 == 5",
			"6 >= 5",
			"5 >= 5",
			"4 >= 5",
			"5 & 3",
			"5.5 & 0",
			"5 | 3",
			"5.5 | 0",
			"5 >> 1",
			"5 >> 2",
			"5 << 1",
			"5 << 2",
			"1 != 2",
			"1 != 1",
			"5 && 0",
			"5 && 1",
			"0 || 1",
			"0 || 0",
			"8.456 > 8.3",
			"8.456 < 8.7",
			"(8.456 > 8.3) && (8.456 < 8.7)",
			"8.123 > 8.3",
			"8.123 < 8.7",
			"(8.123 > 8.3) && (8.123 < 8.7)",
			"8.800 > 8.3",
			"8.800 < 8.7",
			"(8.800 > 8.3) && (8.800 < 8.7)",
		};
		
		for (String test :
			testcases) {
			System.out.println(test + " = " + eval(test));
		}
	}
	
	public static double eval(final String str) {
		return new Object() {
			int pos = -1, ch;
			
			void nextChar() {
				ch = (++pos < str.length()) ? str.charAt(pos) : -1;
			}
			
			void prevChar() {
				ch = (--pos >= 0) ? str.charAt(pos) : -1;
			}
			
			boolean eat(int charToEat) {
				while (ch == ' ') {
					nextChar();
				}
				if (ch == charToEat) {
					nextChar();
					return true;
				}
				return false;
			}
			
			double parse() {
				nextChar();
				double x = parseExpression();
				if (pos < str.length()) {
					throw new RuntimeException("Unexpected: " + (char) ch);
				}
				return x;
			}
			
			double parseExpression() {
				double x = parseTerm();
				for (;;) {
					if (eat('+')) {
						x += parseTerm(); // addition
					} else if (eat('-')) {
						x -= parseTerm(); // subtraction
					} else {
						return x;
					}
				}
			}
			
			double parseTerm() {
				double x = parseFactor();
				for (;;) {
					if (eat('*') || eat('\u00D7') || eat('x') || eat('\u2715')) { // Multiplication sign, multiplication X
						x *= parseFactor(); // multiplication
					} else if (eat('/') || eat('\u00F7') || eat('∕')) {
						x /= parseFactor(); // division
					} else if (eat('&')) {
						if (eat('&')) {
							boolean factor = parseFactor() != 0;
							x = x != 0 && factor ? 1 : 0;
						} else {
							x = (int) x & (int) parseFactor();
						}
					} else if (eat('|')) {
						if (eat('|')) {
							boolean factor = parseFactor() != 0;
							x = x != 0 || factor ? 1 : 0;
						} else {
							x = (int) x | (int) parseFactor();
						}
					} else if (eat('>')) {
						if (eat('>')) {
							x = (int) x >> (int) parseFactor();
						} else if (eat('=')) {
							x = x >= parseFactor() ? 1 : 0;
						} else {
							x = x > parseFactor() ? 1 : 0;
						}
					} else if (eat('<')) {
						if (eat('<')) {
							x = (int)x << (int)parseFactor();
						} else if (eat('=')) {
							x = x <= parseFactor() ? 1 : 0;
						} else {
							x = x < parseFactor() ? 1 : 0;
						}
					} else if (eat('=')) {
						if (eat('=')) {
							x = x == parseFactor() ? 1 : 0;
						} else {
							prevChar();
						}
					} else if (eat('!')) {
						if (eat('=')) {
							x = x != parseFactor() ? 1 : 0;
						} else {
							prevChar();
						}
					} else {
						return x;
					}
				}
			}
			
			double parseFactor() {
				if (eat('+')) {
					return parseFactor(); // unary plus
				}
				if (eat('-')) {
					return -parseFactor(); // unary minus
				}
				
				double x;
				int startPos = this.pos;
				if (eat('(')) { // parentheses
					x = parseExpression();
					eat(')');
				} else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
					while ((ch >= '0' && ch <= '9') || ch == '.') {
						nextChar();
					}
					x = Double.parseDouble(str.substring(startPos, this.pos));
				} else if (ch >= 'a' && ch <= 'z') { // functions
					while (ch >= 'a' && ch <= 'z') {
						nextChar();
					}
					String func = str.substring(startPos, this.pos);
					if (func.equalsIgnoreCase("pi")) {
						x = Math.PI;
					} else if (func.equals("e") && ch < '0' || ch > '9') {
						x = Math.E;
					} else {
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
								x = Math.asin(x);
								break;
							case "arccos":
								x = Math.acos(x);
								break;
							case "arctan":
								x = Math.atan(x);
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
							case "abs":
								x = Math.abs(x);
								break;
							default:
								throw new RuntimeException("Unknown function: " + func);
						}
						
					}
				} else if (fractions.containsKey((int)ch)) {
					x = fractions.get((int)ch);
					eat(ch);
				} else {
					throw new RuntimeException("Unexpected: " + (char) ch);
				}
				
				if (eat('^')) {
					x = Math.pow(x, parseFactor()); // exponentiation
				} else if(eat('e') || eat('E')) {
					x = x * Math.pow(10, parseFactor());
				} else if (eat('!')) {
					if (eat('=')) {
						prevChar();
						prevChar();
					} else {
						double val = 1;
						x = Math.floor(x);
						for (int i = 1 ; i <= x ; i++) {
							val *= i;
						}
						x = val;
					}
				} else if(superscripts.contains(ch) || ch == 0x207B) { // superscript
					boolean negative = eat('\u207B');
					long superscript = 0;
					int num;
					while ((num = superscripts.indexOf(ch)) != -1) {
						superscript *= 10;
						superscript += num;
						nextChar();
					}
					if (negative) {
						superscript = -superscript;
					}
					x = Math.pow(x, superscript);
				} else if (fractions.containsKey((int)ch)) {
					x = x + fractions.get((int)ch);
					eat(ch);
				}
				
				return x;
			}
		}.parse();
	}
}
