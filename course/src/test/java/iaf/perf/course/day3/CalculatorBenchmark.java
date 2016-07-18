package iaf.perf.course.day3; 

import java.io.IOException;

import iaf.perf.course.day3.Ex2.Calculator;

public class CalculatorBenchmark 
{
	
	public static void main(String[] args) throws IOException {
		CalculatorBenchmark bench = new CalculatorBenchmark();
		if (args.length == 0) {
			System.out.println("Please input type: [basic|better|norec|j8]");
			return;
		}
		System.in.read(new byte[1024]);
		Calculator calc = null;
		switch (args[0]) {
		case "basic":
			calc = new Ex2.NaiveCalculator();
			break;
		case "better":
			calc = new Ex2.BetterCalculator();
			break;
		case "norec":
			calc = new Ex2.NoRecursionCalculator();
			break;
		case "j8":
			calc = new Ex2.Java8Calculator();
			break;
		default:
			System.out.println("Illegal input: must be one of [basic|better|norec|j8]");
			return;
		}
		
		System.out.println("Benchmarking type " + args[0]);
		bench.measure(calc);
		System.in.read();
	}
	
	public void measure(Calculator calc) {
		warmup(calc);
		benchmark(calc);
	}
	
	public void warmup(Calculator calc) {
		long st = System.nanoTime();
		for (int i = 2; i < 1_000_000; i++) {
			boolean result = calc.calculate(i);
//			if (!result) System.out.println("false: " + i);
		}
		long en = System.nanoTime();
		System.out.println("Warmup time: " + (en-st)/1E6+"ms");
	}
	
	public void benchmark(Calculator calc) {
		long st = System.nanoTime();
		for (int i = 2; i < 10_000_000; i++) {
			boolean result = calc.calculate(i);
//			if (!result) System.out.println("false: "+ i);
		}
		long en = System.nanoTime();
		System.out.println("Time: " + (en-st)/1E6+"ms");
	}

}

