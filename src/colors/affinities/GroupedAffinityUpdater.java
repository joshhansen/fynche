package colors.affinities;

import colors.interfaces.AffinityUpdater;
import colors.interfaces.Agent;
import colors.util.Counter;

public class GroupedAffinityUpdater implements AffinityUpdater {
	protected final Counter<AffinityUpdater> generators;
	
	public GroupedAffinityUpdater() {
		this(new Counter<AffinityUpdater>());
	}
	
	public GroupedAffinityUpdater(Counter<AffinityUpdater> generators) {
		this.generators = generators;
	}

	public void addGenerator(final AffinityUpdater artGen, final double weight) {
		generators.setCount(artGen, weight);
	}

	@Override
	public Counter<Agent> newAffinities(Agent agent) {
		return generators.sample().newAffinities(agent);
	}
}
