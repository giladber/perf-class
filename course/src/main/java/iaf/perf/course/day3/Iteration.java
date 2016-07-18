package iaf.perf.course.day3;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import sun.misc.Contended;

public class Iteration {

	private final int[] prim = new int[SIZE];
	private final Integer[] obj = new Integer[SIZE];
	
	/*
	 * An aggressively-optimizing JIT may note that other than iterating
	 * over the arrays, we make no visible side-effects to the state of the program.
	 * This could cause it to eliminate all of the loop, making us actually measure
	 * nothing.
	 * 
	 * There is no real reason to use the @Contended annotation here other
	 * than for an educational example of how to use it, since there is no 
	 * concurrent access to either of these fields (other than when filling
	 * the arrays with dummy data).
	 * 
	 * Note that since iteration is *very* quick, it may very well be that 
	 * the call to System.nanoTime() dominates the benchmark time, making us 
	 * measure the totally wrong stuff.
	 * 
	 * This is part of why we need JMH :)
	 */
	@Contended private volatile int intermediatePrim;
	@Contended private volatile Integer intermediateObj;
	
	private static final int SIZE = 1_000_000;
	private static final int WARMUP_ITERATIONS = 100;
	private static final int ITERATIONS = 500;
	
	public static void main(String[] args) {
		new Iteration().benchmark();
	}
	
	public void benchmark() {
		putData();
		warmUp();
		
		intermediatePrim = 0;
		intermediateObj = 0;
		
		measure();
	}

	private void measure() {
		System.gc();
		long s1 = System.nanoTime();
		for (int iter = 0; iter < ITERATIONS; iter++) {
			for (int i = 0; i < SIZE; i++) {
				intermediatePrim = prim[i];
			}
		}
		long e1 = System.nanoTime();
		
		System.gc();
		long s2 = System.nanoTime();
		for (int iter = 0; iter < ITERATIONS; iter++) {
			for (int i = 0; i < SIZE; i++) {
				intermediateObj = obj[i];
			}
		}
		long e2 = System.nanoTime();
		
		System.out.println("t1="+(e1-s1)/1E9+"ms, t2="+(e2-s2)/1E6 + "ms");
	}

	private void warmUp() 
	{
		System.out.println("Beginning warmup");
		long s = System.nanoTime();
		for (int x = 0; x < WARMUP_ITERATIONS; x++) {
			IntStream.range(0, SIZE - 1).
				forEach(idx -> {
					intermediatePrim = prim[idx];
					intermediateObj = obj[idx];
			});
		}
		long e = System.nanoTime();
		System.out.println("Time to warm up: " + (e-s)/1E6 + "ms");
	}
	
	public int getIntermediatePrim() {
		return intermediatePrim;
	}
	
	public Integer getIntermediateObj() {
		return intermediateObj;
	}

	private void putData() {
		final ThreadLocalRandom rnd = ThreadLocalRandom.current();
		IntStream.range(0, SIZE - 1).
			parallel().
			mapToObj(x -> new IntTriplet(x, rnd.nextInt(), rnd.nextInt())).
			forEach(o -> {
				int idx = o._1;
				prim[idx] = o._2;
				obj[idx] = o._3;
			});
	}
	
	private static final class IntTriplet {
		final int _1;
		final int _2;
		final int _3;
		IntTriplet(int _1, int _2, int _3) {
			this._1 = _1;
			this._2 = _2;
			this._3 = _3;
		}
	}
	
}
