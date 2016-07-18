package iaf.perf.course.day2;

/**
 * To see (compiler-based) dead code elimination in action, compile and then 
 * print the bytecode for this class:
 * javac DeadCodeElimination.java
 * javap -v -l DeadCodeElimination.class
 * @author giladrber
 *
 */
public class DeadCodeElimination {
	private static final boolean DEBUG = false;
	
	void doStuff() {
		if (DEBUG) {
			do1();
		}
		else {
			do2();
		}
	}

	private void do1() {
		System.out.println("1");
	}

	private void do2() {
		System.out.println("2");
	}
	
	
}
