package iaf.perf.course.day3;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.junit.Test;

import iaf.perf.course.day3.map.TimedSizableConcurrentHashMap;
import iaf.perf.course.day3.map.Ex1.TimedSizableMap;

public class TimedSizableConcurrentMapTest
{
	private static final int MAP_SIZE = 150000;
	private static final int NUM_PARTIES = 4;
	private final CyclicBarrier _bar = new CyclicBarrier(NUM_PARTIES);
	private final TimedSizableMap<Integer, Object> map = new TimedSizableConcurrentHashMap<>();
	
	private final Runnable PUT_ACTION = () -> {
		try {
			_bar.await();
			IntStream.range(0, MAP_SIZE).
			forEach(x -> 
			{
				map.put(x, new Object(), 5, TimeUnit.MILLISECONDS);	
			});
		} catch (BrokenBarrierException | InterruptedException ex)
		{
			ex.printStackTrace();
		}
	};
	
	@Test
	public void testConcurrentPut() throws InterruptedException
	{
		final ExecutorService _s = Executors.newCachedThreadPool();
		_bar.reset();
		
		for(int i = 0; i < NUM_PARTIES; i++)
		{
			_s.submit(PUT_ACTION);
		}
		
		Thread.sleep(10000);
		assertEquals(0, map.size());
	}

}
