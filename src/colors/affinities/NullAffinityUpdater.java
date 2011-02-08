package colors.affinities;

import java.util.Map;

import colors.interfaces.AffinityUpdater;
import colors.interfaces.Agent;
import colors.interfaces.Factory;

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
