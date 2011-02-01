package colors.agents;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import colors.Artefact;
import colors.Rating;

public abstract class AbstractAgent implements Agent {
	private static final long serialVersionUID = 1L;
	
	private static class PartitionedSet<T,P> {
		private final Set<T> all = new HashSet<T>();
		private final Map<P,Set<T>> byRound = new HashMap<P,Set<T>>();
		public void add(final T artefact, final P round) {
			all.add(artefact);
			
			Set<T> set = byRound.get(round);
			if(set == null) {
				set = new HashSet<T>();
				byRound.put(round, set);
			}
			set.add(artefact);
		}
		
		public Set<T> all() {
			return Collections.unmodifiableSet(all);
		}
		
		public Set<T> partition(final P round) {
			return Collections.unmodifiableSet(byRound.get(round));
		}
	}
	
	protected final PartitionedSet<Artefact,Integer> publishedArtefacts = new PartitionedSet<Artefact,Integer>();
	protected final PartitionedSet<Rating,Integer> ratings = new PartitionedSet<Rating,Integer>();


	@Override
	public Set<Artefact> publishedArtefacts() {
		return publishedArtefacts.all();
	}

	@Override
	public Set<Artefact> publishedArtefacts(int roundNum) {
		return publishedArtefacts.partition(roundNum);
	}

	@Override
	public Set<Rating> ratings() {
		return ratings.all();
	}

	@Override
	public Set<Rating> ratings(int roundNum) {
		return ratings.partition(roundNum);
	}

}
