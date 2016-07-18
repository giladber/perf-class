package iaf.perf.course.day3.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A naive implementation of the TimedSizableMap interface, based on a HashMap.
 * It is naive since, well, it doesn't work! Can you find out why?
 * @author Gilad Ber
 *
 * @param <K>
 * @param <V>
 */
public class NaiveTimedSizableHashMap<K, V> implements Ex1.TimedSizableMap<K, V> 
{
	private final ScheduledExecutorService removalService = Executors.newScheduledThreadPool(1);
	private final Map<K, V> data = new HashMap<>();
	
	public void put(K key, V value, int duration, TimeUnit unit) {
		data.put(key,  value);
		removalService.schedule(() -> data.remove(key), duration, unit);
	}

	public Optional<V> get(K key) {
		return Optional.ofNullable(data.get(key));
	}

	public long size() {
		return data.size();
	}

	@Override
	public Optional<V> remove(K key) {
		return Optional.ofNullable(data.remove(key));
	}

}
