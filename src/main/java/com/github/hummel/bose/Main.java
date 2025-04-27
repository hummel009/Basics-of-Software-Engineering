package com.github.hummel.bose;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMTGitHubDarkIJTheme;

import javax.swing.*;
import java.awt.*;

public class Main {
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
