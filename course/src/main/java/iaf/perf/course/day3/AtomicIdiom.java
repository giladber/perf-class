package iaf.perf.course.day3;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicIdiom {
	private final AtomicLong _long = new AtomicLong(0);
	public long getCounter() {
		return _long.getAndIncrement();
	}
	
	public void square() {
		long prev, next;
		do {
			prev = _long.get();
			next = prev * prev;
		} while (!_long.compareAndSet(prev, next));
	}
	
	@SuppressWarnings("unchecked")
	public <V extends Cloneable> void atomicClone(AtomicReference<V> ref) 
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, 
			NoSuchMethodException, SecurityException 
	{
		V base, cloned;
		do {
			base = ref.get();
			Method cloneMethod = Object.class.getMethod("clone");
			cloned = (V) cloneMethod.invoke(base);
		} while (!ref.compareAndSet(base, cloned));
	}
	
	@SuppressWarnings("unchecked")
	public <V extends Cloneable> void easierClone(AtomicReference<V> ref) {
		ref.getAndUpdate(val -> {
			try {
				return (V) Object.class.getMethod("clone").invoke(val);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}
}
