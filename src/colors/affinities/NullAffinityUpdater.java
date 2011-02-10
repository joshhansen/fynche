package colors.affinities;

import colors.interfaces.AffinityUpdater;
import colors.interfaces.Agent;
import colors.interfaces.Factory;
import colors.util.Counter;

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
	public Counter<Agent> newAffinities(Agent agent) {
		return agent.affinities();
	}

}
