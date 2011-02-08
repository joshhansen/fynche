package colors.agents;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import colors.MultiAgentSystem;
import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.PreferenceModel;
import colors.ratings.SimpleRating;

public abstract class AbstractAgent implements Agent {
	private static final long serialVersionUID = 1L;
	
	protected static class PartitionedSet<T,P> {
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
	
	protected final MultiAgentSystem sys;
	protected Map<Agent,Double> affinities = new HashMap<Agent,Double>();
	protected Map<Agent,PreferenceModel> preferenceModels = new HashMap<Agent,PreferenceModel>();
	protected final PartitionedSet<Artefact,Integer> publishedArtefacts = new PartitionedSet<Artefact,Integer>();
	protected final PartitionedSet<SimpleRating,Integer> ratings = new PartitionedSet<SimpleRating,Integer>();

	public AbstractAgent(final MultiAgentSystem sys) {
		this.sys = sys;
	}
	
	public MultiAgentSystem system() {
		return sys;
	}

	@Override
	public Set<Artefact> publishedArtefacts() {
		return publishedArtefacts.all();
	}

	@Override
	public Set<Artefact> publishedArtefacts(int roundNum) {
		return publishedArtefacts.partition(roundNum);
	}

	@Override
	public Set<SimpleRating> ratings() {
		return ratings.all();
	}

	@Override
	public Set<SimpleRating> ratings(int roundNum) {
		return ratings.partition(roundNum);
	}
	
	@Override
	public Map<Agent, Double> affinities() {
		return affinities;
	}

	public Map<Agent, PreferenceModel> preferenceModels() {
		return preferenceModels;
	}
	
	
	
//	public PreferenceModel preferences() {
//		return prefs;
//	}
	
//	public void create() {
//		for(int i = 0; i < numArtefactsToGenerate(); i++) {
//			Artefact a = generateArtefact();
//			if(shouldPublish(a))
//				publishedArtefacts.add(a, sys.round());
//		}
//	}
//	
//	protected abstract int numArtefactsToGenerate();
//	protected abstract Artefact generateArtefact();
//	protected abstract boolean shouldPublish(final Artefact a);

}
