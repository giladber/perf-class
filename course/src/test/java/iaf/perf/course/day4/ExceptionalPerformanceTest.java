package iaf.perf.course.day4;

public class ExceptionalPerformanceTest {
	
	private static final int ITERATIONS = 10_000_000;
	
	public static void main(String[] args) {
		byte[][] ins = new byte[ITERATIONS][5];
		
		long start = System.nanoTime();
		new ExceptionalPerformance.StandardComputer().compute(ins);
		long end = System.nanoTime();
		
		System.out.println("Warmup time: " + (end-start)/1E6+"ms");
		System.gc();
		
		start = System.nanoTime();
		new ExceptionalPerformance.StandardComputer().compute(ins);
		end = System.nanoTime();
		
		System.out.println("Time: " + (end-start)/1E6+"ms");
	}
}
