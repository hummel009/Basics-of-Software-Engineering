package com.github.hummel.bose;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Unit tests for {@link Main} class.
 * Tests verify the application launch process.
 */
class MainTest {

	/**
	 * Tests main method execution (basic verification that it runs without exceptions).
	 */
	@Test
	void testMainMethod() {
		assertDoesNotThrow(() -> Main.main(new String[]{}));
	}

	/**
	 * Tests the look and feel setup in a headless environment.
	 */
	@Test
	void testLookAndFeelSetup() {
		assertDoesNotThrow(() -> {
			System.setProperty("java.awt.headless", "true");
			Main.main(new String[]{});
			System.setProperty("java.awt.headless", "false");
		});
	}
}