package iaf.perf.course.day2;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sun.misc.Contended;

@SuppressWarnings("restriction")
public class FalseSharing {

	private static final int NUM_THREADS = 2;
	static final long WARMUP_ITER = 10_000_000;
	public static final long BENCH_ITER = 1_000_000_000;
	
	public static void main(String[] args) {
		
		if (args[0].equals("pad")) {
			System.out.println("Running padded version");
			PaddedLongs longs = new PaddedLongs();
			warmUp(longs);
			benchmark(longs);
		}
		else if (args[0].equals("long")) {
			System.out.println("Running regular version");
			ContendedLongs longs = new ContendedLongs();
			warmUp(longs);
			benchmark(longs);
		}
		else {
			System.out.println("Bad input, must be either pad or long");
		}
	}
	
	private static void warmUp(Longs longs) 
	{
		long s = System.nanoTime();
		for (long i = 0; i < WARMUP_ITER; i++) {
			longs.set1(i);
			longs.set2(i);
		}
		long e = System.nanoTime();
		System.out.println("Warm up time: " + (e-s)/1E6+"ms");
	}

	private static void benchmark(final Longs longs) {
		ExecutorService es = Executors.newFixedThreadPool(NUM_THREADS);
		final CyclicBarrier bar = new CyclicBarrier(NUM_THREADS + 1);
		final CyclicBarrier endBar = new CyclicBarrier(NUM_THREADS + 1);
		
		es.submit(() -> {
			tryWait(bar);
			for (long i = 0; i < BENCH_ITER; i++) {
				longs.set1(42);
			}
			tryWait(endBar);
		});
		
		es.submit(() -> {
			tryWait(bar);
			for (long i = 0; i < BENCH_ITER; i++) {
				longs.set2(43);
			}
			tryWait(endBar);
		});
		
		tryWait(bar);
		long start = System.nanoTime();
		tryWait(endBar);
		long end = System.nanoTime();
		es.shutdown();
		System.out.println("Time: " + (end-start)/1E6 + "ms");
	}

	private static void tryWait(final CyclicBarrier bar) {
		try {
			bar.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public long get(ContendedLongs cl) {
		return cl.get1() + cl.get2();
	}

	interface Longs {
		void set1(final long v);
		void set2(final long v);
	}
	
	private static final class ContendedLongs implements Longs {
		volatile long _1;
		volatile long _2;
		
		@Override
		public void set1(long v) {
			_1 = v;
		}
		
		@Override
		public void set2(long v) {
			_2 = v;
		}	
		
		public long get1() {return _1;}
		public long get2() {return _2;}
	}
	
	private static final class PaddedLongs implements Longs {
		@Contended volatile long _1;
		@Contended volatile long _2;
		
		@Override
		public void set1(long v) {
			_1 = v;
		}
		
		@Override
		public void set2(long v) {
			_2 = v;
		}
	}
	
}
