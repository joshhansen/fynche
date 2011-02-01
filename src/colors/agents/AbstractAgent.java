package colors.agents;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import colors.MultiAgentSystem;
import colors.Rating;
import colors.artefacts.Artefact;

public abstract class AbstractAgent implements Agent {
	private static final long serialVersionUID = 1L;
	
	protected static class PartitionedSet<T,P> {
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
	
	protected final MultiAgentSystem sys;
	protected Map<Agent,Double> affinities = new HashMap<Agent,Double>();
	protected final PartitionedSet<Artefact,Integer> publishedArtefacts = new PartitionedSet<Artefact,Integer>();
	protected final PartitionedSet<Rating,Integer> ratings = new PartitionedSet<Rating,Integer>();

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
	public Set<Rating> ratings() {
		return ratings.all();
	}

	@Override
	public Set<Rating> ratings(int roundNum) {
		return ratings.partition(roundNum);
	}
	
	@Override
	public Map<Agent, Double> affinities() {
		return affinities;
	}
	
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
