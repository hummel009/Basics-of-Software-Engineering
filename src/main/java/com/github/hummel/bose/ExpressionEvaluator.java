package com.github.hummel.bose;

/**
 * A mathematical expression evaluator that parses and computes the value of arithmetic expressions.
 * Supports basic operations (+, -, *, /), parentheses, exponents (^), unary operators, and decimal numbers.
 * Implements operator precedence and handles whitespace in expressions.
 */
public class ExpressionEvaluator {
	private final String expression;
	private int pos = -1;
	private int ch;

	/**
	 * Constructs a new ExpressionEvaluator for the specified mathematical expression.
	 *
	 * @param expression The mathematical expression to evaluate. May contain:
	 *                   - Numbers (integer and decimal)
	 *                   - Operators: +, -, *, /, ^ (exponent)
	 *                   - Parentheses for grouping
	 *                   - Unary + and - operators
	 *                   - Whitespace (ignored)
	 * @throws NullPointerException if the expression is null
	 */
	ExpressionEvaluator(String expression) {
		this.expression = expression;
	}

	/**
	 * Parses and evaluates the mathematical expression.
	 *
	 * @return The computed result of the expression
	 * @throws RuntimeException if the expression contains:
	 *                          - Unexpected characters
	 *                          - Invalid syntax
	 *                          - Unbalanced parentheses
	 *                          - Malformed numbers
	 */
	double parse() {
		nextChar();
		var x = parseExpression();
		if (pos < expression.length()) {
			throw new RuntimeException("Unexpected symbol: " + (char) ch);
		}
		return x;
	}

	/**
	 * Advances to the next character in the expression.
	 * Sets ch to -1 when reaching the end of the expression.
	 */
	private void nextChar() {
		++pos;
		ch = pos < expression.length() ? expression.charAt(pos) : -1;
	}

	/**
	 * Consumes whitespace and checks if the current character matches the expected one.
	 * If matched, advances to the next character.
	 *
	 * @param charToEat The character to match
	 * @return true if the character was matched and consumed, false otherwise
	 */
	private boolean eat(int charToEat) {
		while (ch == ' ') {
			nextChar();
		}
		if (ch == charToEat) {
			nextChar();
			return true;
		}
		return false;
	}

	/**
	 * Parses an expression (addition and subtraction operations).
	 *
	 * @return The result of the parsed expression
	 */
	private double parseExpression() {
		var x = parseTerm();
		while (true) {
			if (eat('+')) {
				x += parseTerm();
			} else if (eat('-')) {
				x -= parseTerm();
			} else {
				return x;
			}
		}
	}

	/**
	 * Parses a term (multiplication and division operations).
	 *
	 * @return The result of the parsed term
	 */
	private double parseTerm() {
		var x = parseFactor();
		while (true) {
			if (eat('*')) {
				x *= parseFactor();
			} else if (eat('/')) {
				x /= parseFactor();
			} else {
				return x;
			}
		}
	}

	/**
	 * Parses a factor which can be:
	 * - A number
	 * - A parenthesized expression
	 * - A unary operator (+ or -) followed by a factor
	 * - An exponentiation operation (^)
	 *
	 * @return The result of the parsed factor
	 * @throws RuntimeException if invalid syntax is encountered
	 */
	private double parseFactor() {
		while (true) {
			if (eat('+')) {
				continue;
			}
			if (eat('-')) {
				return -parseFactor();
			}

			double x;
			var startPos = pos;
			if (eat('(')) {
				x = parseExpression();
				eat(')');
			} else if (ch >= '0' && ch <= '9' || ch == '.') {
				while (ch >= '0' && ch <= '9' || ch == '.') {
					nextChar();
				}
				x = Double.parseDouble(expression.substring(startPos, pos));
			} else {
				throw new RuntimeException("Unexpected symbol: " + (char) ch);
			}

			if (eat('^')) {
				return Math.pow(x, parseFactor());
			}
			return x;
		}
	}
}