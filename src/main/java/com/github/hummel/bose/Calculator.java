package com.github.hummel.bose;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Supplier;

public class Calculator extends JFrame {
	private final Map<String, JButton> buttons = new HashMap<>();
	private final Collection<JButton> buttonsExtendedMode = new ArrayList<>();
	private final JPanel panel = new JPanel();

	private final Map<Operation, Supplier<Double>> engine = new EnumMap<>(Operation.class);
	private final Map<String, Supplier<Runnable>> func = new HashMap<>();

	private final Map<Operation, String> opeOperand = new EnumMap<>(Operation.class);
	private final Map<Operation, String> twoOperands = new EnumMap<>(Operation.class);

	private final JLabel outputField = new JLabel();
	private Operation operation;
	private double input1;
	private double input2;
	private int notInclude;

	private boolean extended;

	public Calculator() {
		setTitle("Hummel009's Calculator");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Locale.setDefault(Locale.US);

		panel.setLayout(new GridLayout(7, 4));

		registerButton("C", "C", false);
		registerButton("e", "e", false);
		registerButton("π", "π", false);
		registerButton("÷", "÷", false);

		registerButton("7", "7", false);
		registerButton("8", "8", false);
		registerButton("9", "9", false);
		registerButton("×", "×", false);

		registerButton("4", "4", false);
		registerButton("5", "5", false);
		registerButton("6", "6", false);
		registerButton("-", "-", false);

		registerButton("1", "1", false);
		registerButton("2", "2", false);
		registerButton("3", "3", false);
		registerButton("+", "+", false);

		registerButton("%", "%", false);
		registerButton("0", "0", false);
		registerButton(".", ".", false);
		registerButton("=", "=", false);

		registerButton("√", "√", false);
		registerButton("^", "^", false);
		registerButton("^2", "^2", false);
		registerButton("^3", "^3", false);

		registerButton("log", "log", true);
		registerButton("sin°", "sin°", true);
		registerButton("cos°", "cos°", true);
		registerButton("tg°", "tg°", true);
		registerButton("ctg°", "ctg°", true);
		registerButton("arcsin", "arcsin", true);
		registerButton("arccos", "arccos", true);
		registerButton("arctg", "arctg", true);
		registerButton("arcctg", "arcctg", true);
		registerButton("10^", "10^", true);
		registerButton("lg", "lg", true);
		registerButton("ln", "ln", true);
		registerButton("ch", "ch", true);
		registerButton("sh", "sh", true);
		registerButton("th", "th", true);
		registerButton("cth", "cth", true);

		registerButton("1/x", "1/x", false);
		registerButton("n!", "n!", false);
		registerButton("n!!", "n!!", false);

		registerButton("⚙", "⚙", false);

		outputField.setFont(outputField.getFont().deriveFont(20.0f));
		outputField.setHorizontalAlignment(SwingConstants.RIGHT);
		outputField.setPreferredSize(new Dimension(outputField.getWidth(), outputField.getFontMetrics(outputField.getFont()).getHeight()));

		operation = Operation.NULL;

		engine.put(Operation.PLUS, () -> input1 + input2);
		engine.put(Operation.MINUS, () -> input1 - input2);
		engine.put(Operation.MULTIPLE, () -> input1 * input2);
		engine.put(Operation.ARCSIN, () -> StrictMath.asin(input1));
		engine.put(Operation.ARCCOS, () -> StrictMath.acos(input1));
		engine.put(Operation.ARCTG, () -> StrictMath.atan(input1));
		engine.put(Operation.ARCCTG, () -> 1 / StrictMath.atan(input1));
		engine.put(Operation.SIN, () -> StrictMath.sin(StrictMath.toRadians(input1)));
		engine.put(Operation.COS, () -> StrictMath.cos(StrictMath.toRadians(input1)));
		engine.put(Operation.TG, () -> StrictMath.tan(StrictMath.toRadians(input1)));
		engine.put(Operation.CTG, () -> 1 / StrictMath.tan(StrictMath.toRadians(input1)));
		engine.put(Operation.SQRT, () -> StrictMath.sqrt(input1));
		engine.put(Operation.LOGARITHM, () -> StrictMath.log10(input1) / StrictMath.log10(input2));
		engine.put(Operation.POWER, () -> StrictMath.pow(input1, input2));
		engine.put(Operation.DIVIDE, () -> input1 / input2);
		engine.put(Operation.PERCENT, () -> input2 * input1 / 100);
		engine.put(Operation.SQARE, () -> input1 * input1);
		engine.put(Operation.CUBE, () -> StrictMath.pow(input1, 3));
		engine.put(Operation.LG, () -> StrictMath.log10(input1));
		engine.put(Operation.LN, () -> StrictMath.log(input1));
		engine.put(Operation.CH, () -> (StrictMath.pow(2.7183, input1) + StrictMath.pow(2.7183, -1 * input1)) / 2);
		engine.put(Operation.SH, () -> (StrictMath.pow(2.7183, input1) - StrictMath.pow(2.7183, -1 * input1)) / 2);
		engine.put(Operation.TH, () -> (StrictMath.pow(2.7183, input1) - StrictMath.pow(2.7183, -1 * input1)) / (StrictMath.pow(2.7183, input1) + StrictMath.pow(2.7183, -1 * input1)));
		engine.put(Operation.CTH, () -> (StrictMath.pow(2.7183, input1) + StrictMath.pow(2.7183, -1 * input1)) / (StrictMath.pow(2.7183, input1) - StrictMath.pow(2.7183, -1 * input1)));
		engine.put(Operation.TEN, () -> StrictMath.pow(10, input1));
		engine.put(Operation.BACK, () -> 1 / input1);
		engine.put(Operation.NULL, () -> input2);
		engine.put(Operation.DOUBLEFACT, () -> {
			var result = 1L;
			for (var k = StrictMath.round(input1); k > 0; k -= 2) {
				result *= k;
			}
			return (double) result;
		});
		engine.put(Operation.FACTORIAL, () -> {
			var result = 1L;
			for (var k = StrictMath.round(input1); k > 0; k -= 1) {
				result *= k;
			}
			return (double) result;
		});

		opeOperand.put(Operation.FACTORIAL, "n!");
		opeOperand.put(Operation.DOUBLEFACT, "n!!");
		opeOperand.put(Operation.SQRT, "√");
		opeOperand.put(Operation.SIN, "sin°");
		opeOperand.put(Operation.COS, "cos°");
		opeOperand.put(Operation.TG, "tg°");
		opeOperand.put(Operation.CTG, "ctg°");
		opeOperand.put(Operation.ARCSIN, "arcsin");
		opeOperand.put(Operation.ARCCOS, "arccos");
		opeOperand.put(Operation.ARCTG, "arctg");
		opeOperand.put(Operation.ARCCTG, "arcctg");
		opeOperand.put(Operation.SQARE, "^2");
		opeOperand.put(Operation.CUBE, "^3");
		opeOperand.put(Operation.LG, "lg");
		opeOperand.put(Operation.LN, "ln");
		opeOperand.put(Operation.CH, "ch");
		opeOperand.put(Operation.SH, "sh");
		opeOperand.put(Operation.TH, "th");
		opeOperand.put(Operation.CTH, "cth");
		opeOperand.put(Operation.TEN, "10^");
		opeOperand.put(Operation.BACK, "1/x");

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
				buttonsExtendedMode.forEach(panel::remove);
			} else {
				extended = true;
				panel.setLayout(new GridLayout(11, 4));
				panel.remove(buttons.get("⚙"));
				//noinspection StreamToLoop
				buttonsExtendedMode.forEach(panel::add);
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
				var output = calculate();
				var result = new DecimalFormat("#.###############").format(output);
				outputField.setText(outputField.getText() + '=' + result);
				operation = Operation.FINISHED;
			} catch (Exception e) {
				outputField.setText("");
			}
		});
		func.put("e", () -> () -> outputField.setText("2.718281828459045"));
		func.put("π", () -> () -> outputField.setText("3.141592653589793"));

		add(outputField, BorderLayout.PAGE_START);
		add(panel, BorderLayout.CENTER);
		setSize(450, 750);
		setLocationRelativeTo(null);
	}

	private double calculate() {
		return engine.get(operation).get();
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
			var output = calculate();
			var result = new DecimalFormat("#.###############").format(output);
			outputField.setText(buttons.get(buttonName).getText() + '(' + outputField.getText() + ')' + '=' + result);
			operation = Operation.FINISHED;
		} catch (Exception e) {
			outputField.setText("");
		}
	}

	public void selectButton(String buttonName) {
		var skip = false;

		for (var entry : opeOperand.entrySet()) {
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

	private void registerButton(String name, String text, boolean hidden) {
		var button = new JButton();
		button.setFont(button.getFont().deriveFont(20.0f));
		button.addActionListener(event -> selectButton(((AbstractButton) event.getSource()).getText()));
		button.setText(text);
		if (hidden) {
			buttonsExtendedMode.add(button);
		} else {
			panel.add(button);
		}
		buttons.put(name, button);
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