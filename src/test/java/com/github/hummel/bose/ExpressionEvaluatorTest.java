package com.github.hummel.bose;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link ExpressionEvaluator} class.
 * Tests cover basic arithmetic operations, edge cases, and error handling.
 */
class ExpressionEvaluatorTest {

	/**
	 * Tests basic arithmetic operations (addition, subtraction, multiplication, division).
	 */
	@Test
	void testBasicOperations() {
		assertEquals(5.0, new ExpressionEvaluator("2 + 3").parse());
		assertEquals(-1.0, new ExpressionEvaluator("2 - 3").parse());
		assertEquals(6.0, new ExpressionEvaluator("2 * 3").parse());
		assertEquals(0.666, new ExpressionEvaluator("2 / 3").parse(), 0.001);
	}

	/**
	 * Tests operator precedence and grouping with parentheses.
	 */
	@Test
	void testOperatorPrecedenceAndParentheses() {
		assertEquals(8.0, new ExpressionEvaluator("2 + 3 * 2").parse());
		assertEquals(10.0, new ExpressionEvaluator("(2 + 3) * 2").parse());
		assertEquals(16.0, new ExpressionEvaluator("2 ^ 3 * 2").parse());
		assertEquals(64.0, new ExpressionEvaluator("2 ^ (3 * 2)").parse());
	}

	/**
	 * Tests exponentiation and unary operators.
	 */
	@Test
	void testExponentsAndUnary() {
		assertEquals(8.0, new ExpressionEvaluator("2 ^ 3").parse());
		assertEquals(-8.0, new ExpressionEvaluator("-2 ^ 3").parse());
		assertEquals(-8.0, new ExpressionEvaluator("(-2) ^ 3").parse());
		assertEquals(0.125, new ExpressionEvaluator("2 ^ -3").parse());
	}

	/**
	 * Tests decimal numbers and whitespace handling.
	 */
	@Test
	void testDecimalsAndWhitespace() {
		assertEquals(3.5, new ExpressionEvaluator("1.5 + 2.0").parse());
		assertEquals(3.5, new ExpressionEvaluator("  1.5  +  2.0  ").parse());
	}

	/**
	 * Tests invalid expressions that should throw exceptions.
	 */
	@Test
	void testInvalidExpressions() {
		assertThrows(RuntimeException.class, () -> new ExpressionEvaluator("2 +").parse());
		assertThrows(RuntimeException.class, () -> new ExpressionEvaluator("2 + * 3").parse());
		assertThrows(RuntimeException.class, () -> new ExpressionEvaluator("abc").parse());
		assertThrows(RuntimeException.class, () -> new ExpressionEvaluator("2 (").parse());
	}
}