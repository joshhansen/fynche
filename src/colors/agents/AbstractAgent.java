package colors.agents;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import colors.MultiAgentSystem;
import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.PreferenceModel;
import colors.interfaces.Rating;
import colors.util.Counter;
import colors.util.PartitionedSet;

public abstract class AbstractAgent implements Agent {
	private static final long serialVersionUID = 1L;
	
	protected final MultiAgentSystem sys;
	protected Counter<Agent> affinities = new Counter<Agent>();
	protected Map<Agent,PreferenceModel> preferenceModels = new HashMap<Agent,PreferenceModel>();
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
	public Counter<Agent> affinities() {
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
