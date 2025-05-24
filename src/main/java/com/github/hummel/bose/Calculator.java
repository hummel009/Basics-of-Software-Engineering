package com.github.hummel.bose;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Supplier;

public class Calculator extends JFrame {
	private final Map<String, JButton> buttons = new HashMap<>();
	private final Collection<JButton> buttonsExtendedSet = new ArrayList<>();
	private final JPanel panel = new JPanel();

	private final Map<String, Supplier<Runnable>> func = new HashMap<>();

	private final Map<Operation, String> oneOperand = new EnumMap<>(Operation.class);
	private final Map<Operation, String> twoOperands = new EnumMap<>(Operation.class);

	private final Engine engine = new Engine();
	private final JLabel outputField = new JLabel();
	private Operation operation = Operation.NULL;
	private double input1;
	private double input2;
	private int notInclude;

	private boolean extended;

	public Calculator() {
		setTitle("Hummel009's Calculator");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Locale.setDefault(Locale.US);

		panel.setLayout(new GridLayout(7, 4));

		registerButton("C", false);
		registerButton("e", false);
		registerButton("π", false);
		registerButton("÷", false);

		registerButton("7", false);
		registerButton("8", false);
		registerButton("9", false);
		registerButton("×", false);

		registerButton("4", false);
		registerButton("5", false);
		registerButton("6", false);
		registerButton("-", false);

		registerButton("1", false);
		registerButton("2", false);
		registerButton("3", false);
		registerButton("+", false);

		registerButton("%", false);
		registerButton("0", false);
		registerButton(".", false);
		registerButton("=", false);

		registerButton("√", false);
		registerButton("^", false);
		registerButton("^2", false);
		registerButton("^3", false);

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

		registerButton("1/x", false);
		registerButton("n!", false);
		registerButton("n!!", false);

		registerButton("⚙", false);

		oneOperand.put(Operation.FACTORIAL, "n!");
		oneOperand.put(Operation.DOUBLEFACT, "n!!");
		oneOperand.put(Operation.SQRT, "√");
		oneOperand.put(Operation.SIN, "sin°");
		oneOperand.put(Operation.COS, "cos°");
		oneOperand.put(Operation.TG, "tg°");
		oneOperand.put(Operation.CTG, "ctg°");
		oneOperand.put(Operation.ARCSIN, "arcsin");
		oneOperand.put(Operation.ARCCOS, "arccos");
		oneOperand.put(Operation.ARCTG, "arctg");
		oneOperand.put(Operation.ARCCTG, "arcctg");
		oneOperand.put(Operation.SQARE, "^2");
		oneOperand.put(Operation.CUBE, "^3");
		oneOperand.put(Operation.LG, "lg");
		oneOperand.put(Operation.LN, "ln");
		oneOperand.put(Operation.CH, "ch");
		oneOperand.put(Operation.SH, "sh");
		oneOperand.put(Operation.TH, "th");
		oneOperand.put(Operation.CTH, "cth");
		oneOperand.put(Operation.TEN, "10^");
		oneOperand.put(Operation.BACK, "1/x");

		twoOperands.put(Operation.PLUS, "+");
		twoOperands.put(Operation.MINUS, "-");
		twoOperands.put(Operation.MULTIPLE, "×");
		twoOperands.put(Operation.LOGARITHM, "log");
		twoOperands.put(Operation.POWER, "^");
		twoOperands.put(Operation.DIVIDE, "÷");
		twoOperands.put(Operation.PERCENT, "%");

		func.put("⚙", () -> () -> {
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
		func.put("C", () -> () -> {
			input1 = input2 = 0;
			outputField.setText("");
		});
		func.put("=", () -> () -> {
			try {
				input2 = Double.parseDouble(outputField.getText().substring(notInclude));
				var output = engine.getCalculationResult(operation, input1, input2);
				var result = new DecimalFormat("#.###############").format(output);
				outputField.setText(outputField.getText() + '=' + result);
				operation = Operation.FINISHED;
			} catch (Exception e) {
				outputField.setText("");
			}
		});
		func.put("e", () -> () -> outputField.setText("2.718281828459045"));
		func.put("π", () -> () -> outputField.setText("3.141592653589793"));

		outputField.setFont(outputField.getFont().deriveFont(20.0f));
		outputField.setHorizontalAlignment(SwingConstants.RIGHT);
		outputField.setPreferredSize(new Dimension(outputField.getWidth(), outputField.getFontMetrics(outputField.getFont()).getHeight()));

		add(outputField, BorderLayout.PAGE_START);
		add(panel, BorderLayout.CENTER);
		setSize(450, 750);
		setLocationRelativeTo(null);
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
		var skip = false;

		for (var entry : oneOperand.entrySet()) {
			if (buttonName.equals(entry.getValue())) {
				oneNumber(entry.getKey(), entry.getValue());
				skip = true;
				break;
			}
		}

		if (!skip) {
			for (var entry : twoOperands.entrySet()) {
				if (buttonName.equals(entry.getValue())) {
					twoNumbers(entry.getKey(), entry.getValue());
					skip = true;
					break;
				}
			}
		}

		if (!skip) {
			var func = this.func.get(buttonName);
			if (func != null) {
				func.get().run();
				skip = true;
			}
		}

		if (!skip) {
			if (buttonName.matches("[0-9.]")) {
				if (operation == Operation.FINISHED) {
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
			input1 = Double.parseDouble(outputField.getText());
			operation = op;
			var output = engine.getCalculationResult(operation, input1, input2);
			var result = new DecimalFormat("#.###############").format(output);
			outputField.setText(buttons.get(buttonName).getText() + '(' + outputField.getText() + ')' + '=' + result);
			operation = Operation.FINISHED;
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
			input1 = Double.parseDouble(outputField.getText());
			operation = op;
			outputField.setText(outputField.getText() + buttonName);
		} catch (Exception e) {
			outputField.setText("");
		}
	}

	public enum Operation {
		NULL, FINISHED, ARCCOS, ARCCTG, ARCSIN, ARCTG, COS, CTG, DIVIDE, FACTORIAL, LOGARITHM, MINUS, MULTIPLE, PERCENT, PLUS, POWER, SIN, SQRT, TG, SQARE, CUBE, LG, LN, CH, SH, TH, CTH, TEN, BACK, DOUBLEFACT
	}

	public boolean isExtended() {
		return extended;
	}

	public String getOutputText() {
		return outputField.getText();
	}
}