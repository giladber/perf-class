package iaf.perf.course.day3.map;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Ex1 
{

	/**
	 * 
	 * @author Gilad Ber
	 * A time-based map, to which elements may be added for a maximal time amount.
	 * @param <K> Type of the map's key
	 * @param <V> Type of the map's value
	 */
	public interface TimedMap<K, V> {
		
		/**
		 * Adds the input (key, value) pair to the map, to be removed after at most 
		 * duration time units (i.e. 500 milliseconds).
		 * 
		 * The key is promised to be removed after this amount of time unless an additional
		 * put operation had been performed before the time has passed.
		 * 
		 * Results of concurrent put operations are implementation dependent.
		 * 
		 * @param key Key to insert into the map
		 * @param value Value to insert into the map
		 * @param duration Numeric duration for which to add the key,value pair
		 * @param unit Time unit relative to the numeric duration 
		 */
		public void put(K key, V value, int duration, TimeUnit unit);

		/**
		 * Get the value corresponding to the input key.
		 * 
		 * @param key Key whose value is to be returned
		 * @return An optional capturing the key's corresponding value,
		 * or Optional.EMPTY if the key does not exist in the map.
		 */
		public Optional<V> get(K key);
		
		/**
		 * Remove the input key and its corresponding value from the map.
		 * @param key Key whose value we wish to remove from the map.
		 * @return The removed value (with similar semantics to get).
		 */
		public Optional<V> remove(K key);
	}
	
	/**
	 * Represents a collection which offers a way to query the amount of its
	 * underlying elements.
	 * @author Gilad Ber
	 *
	 */
	public interface Sizable {
		public long size();
	}
	
	public interface TimedSizableMap<K, V> extends TimedMap<K, V>, Sizable {}
}

