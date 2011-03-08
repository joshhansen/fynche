package colors.affinities;

import colors.interfaces.AffinityInitializer;
import colors.interfaces.Agent;
import colors.interfaces.Factory;
import colors.util.Counter;
import colors.util.Rand;

public class RandomAffinityInitializer implements AffinityInitializer {
	public static Factory<AffinityInitializer> factory() {
		return new Factory<AffinityInitializer>(){
			@Override
			public AffinityInitializer instantiate() {
				return new RandomAffinityInitializer();
			}
		};
	}
	
	@Override
	public Counter<Agent> initialAffinities(Agent agent) {
		Counter<Agent> affins = new Counter<Agent>();
		for(Agent otherAgent : agent.system().agents()) {
			if(otherAgent != agent) {
				affins.setCount(otherAgent, Rand.nextDouble());
			}
		}
		affins.normalize();
		return affins;
	}
}
