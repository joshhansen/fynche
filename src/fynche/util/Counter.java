/*
 * Fynche - a Framework for Multiagent Computational Creativity
 * Copyright 2011 Josh Hansen
 * 
 * This file is part of the Fynche <https://github.com/joshhansen/fynche>.
 * 
 * Fynche is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Fynche is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with Fynche.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * If you have inquiries regarding any further use of Fynche, please
 * contact Josh Hansen <http://joshhansen.net/>
 */
package fynche.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class Counter<T> {
	private Map<T,Double> counts = new HashMap<T,Double>();
	
	public Counter() {
		//do nothing
	}
	
	public Counter(final Counter<T> copyMe) {
		for(Entry<T,Double> entry : copyMe.entrySet())
			counts.put(entry.getKey(), entry.getValue());
	}
	
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
			counts.put(item, counts.get(item).doubleValue()/denom);
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
