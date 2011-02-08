package colors.affinities;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import colors.interfaces.AffinityInitializer;
import colors.interfaces.Agent;
import colors.interfaces.Factory;

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
	public Map<Agent, Double> initialAffinities(Agent agent) {
		Map<Agent,Double> affins = new HashMap<Agent,Double>();
		for(Agent otherAgent : agent.system().agents()) {
			if(otherAgent != agent) {
				affins.put(otherAgent, rand.nextDouble());
			}
		}
		return affins;
	}
}
