package colors;

import java.util.HashMap;
import java.util.Map;

public class Counter<T> {
	private Map<T,Double> counts = new HashMap<T,Double>();
	
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
}
