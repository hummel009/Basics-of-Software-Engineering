package com.github.hummel.bose;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMTGitHubDarkIJTheme;

import javax.swing.*;
import java.awt.*;

public class Main {
	/**
	 * Main method that launches the calculator application.
	 * Performs the following setup:
	 * 1. Initializes FlatLaf as the base look and feel
	 * 2. Applies the GitHub Dark theme from Material Theme UI Lite
	 * 3. Creates and displays the calculator window
	 * 4. Handles any initialization errors by printing stack traces
	 * <p>
	 * The application runs on the AWT Event Dispatch Thread for thread safety.
	 *
	 * @param arg Command-line arguments (not used)
	 */
	public static void main(String[] arg) {
		FlatLightLaf.setup();
		EventQueue.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(new FlatMTGitHubDarkIJTheme());
				var frame = new Calculator();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}