package colors.affinities;

import colors.interfaces.AffinityCombo;
import colors.interfaces.Agent;
import colors.interfaces.Factory;
import colors.util.Counter;
import colors.util.Util.SmartStaticFactory;

public class IndifferentAffinityCombo implements AffinityCombo {
	public static final Factory<IndifferentAffinityCombo> factory = new SmartStaticFactory<IndifferentAffinityCombo>() {
		@Override
		protected IndifferentAffinityCombo instantiate_() {
			return new IndifferentAffinityCombo();
		}
	};

	@Override
	public Counter<Agent> initialAffinities(Agent agent) {
		final double affin = 1.0 / (double)(agent.system().agents().size() - 1);
		final Counter<Agent> affins = new Counter<Agent>();
		for(Agent other : agent.system().agents()) {
			if(other != agent) {
				affins.setCount(other, affin);
			}
		}
		return affins;
	}

	@Override
	public Counter<Agent> newAffinities(Agent agent) {
		return initialAffinities(agent);
	}

}
