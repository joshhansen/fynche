package colors.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PartitionedSet<T,P> {
	private final Set<T> all = new HashSet<T>();
	private final Map<P,Set<T>> byRound = new HashMap<P,Set<T>>();
	public void add(final T item, final P partition) {
		all.add(item);
		
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
		return Collections.unmodifiableSet(all);
	}
	
	public Set<T> partition(final P round) {
		Set<T> got = byRound.get(round);
		if(got == null)
			return Collections.emptySet();
		else
			return Collections.unmodifiableSet(got);
	}
}