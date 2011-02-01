package colors.affinities;

import java.util.Map;

import colors.agents.Agent;

public class NullAffinityUpdater implements AffinityUpdater {

	@Override
	public Map<Agent, Double> newAffinities(Agent agent) {
		return agent.affinities();
	}

}
