package colors.affinities;

import java.util.Map;

import colors.agents.Agent;

public interface AffinityInitializer {
	public Map<Agent,Double> initialAffinities(final Agent agent);
}
