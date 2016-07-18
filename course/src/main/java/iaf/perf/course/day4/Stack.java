package iaf.perf.course.day4;

import java.util.Optional;

public class Stack<V> {
	private int index;
	private V[] data;
	
	@SuppressWarnings("unchecked")
	public Stack(int size) {
		int power2size = 1 << (32 - Integer.numberOfLeadingZeros(size - 1));
		data = (V[]) new Object[power2size];
	}
	
	public Optional<V> pop() {
		return Optional.ofNullable(data[index--]);
	}
	
	public void put(V v) {
		ensureCapacity();
		data[index++] = v;
	}

	@SuppressWarnings("unchecked")
	private void ensureCapacity() {
		if (index == data.length) {
			int newSize = 2 * data.length; 
			Object[] resized = new Object[newSize];
			System.arraycopy(data, 0, resized, 0, data.length);
			data = (V[]) resized;
		}
	}
	
	public int size() {
		return index + 1;
	}
}
