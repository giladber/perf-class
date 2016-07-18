package iaf.perf.course.day3;

import java.util.concurrent.ThreadLocalRandom;

public class Garbage {

	public long trash() {
		long base = ThreadLocalRandom.current().nextLong();
		long second = new Long(Long.highestOneBit(base) ^ 0xff);
		long third = Long.bitCount(new Long(second)) << 2;
		return new Long(third ^ second);
	}
	
	public String moreTrash() {
		String s1 = new String("Don't do this!");
		String s2 = "Do this instead";
		String s3 = new String(" :( ");
		String s4 = new String("Don't do this either! " + s1 + s2 + s3);
		return new String(s4).intern(); //no! Interning is bad! :(
	}
	
}
