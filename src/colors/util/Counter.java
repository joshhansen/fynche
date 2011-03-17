package colors.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

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
	
	public Counter<T> normalize() {
		final double denom = totalCount();
		for(T item : counts.keySet()) {
			counts.put(item, counts.get(item)/denom);
		}
		return this;
	}
	
	/**
	 * This is O(n). There's got to be a faster way.
	 */
	public T sample() {
		if(counts.isEmpty()) throw new IllegalArgumentException("Counter must have counts in order to be sampled.");
		final double total = totalCount();
		final double position = Rand.nextDouble();
		double sum = 0.0;
		for(Entry<T,Double> entry : counts.entrySet()) {
			final double thisCount = entry.getValue().doubleValue() / total;
			if(sum >= position || sum+thisCount>=0.999999999)
				return entry.getKey();
			sum += thisCount;
		}
		throw new IllegalArgumentException("Not really an illegal argument, but something went wrong!");
	}

	public double getLogCount(T item) {
		final double count = getCount(item);
		if(count == 0.0) return 0.0;
		return Math.log(count);
	}

	public void setCount(T item, double weight) {
		counts.put(item, weight);
	}
	
	public Double remove(T item) {
		return counts.remove(item);
	}

	public Set<T> keySet() {
		return counts.keySet();
	}

	public boolean isEmpty() {
		return counts.isEmpty();
	}

	public Set<Entry<T, Double>> entrySet() {
		return counts.entrySet();
	}
	
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		
		for(Entry<T,Double> entry : entrySet()) {
			sb.append(entry.getKey().toString());
			sb.append(":");
			sb.append(entry.getValue());
			sb.append(' ');
		}
		
		return sb.toString().trim();
	}
}
