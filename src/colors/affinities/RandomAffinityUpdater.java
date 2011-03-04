package colors.affinities;

import java.util.Random;

import colors.interfaces.AffinityUpdater;
import colors.interfaces.Agent;
import colors.util.Counter;

public class RandomAffinityUpdater implements AffinityUpdater {
	private static final Random rand = new Random();
	private final double maxAffin;
	
	public RandomAffinityUpdater() {
		this(1.0);
	}
	
	public RandomAffinityUpdater(double maxAffin) {
		this.maxAffin = maxAffin;
	}

	@Override
	public Counter<Agent> newAffinities(Agent agent) {
		Counter<Agent> affins = new Counter<Agent>();
		for(Agent otherAgent : agent.system().agents()) {
			if(otherAgent != agent) {
				affins.setCount(otherAgent, rand.nextDouble()*maxAffin);
			}
		}
		affins.normalize();
		return affins;
	}
}
