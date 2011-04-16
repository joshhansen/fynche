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

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PartitionedSet<T,P> {
//	private final Set<T> all = new HashSet<T>();
	private final Map<P,Set<T>> byRound = new HashMap<P,Set<T>>();
	public void add(final T item, final P partition) {
//		all.add(item);
		
		Set<T> set = byRound.get(partition);
		if(set == null) {
			set = new HashSet<T>();
			byRound.put(partition, set);
		}
		set.add(item);
	}
	
	public void add(final Collection<T> items, final P partition) {
		for(T item : items) {
			add(item, partition);
		}
	}
	
	public Set<T> all() {
		return new AbstractSet<T>(){
			@Override
			public Iterator<T> iterator() {
				return new Iterator<T>(){
					Iterator<Set<T>> partIt = byRound.values().iterator();
					Iterator<T> it = null;
					
					@Override
					public boolean hasNext() {
						if(it != null && it.hasNext()) return true;
						if(partIt.hasNext()) return true;
						return false;
					}
					
					private void refreshIt() {
						if(it == null || (!it.hasNext() && partIt.hasNext()))
							it = partIt.next().iterator();
					}

					@Override
					public T next() {
						refreshIt();
						return it.next();
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}

			@Override
			public int size() {
				int sum = 0;
				for(Set<T> partition : byRound.values())
					sum += partition.size();
				return sum;
			}
		};
	}
	
	public Set<T> partition(final P round) {
		Set<T> got = byRound.get(round);
		if(got == null)
			return Collections.emptySet();
		else
			return Collections.unmodifiableSet(got);
	}
}