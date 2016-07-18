package iaf.perf.course.day2;


public class UnsafePublication {
	public static void main(String[] args) throws InterruptedException {
		new UnsafePublication();
	}
	
	private final Object obj;
	
	public UnsafePublication() throws InterruptedException {
		final Observer observer = new Observer();
		observer.addListener(this);
		obj = new Object();
		observer.event();
	}
	
	private static final class Observer {
		Object myObj;
		public void addListener(UnsafePublication x) {
			myObj = x.obj;
		}
		
		public void event() {
			//some time later, and we have no idea why myObj is null! even though it is
			//final, and x.obj is obviously not null
			System.out.println(myObj.toString());
		}
	}
}
