package colors.affinities;

import java.util.Map;

import colors.interfaces.AffinityUpdater;
import colors.interfaces.Agent;

public class NullAffinityUpdater implements AffinityUpdater {

	@Override
	public Map<Agent, Double> newAffinities(Agent agent) {
		return agent.affinities();
	}

}
