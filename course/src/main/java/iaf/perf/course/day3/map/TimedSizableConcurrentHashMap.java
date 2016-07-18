package iaf.perf.course.day3.map;

import java.util.concurrent.ConcurrentHashMap;


/**
 * A concurrent version of the timed, sizable hash map.
 * Will this work?
 * @author Gilad Ber
 *
 * @param <K> Type of map's key
 * @param <V> Type of map's value
 */
public class TimedSizableConcurrentHashMap<K, V> extends TimedSizableHashMap<K, V>
{
	public TimedSizableConcurrentHashMap() {
		super(new ConcurrentHashMap<>());
	}

}
