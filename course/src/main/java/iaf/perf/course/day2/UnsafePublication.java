package iaf.perf.course.day2;


public class UnsafePublication {
	private static Holder holder;
	
	private static final Runnable r1 = () -> {
		holder = new Holder(1_1);
	};
	
	private static final Runnable r2 = () -> {
		while (holder == null) {}
		holder.insanity();
	};
	
	public static void main(String[] args) throws InterruptedException {
		new Thread(r2).start();
		new Thread(r1).start();
	}
	
	private static final class Holder {
		private int n;
		public Holder(int n) {
			this.n = n;
		}
		
		public void insanity() {
			if (n != n) {
				throw new AssertionError("WTF Java?!");
			}
		}
	}
}
