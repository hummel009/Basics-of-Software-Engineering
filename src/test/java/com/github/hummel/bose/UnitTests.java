package com.github.hummel.bose;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitTests {
	private static void testOperation(Calculator calculator, String input1, String operation, String input2, String equals, String expected) {
		calculator.selectButton("C");

		for (var c : input1.toCharArray()) {
			var buttonName = String.valueOf(c);
			calculator.selectButton(buttonName);
		}

		calculator.selectButton(operation);

		if (input2 != null) {
			for (var c : input2.toCharArray()) {
				var buttonName = String.valueOf(c);
				calculator.selectButton(buttonName);
			}
		}

		if (equals != null) {
			calculator.selectButton(equals);
		}

		assertEquals(expected, calculator.getOutputText());
	}

	@Test
	void testAllOperations() {
		var calculator = new Calculator();

		calculator.selectButton("1");
		calculator.selectButton("2");
		calculator.selectButton("3");
		calculator.selectButton("C");
		assertEquals("", calculator.getOutputText());

		testOperation(calculator, "5", "+", "3", "=", "5+3=8");
		testOperation(calculator, "9", "-", "4", "=", "9-4=5");
		testOperation(calculator, "6", "×", "7", "=", "6×7=42");
		testOperation(calculator, "8", "÷", "2", "=", "8÷2=4");

		testOperation(calculator, "100", "%", "15", "=", "100%15=15");

		testOperation(calculator, "9", "√", null, null, "√(9)=3");
		testOperation(calculator, "4", "^", "3", "=", "4^3=64");
		testOperation(calculator, "5", "^2", null, null, "^2(5)=25");
		testOperation(calculator, "2", "^3", null, null, "^3(2)=8");

		calculator.selectButton("π");
		assertEquals("3.141592653589793", calculator.getOutputText());
		calculator.selectButton("C");

		calculator.selectButton("e");
		assertEquals("2.718281828459045", calculator.getOutputText());
		calculator.selectButton("C");

		testOperation(calculator, "4", "1/x", null, null, "1/x(4)=0.25");
		testOperation(calculator, "5", "n!", null, null, "n!(5)=120");
		testOperation(calculator, "7", "n!!", null, null, "n!!(7)=105");
		testOperation(calculator, "2", "log", "5", "=", "2log5=0.430676558073393");

		testOperation(calculator, "30", "sin°", null, null, "sin°(30)=0.5");
		testOperation(calculator, "60", "cos°", null, null, "cos°(60)=0.5");
		testOperation(calculator, "45", "tg°", null, null, "tg°(45)=1");
		testOperation(calculator, "90", "ctg°", null, null, "ctg°(90)=0");

		testOperation(calculator, "0.25", "arcsin", null, null, "arcsin(0.25)=0.252680255142079");
		testOperation(calculator, "0.75", "arccos", null, null, "arccos(0.75)=0.722734247813416");
		testOperation(calculator, "0", "arctg", null, null, "arctg(0)=0");
		testOperation(calculator, "1", "arcctg", null, null, "arcctg(1)=1.273239544735163");

		testOperation(calculator, "2", "10^", null, null, "10^(2)=100");
		testOperation(calculator, "100", "lg", null, null, "lg(100)=2");
		testOperation(calculator, "0", "ln", null, null, "ln(0)=-∞");

		testOperation(calculator, "0", "ch", null, null, "ch(0)=1");
		testOperation(calculator, "0", "sh", null, null, "sh(0)=0");
		testOperation(calculator, "0.5", "th", null, null, "th(0.5)=0.462119785923675");
		testOperation(calculator, "0.5", "cth", null, null, "cth(0.5)=2.163941104580108");

		assertFalse(calculator.isExtended());
		calculator.selectButton("⚙");
		assertTrue(calculator.isExtended());
		calculator.selectButton("⚙");
		assertFalse(calculator.isExtended());
	}
}