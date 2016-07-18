package iaf.perf.course.day3;

public class BrokenSingleton 
{
	private static Object lock = new Object();
	private static BrokenSingleton _instance;
	private BrokenSingleton() {/*Expensive startup code*/}
	
	public static BrokenSingleton get() {
		if (_instance == null) {
			synchronized(lock) {
				if (_instance == null) {
					_instance = new BrokenSingleton();
				}
			}
		}
		return _instance;
	}
	
	public enum CorrectSingleton {
		INSTANCE {
			public void doStuff() {
				System.out.println("Blah blah blah");
			}
		};
	}
}