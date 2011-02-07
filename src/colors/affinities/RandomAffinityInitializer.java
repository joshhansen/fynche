package colors.affinities;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import colors.interfaces.AffinityInitializer;
import colors.interfaces.Agent;

public class RandomAffinityInitializer implements AffinityInitializer {
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
