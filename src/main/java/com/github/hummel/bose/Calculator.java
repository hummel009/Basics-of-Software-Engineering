package com.github.hummel.bose;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;

public class Calculator extends JFrame {
	private final Map<String, JButton> buttons = new HashMap<>();
	private final Collection<JButton> buttonsExtendedSet = new ArrayList<>();

	private final Map<String, Runnable> customFunc = new HashMap<>();
	private final Map<String, Operation> oneOpFunc = new HashMap<>();
	private final Map<String, Operation> twoOpFunc = new HashMap<>();

	private final JPanel panel = new JPanel();
	private final JLabel outputField = new JLabel();

	private final Engine engine = new Engine();

	private Operation operation = Operation.NULL;
	private double operand1;
	private double operand2;

	private boolean extended;

	private int notInclude;

	public Calculator() {
		setTitle("Hummel009's Calculator");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Locale.setDefault(Locale.US);

		panel.setLayout(new GridLayout(7, 4));

		registerButton("C");
		registerButton("e");
		registerButton("π");
		registerButton("÷");

		registerButton("7");
		registerButton("8");
		registerButton("9");
		registerButton("×");

		registerButton("4");
		registerButton("5");
		registerButton("6");
		registerButton("-");

		registerButton("1");
		registerButton("2");
		registerButton("3");
		registerButton("+");

		registerButton("%");
		registerButton("0");
		registerButton(".");
		registerButton("=");

		registerButton("√");
		registerButton("^");
		registerButton("^2");
		registerButton("^3");

		registerButton("1/");
		registerButton("n!");
		registerButton("n!!");

		registerButton("⚙");

		registerButton("log", true);
		registerButton("sin°", true);
		registerButton("cos°", true);
		registerButton("tg°", true);
		registerButton("ctg°", true);
		registerButton("arcsin", true);
		registerButton("arccos", true);
		registerButton("arctg", true);
		registerButton("arcctg", true);
		registerButton("10^", true);
		registerButton("lg", true);
		registerButton("ln", true);
		registerButton("ch", true);
		registerButton("sh", true);
		registerButton("th", true);
		registerButton("cth", true);

		oneOpFunc.put("n!", Operation.FACTORIAL);
		oneOpFunc.put("n!!", Operation.DOUBLEFACT);
		oneOpFunc.put("√", Operation.SQRT);
		oneOpFunc.put("sin°", Operation.SIN);
		oneOpFunc.put("cos°", Operation.COS);
		oneOpFunc.put("tg°", Operation.TG);
		oneOpFunc.put("ctg°", Operation.CTG);
		oneOpFunc.put("arcsin", Operation.ARCSIN);
		oneOpFunc.put("arccos", Operation.ARCCOS);
		oneOpFunc.put("arctg", Operation.ARCTG);
		oneOpFunc.put("arcctg", Operation.ARCCTG);
		oneOpFunc.put("^2", Operation.SQARE);
		oneOpFunc.put("^3", Operation.CUBE);
		oneOpFunc.put("lg", Operation.LG);
		oneOpFunc.put("ln", Operation.LN);
		oneOpFunc.put("ch", Operation.CH);
		oneOpFunc.put("sh", Operation.SH);
		oneOpFunc.put("th", Operation.TH);
		oneOpFunc.put("cth", Operation.CTH);
		oneOpFunc.put("10^", Operation.TEN);
		oneOpFunc.put("1/x", Operation.BACK);

		twoOpFunc.put("+", Operation.PLUS);
		twoOpFunc.put("-", Operation.MINUS);
		twoOpFunc.put("×", Operation.MULTIPLE);
		twoOpFunc.put("log", Operation.LOGARITHM);
		twoOpFunc.put("^", Operation.POWER);
		twoOpFunc.put("÷", Operation.DIVIDE);
		twoOpFunc.put("%", Operation.PERCENT);

		customFunc.put("⚙", () -> {
			if (extended) {
				extended = false;
				panel.setLayout(new GridLayout(7, 4));
				panel.remove(buttons.get("⚙"));
				//noinspection StreamToLoop
				buttonsExtendedSet.forEach(panel::remove);
			} else {
				extended = true;
				panel.setLayout(new GridLayout(11, 4));
				panel.remove(buttons.get("⚙"));
				//noinspection StreamToLoop
				buttonsExtendedSet.forEach(panel::add);
			}
			panel.add(buttons.get("⚙"));
			panel.revalidate();
			panel.repaint();
		});
		customFunc.put("C", () -> {
			operand1 = 0;
			operand2 = 0;
			operation = Operation.NULL;
			outputField.setText("");
		});
		customFunc.put("=", () -> {
			try {
				operand2 = Double.parseDouble(outputField.getText().substring(notInclude));
				var output = engine.getCalculationResult(operation, operand1, operand2);
				var result = new DecimalFormat("#.###############").format(output);
				outputField.setText(outputField.getText() + '=' + result);
				operation = Operation.FINISH;
			} catch (Exception e) {
				outputField.setText("");
			}
		});
		customFunc.put("e", () -> outputField.setText("2.718281828459045"));
		customFunc.put("π", () -> outputField.setText("3.141592653589793"));

		outputField.setFont(outputField.getFont().deriveFont(20.0f));
		outputField.setHorizontalAlignment(SwingConstants.RIGHT);
		outputField.setPreferredSize(new Dimension(outputField.getWidth(), outputField.getFontMetrics(outputField.getFont()).getHeight()));

		add(outputField, BorderLayout.PAGE_START);
		add(panel, BorderLayout.CENTER);
		setSize(450, 750);
		setLocationRelativeTo(null);
	}

