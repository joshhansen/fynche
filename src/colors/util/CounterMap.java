package colors.util;

import java.util.HashMap;
import java.util.Map;

public class CounterMap<K, V> {
	private final Map<K,Counter<V>> counters = new HashMap<K,Counter<V>>();
	
	public Counter<V> getCounter(final K key) {
		Counter<V> counter = counters.get(key);
		if(counter == null) {
			counter = new Counter<V>();
			counters.put(key, counter);
		}
		return counter;
	}
	
	public void normalize() {
		for(Counter<V> counter : counters.values()) {
			counter.normalize();
		}
	}
	
	public double getCount(final K key, final V value) {
		return getCounter(key).getCount(value);
	}
	
	public void setCounter(final K key, final Counter<V> counter) {
		counters.put(key, counter);
	}
	
	public void setCount(final K key, final V value, final double count) {
		getCounter(key).setCount(value, count);
	}
	
	public boolean containsKey(final K key) {
		return counters.containsKey(key);
	}
}
