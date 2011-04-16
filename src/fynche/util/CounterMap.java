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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
	
	public Set<Entry<K,Counter<V>>> entrySet() {
		return counters.entrySet();
	}
	
	public Set<K> keySet() {
		return counters.keySet();
	}
	
	public double totalCount() {
		double sum = 0.0;
		for(Counter<V> counter : counters.values()) {
			sum += counter.totalCount();
		}
		return sum;
	}
}
