package colors.affinities;

import colors.interfaces.AffinityCombo;
import colors.interfaces.Agent;
import colors.interfaces.Factory;
import colors.util.Counter;
import colors.util.Rand;
import colors.util.Util.SmartStaticFactory;

public class RandomAffinityCombo implements AffinityCombo {
	public static final Factory<RandomAffinityCombo> factory = new SmartStaticFactory<RandomAffinityCombo>(){
		@Override
		protected RandomAffinityCombo instantiate_() {
			return new RandomAffinityCombo();
		}
	};
	
	private final double maxAffin;
	
	public RandomAffinityCombo() {
		this(1.0);
	}
	
	public RandomAffinityCombo(double maxAffin) {
		this.maxAffin = maxAffin;
	}

	@Override
	public Counter<Agent> newAffinities(Agent agent) {
		Counter<Agent> affins = new Counter<Agent>();
		for(Agent otherAgent : agent.system().agents()) {
			if(otherAgent != agent) {
				affins.setCount(otherAgent, Rand.nextDouble()*maxAffin);
			}
		}
		affins.normalize();
		return affins;
	}

	@Override
	public Counter<Agent> initialAffinities(Agent agent) {
		return newAffinities(agent);
	}
}
