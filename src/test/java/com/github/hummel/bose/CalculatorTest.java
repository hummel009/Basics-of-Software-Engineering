package com.github.hummel.bose;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Calculator} class.
 * Tests focus on UI component verification and button click handling.
 */
class CalculatorTest {

	/**
	 * Tests calculator initialization and UI components.
	 */
	@Test
	void testCalculatorInitialization() {
		var calculator = new Calculator();

		assertNotNull(calculator.getContentPane());
		assertEquals("Hummel009's Calculator", calculator.getTitle());
		assertEquals(WindowConstants.EXIT_ON_CLOSE, calculator.getDefaultCloseOperation());

		var components = calculator.getContentPane().getComponents();
		assertTrue(components[0] instanceof JTextField);
		assertTrue(components[1] instanceof JPanel);
	}

	/**
	 * Tests button click handling and display updates.
	 */
	@Test
	void testButtonClicks() {
		var calculator = new Calculator();
		var display = (JTextField) calculator.getContentPane().getComponent(0);

		calculator.onButtonClick("5");
		assertEquals("5", display.getText());

		calculator.onButtonClick("+");
		assertEquals("5+", display.getText());

		calculator.onButtonClick("2");
		assertEquals("5+2", display.getText());

		calculator.onButtonClick("=");
		assertEquals("7.0", display.getText());

		calculator.onButtonClick("C");
		assertEquals("", display.getText());
	}
}