	private void registerButton(String name) {
		registerButton(name, false);
	}

	private void registerButton(String name, boolean extendedSet) {
		var button = new JButton();
		button.setFont(button.getFont().deriveFont(20.0f));
		button.addActionListener(event -> selectButton(((AbstractButton) event.getSource()).getText()));
		button.setText(name);
		if (extendedSet) {
			buttonsExtendedSet.add(button);
		} else {
			panel.add(button);
		}
		buttons.put(name, button);
	}

	public void selectButton(String buttonName) {
		if (outputField.getText().isEmpty()) {
			if (buttonName.matches("[0-9]")) {
				outputField.setText(outputField.getText() + buttonName);
			}
		} else {
			if (oneOpFunc.containsKey(buttonName)) {
				oneNumber(oneOpFunc.get(buttonName), buttonName);
			} else if (twoOpFunc.containsKey(buttonName)) {
				twoNumbers(twoOpFunc.get(buttonName), buttonName);
			} else if (customFunc.containsKey(buttonName)) {
				customFunc.get(buttonName).run();
			} else {
				if (operation == Operation.FINISH) {
					outputField.setText(buttonName);
					operation = Operation.NULL;
				} else {
					outputField.setText(outputField.getText() + buttonName);
				}
			}
		}
	}

	private void oneNumber(Operation op, String buttonName) {
		var outputText = outputField.getText();
		var equalsIndex = outputText.indexOf('=');
		if (equalsIndex != -1) {
			outputText = outputText.substring(equalsIndex + 1).trim();
			outputField.setText(outputText);
		}
		try {
			operand1 = Double.parseDouble(outputField.getText());
			operation = op;
			var output = engine.getCalculationResult(operation, operand1, operand2);
			var result = new DecimalFormat("#.###############").format(output);
			outputField.setText(buttons.get(buttonName).getText() + '(' + outputField.getText() + ')' + '=' + result);
			operation = Operation.FINISH;
		} catch (Exception e) {
			outputField.setText("");
		}
	}

	private void twoNumbers(Operation op, CharSequence buttonName) {
		var outputText = outputField.getText();
		var equalsIndex = outputText.indexOf('=');
		if (equalsIndex != -1) {
			outputText = outputText.substring(equalsIndex + 1).trim();
			outputField.setText(outputText);
		}
		try {
			notInclude = outputField.getText().length() + buttonName.length();
			operand1 = Double.parseDouble(outputField.getText());
			operation = op;
			outputField.setText(outputField.getText() + buttonName);
		} catch (Exception e) {
			outputField.setText("");
		}
	}

	public boolean isExtended() {
		return extended;
	}

	public String getOutputText() {
		return outputField.getText();
	}
}