package colors.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Counter<T> {
	private Map<T,Double> counts = new HashMap<T,Double>();
	
	public void increment(T item) {
		increment(item, 1.0);
	}
	
	public void incrementAll(Collection<T> items) {
		for(T item : items) {
			increment(item);
		}
	}
	
	public void increment(T item, double inc) {
		Double count = counts.get(item);
		if(count == null) {
			count = inc;
		} else {
			count += inc;
		}
		counts.put(item, count);
	}
	
	public double getCount(final T item) {
		Double count = counts.get(item);
		if(count == null)
			return 0.0;
		else
			return count;
	}
	
	public double totalCount() {
		double sum = 0.0;
		for(T item : counts.keySet()) {
			sum += counts.get(item);
		}
		return sum;
	}
	
	public void normalize() {
		final double denom = totalCount();
		for(T item : counts.keySet()) {
			counts.put(item, counts.get(item)/denom);
		}
	}
}
