package com.github.hummel.bose;

import javax.swing.*;
import java.awt.*;

/**
 * A simple calculator GUI application that extends JFrame.
 * The calculator supports basic arithmetic operations, parentheses, exponents, and decimal numbers.
 * Users can input expressions via buttons and see the results in a display field.
 */
public class Calculator extends JFrame {
	private final JTextField display;

	/**
	 * Constructs a new Calculator instance with a graphical user interface.
	 * The calculator includes:
	 * - A display field at the top
	 * - Number buttons (0-9)
	 * - Operator buttons (+, -, *, /, ^)
	 * - Parentheses buttons
	 * - Decimal point button
	 * - Clear (C) button
	 * - Equals (=) button to calculate results
	 * The window is centered on screen and has a fixed size of 350x450 pixels.
	 */
	public Calculator() {
		setTitle("Hummel009's Calculator");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(350, 450);
		setLayout(new BorderLayout());

		display = new JTextField();
		display.setFont(new Font("Arial", Font.BOLD, 24));
		display.setEditable(false);
		display.setHorizontalAlignment(SwingConstants.RIGHT);
		add(display, BorderLayout.PAGE_START);

		var buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(5, 4, 5, 5));

		var buttons = new String[]{
				"7", "8", "9", "/",
				"4", "5", "6", "*",
				"1", "2", "3", "-",
				"0", ".", "(", "+",
				"C", ")", "^", "="
		};

		for (var text : buttons) {
			var button = new JButton(text);
			button.setFont(new Font("Arial", Font.BOLD, 20));
			button.addActionListener(e -> onButtonClick(e.getActionCommand()));
			buttonsPanel.add(button);
		}

		add(buttonsPanel, BorderLayout.CENTER);
		setLocationRelativeTo(null);
	}

	/**
	 * Handles button click events from the calculator's buttons.
	 *
	 * @param command The text of the button that was clicked. Possible values include:
	 *                - Digits (0-9)
	 *                - Operators (+, -, *, /, ^)
	 *                - Decimal point (.)
	 *                - Parentheses ((, ))
	 *                - Clear (C) - clears the display
	 *                - Equals (=) - evaluates the expression and shows the result
	 *                For all buttons except "C" and "=", the button's text is appended to the display.
	 */
	public void onButtonClick(String command) {
		switch (command) {
			case "C":
				display.setText("");
				break;
			case "=":
				try {
					var result = new ExpressionEvaluator(display.getText()).parse();
					display.setText(Double.toString(result));
				} catch (Exception ex) {
					display.setText("Error!");
				}
				break;
			default:
				display.setText(display.getText() + command);
		}
	}
}