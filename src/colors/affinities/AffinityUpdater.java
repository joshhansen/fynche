package colors.affinities;

import java.util.Map;

import colors.agents.Agent;

public interface AffinityUpdater {
	public Map<Agent,Double> newAffinities(final Agent agent);
}
