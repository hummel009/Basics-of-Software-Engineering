package com.github.hummel.bose;

import java.util.EnumMap;
import java.util.Map;

@SuppressWarnings("Convert2MethodRef")
public class Engine {
	private final Map<Calculator.Operation, Applier> engine = new EnumMap<>(Calculator.Operation.class);

	public Engine() {
		engine.put(Calculator.Operation.PLUS, (operand1, operand2) -> operand1 + operand2);
		engine.put(Calculator.Operation.MINUS, (operand1, operand2) -> operand1 - operand2);
		engine.put(Calculator.Operation.MULTIPLE, (operand1, operand2) -> operand1 * operand2);
		engine.put(Calculator.Operation.ARCSIN, (operand1, operand2) -> StrictMath.asin(operand1));
		engine.put(Calculator.Operation.ARCCOS, (operand1, operand2) -> StrictMath.acos(operand1));
		engine.put(Calculator.Operation.ARCTG, (operand1, operand2) -> StrictMath.atan(operand1));
		engine.put(Calculator.Operation.ARCCTG, (operand1, operand2) -> 1 / StrictMath.atan(operand1));
		engine.put(Calculator.Operation.SIN, (operand1, operand2) -> StrictMath.sin(StrictMath.toRadians(operand1)));
		engine.put(Calculator.Operation.COS, (operand1, operand2) -> StrictMath.cos(StrictMath.toRadians(operand1)));
		engine.put(Calculator.Operation.TG, (operand1, operand2) -> StrictMath.tan(StrictMath.toRadians(operand1)));
		engine.put(Calculator.Operation.CTG, (operand1, operand2) -> 1 / StrictMath.tan(StrictMath.toRadians(operand1)));
		engine.put(Calculator.Operation.SQRT, (operand1, operand2) -> StrictMath.sqrt(operand1));
		engine.put(Calculator.Operation.LOGARITHM, (operand1, operand2) -> StrictMath.log10(operand1) / StrictMath.log10(operand2));
		engine.put(Calculator.Operation.POWER, (operand1, operand2) -> StrictMath.pow(operand1, operand2));
		engine.put(Calculator.Operation.DIVIDE, (operand1, operand2) -> operand1 / operand2);
		engine.put(Calculator.Operation.PERCENT, (operand1, operand2) -> operand2 * operand1 / 100);
		engine.put(Calculator.Operation.SQARE, (operand1, operand2) -> operand1 * operand1);
		engine.put(Calculator.Operation.CUBE, (operand1, operand2) -> StrictMath.pow(operand1, 3));
		engine.put(Calculator.Operation.LG, (operand1, operand2) -> StrictMath.log10(operand1));
		engine.put(Calculator.Operation.LN, (operand1, operand2) -> StrictMath.log(operand1));
		engine.put(Calculator.Operation.CH, (operand1, operand2) -> (StrictMath.pow(2.7183, operand1) + StrictMath.pow(2.7183, -1 * operand1)) / 2);
		engine.put(Calculator.Operation.SH, (operand1, operand2) -> (StrictMath.pow(2.7183, operand1) - StrictMath.pow(2.7183, -1 * operand1)) / 2);
		engine.put(Calculator.Operation.TH, (operand1, operand2) -> (StrictMath.pow(2.7183, operand1) - StrictMath.pow(2.7183, -1 * operand1)) / (StrictMath.pow(2.7183, operand1) + StrictMath.pow(2.7183, -1 * operand1)));
		engine.put(Calculator.Operation.CTH, (operand1, operand2) -> (StrictMath.pow(2.7183, operand1) + StrictMath.pow(2.7183, -1 * operand1)) / (StrictMath.pow(2.7183, operand1) - StrictMath.pow(2.7183, -1 * operand1)));
		engine.put(Calculator.Operation.TEN, (operand1, operand2) -> StrictMath.pow(10, operand1));
		engine.put(Calculator.Operation.BACK, (operand1, operand2) -> 1 / operand1);
		engine.put(Calculator.Operation.NULL, (operand1, operand2) -> operand2);
		engine.put(Calculator.Operation.FACTORIAL, (operand1, operand2) -> {
			var result = 1L;
			for (var k = StrictMath.round(operand1); k > 0; k -= 1) {
				result *= k;
			}
			return result;
		});
		engine.put(Calculator.Operation.DOUBLEFACT, (operand1, operand2) -> {
			var result = 1L;
			for (var k = StrictMath.round(operand1); k > 0; k -= 2) {
				result *= k;
			}
			return result;
		});
	}

	public double getCalculationResult(Calculator.Operation operation, double operand1, double operand2) {
		return engine.get(operation).applyOn(operand1, operand2);
	}

	public interface Applier {
		double applyOn(double operand1, double operand2);
	}
}