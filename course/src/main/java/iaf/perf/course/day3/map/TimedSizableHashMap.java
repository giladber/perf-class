package iaf.perf.course.day3.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import iaf.perf.course.day3.map.Ex1.TimedSizableMap;

/**
 * A hash map based implementation of TimedSizable map.
 * 
 * This class is not thread-safe: calling any public operations concurrently will result in program errors.
 * Question: will making the underlying data map a ConcurrentHashMap suffice to make this class thread safe?
 * @author Gilad Ber
 *
 * @param <K> Key type
 * @param <V> Value type
 */
public class TimedSizableHashMap<K, V> implements TimedSizableMap<K, V> 
{
	private final ScheduledExecutorService removalService = Executors.newSingleThreadScheduledExecutor();
	private final Map<K, StampedObject<V>> data;
	private final AtomicLong counter = new AtomicLong(1);
	
	public TimedSizableHashMap() {
		this(new HashMap<>());
	}
	
	protected TimedSizableHashMap(Map<K, StampedObject<V>> map) {
		this.data = map;
	}
	
	public void put(K key, V value, int duration, TimeUnit unit) {
		long mark = counter.getAndIncrement();
		data.put(key, StampedObject.of(value, mark));
		removalService.schedule(() -> tryRemove(key, mark), duration, unit);
	}
	
	private void tryRemove(K key, long mark) {
		if (markEquals(key, mark)) {
			data.remove(key);
		}
	}

	private boolean markEquals(K key, long mark) {
		//alternatively: (pre java8)
		//		if (data.get(key) != null) {
		//			return data.get(key).stamp == mark;
		//		} else {
		//			return false;
		//		}
		
		return Optional.
				ofNullable(data.get(key)).
				map(stamped -> stamped.stamp == mark).
				orElse(false);
		
	}

	public Optional<V> get(K key) {
		//no need to check for data.get(key) != null since put guarantees so. 
		return Optional.ofNullable(data.get(key).obj);
	}

	public long size() {
		return data.size();
	}
	

	@Override
	public Optional<V> remove(K key) {
		return Optional.ofNullable(data.remove(key)).map(so -> so.obj);
	}

	private static final class StampedObject<T> {
		final T obj;
		final long stamp;
		
		private StampedObject(T obj, long stamp) {this.obj = obj; this.stamp = stamp;}
		
		public static <R> StampedObject<R> 
			of(R obj, long stamp) {return new StampedObject<>(obj, stamp);}
	}
		
}
