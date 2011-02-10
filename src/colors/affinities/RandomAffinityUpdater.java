package colors.affinities;

import java.util.Random;

import colors.interfaces.AffinityUpdater;
import colors.interfaces.Agent;
import colors.util.Counter;

public class RandomAffinityUpdater implements AffinityUpdater {
	private static final Random rand = new Random();
	@Override
	public Counter<Agent> newAffinities(Agent agent) {
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
