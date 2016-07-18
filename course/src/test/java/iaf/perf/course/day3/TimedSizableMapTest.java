package iaf.perf.course.day3;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import iaf.perf.course.day3.map.Ex1;
import iaf.perf.course.day3.map.TimedSizableHashMap;

public class TimedSizableMapTest 
{

	private Ex1.TimedSizableMap<Long, Object> map = new TimedSizableHashMap<>();
	
	@Test
	public void testSize()
	{
		assertEquals(map.size(), 0);
		map.put(1L, new Object(), 500, TimeUnit.SECONDS);
		assertEquals(map.size(), 1);
	}
	
	@Test public void testTimelyRemoval() throws InterruptedException {
		map.put(1L, new Object(), 1, TimeUnit.SECONDS);
		assertEquals(map.size(), 1);
		Thread.sleep(1500);
		assertEquals(map.size(), 0);
	}
	
	@Test public void testRemove() {
		map.put(1L, new Object(), 1, TimeUnit.SECONDS);
		map.remove(1L);
		assertEquals(map.size(), 0);
		map.put(1L, new Object(), 1, TimeUnit.SECONDS);
		map.put(2L, new Object(), 1, TimeUnit.SECONDS);
		map.remove(1L);
		assertEquals(map.size(), 1);
	}
	
	@Test public void testIRI() throws InterruptedException {
		Object obj = new Object();
		map.put(1L, obj, 1, TimeUnit.SECONDS);
		assertEquals(map.size(), 1);
		map.remove(1L);
		assertEquals(map.size(), 0);
		map.put(1L, obj, 3, TimeUnit.SECONDS);
		assertEquals(map.size(), 1);
		Thread.sleep(1500);
		assertEquals(1, map.size());
		Thread.sleep(2000);
		assertEquals(0, map.size());
	}
	
}
