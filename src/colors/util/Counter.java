package colors.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class Counter<T> {
	private static final Random rand = new Random();
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
	
	/**
	 * This is O(n). There's got to be a faster way.
	 * NOTE: Assumes the counter has already been normalized
	 */
	public T sample() {
		final double position = rand.nextDouble();
//		Entry<T,Double>[] entries = (Entry<T, Double>[]) counts.entrySet().toArray();
		double sum = 0.0;
//		for(int i = 0; i < entries.length; i++) {
//			Entry<T,Double> entry = entries[i];
		for(Entry<T,Double> entry : counts.entrySet()) {
			final double thisCount = entry.getValue().doubleValue();
			if(sum >= position || sum+thisCount>=0.999999999)
				return entry.getKey();
			sum += thisCount;
		}
		throw new IllegalArgumentException("Not really an illegal argument, but something went wrong!");
	}
}
