package iaf.perf.course.day4;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import iaf.perf.course.day3.map.TimedSizableHashMap;
import iaf.perf.course.day3.map.Ex1.TimedSizableMap;

public class Profiling {

	public static void main(String[] args) {
		new Profiling().foo();
	}
	
	private final TimedSizableMap<Long, byte[]> map = new TimedSizableHashMap<>();
	private final AtomicLong counter = new AtomicLong();
	
	private void foo() {
		int count = 0;
		do {
			count += (bar() == 1 ? 1 : 0);
		} while (count < 1000);
		
		System.out.println("done!");
	}

	public int bar() {
		byte[] b, result = new byte[1 << 22];
		System.arraycopy(b = baz(), 0, result, 0, b.length);
		map.put(counter.getAndIncrement(), result, 5, TimeUnit.SECONDS);
		return result[1];
	}

	private byte[] baz = new byte[1 << 5];
	private byte[] baz() {
		ThreadLocalRandom.current().nextBytes(baz);
		return baz;
	}

}
