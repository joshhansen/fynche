package colors.affinities;

import java.util.Random;

import colors.interfaces.AffinityInitializer;
import colors.interfaces.Agent;
import colors.interfaces.Factory;
import colors.util.Counter;

public class RandomAffinityInitializer implements AffinityInitializer {
	public static Factory<AffinityInitializer> factory() {
		return new Factory<AffinityInitializer>(){
			@Override
			public AffinityInitializer instantiate() {
				return new RandomAffinityInitializer();
			}
		};
	}
	private final Random rand = new Random();
	@Override
	public Counter<Agent> initialAffinities(Agent agent) {
		Counter<Agent> affins = new Counter<Agent>();
		for(Agent otherAgent : agent.system().agents()) {
			if(otherAgent != agent) {
				affins.setCount(otherAgent, rand.nextDouble());
			}
		}
		affins.normalize();
		return affins;
	}
}
