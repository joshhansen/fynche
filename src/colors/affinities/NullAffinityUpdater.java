package colors.affinities;

import java.util.Map;

import colors.Factory;
import colors.interfaces.AffinityUpdater;
import colors.interfaces.Agent;

public class NullAffinityUpdater implements AffinityUpdater {
	public static Factory<AffinityUpdater> factory() {
		return new Factory<AffinityUpdater>(){
			@Override
			public AffinityUpdater instantiate() {
				return new NullAffinityUpdater();
			}
		};
	}
	@Override
	public Map<Agent, Double> newAffinities(Agent agent) {
		return agent.affinities();
	}

}
