package iaf.perf.course.day3;

import java.util.stream.LongStream;

public class Ex2 {

	public static void main(String[] args) {
		boolean answer = new NaiveCalculator().calculate(100);
		System.out.println(answer);
	}

	public interface Calculator {
		public boolean calculate(long start);
	}

	public static final class NaiveCalculator implements Calculator {

		@Override
		public boolean calculate(long start) {
			if (start == 1) return true;
			long next = start % 2 == 1 ? 3*start+1 : start/2;
			return calculate(next);
		}
	}

	public static final class BetterCalculator implements Calculator {

		@Override
		public boolean calculate(long start) {
			int steps = 0;
			long next = start;
			while (steps++ < 100000) {
				next = (next % 2) == 1 ? 3*next+1 : next / 2;
				if (next == 1) return true;
			}
			return false;
		}
	}

	public static final class NoRecursionCalculator implements Calculator {

		@Override
		public boolean calculate(long start) {
			int steps = 0;
			long next = start;
			while (steps++ < 100000) {
				next = (next & 1) == 1 ? 3*next+1 : next >> 1;
				if (next == 1) return true;
			}
			return false;
		}

	}

	public static final class Java8Calculator implements Calculator {
		
		@Override
		public boolean calculate(long start) {
			return LongStream.
				iterate(start, val -> (val & 1) == 1 ? 3*val+1 : val >> 1).
				anyMatch(val -> val == 1);
		}
	}

}
