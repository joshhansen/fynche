package colors.affinities;

import colors.interfaces.AffinityCombo;
import colors.interfaces.Agent;
import colors.interfaces.Factory;
import colors.util.Counter;
import colors.util.Util.SmartStaticFactory;

public class NullAffinityCombo implements AffinityCombo {
	public static final Factory<AffinityCombo> factory = new SmartStaticFactory<AffinityCombo>(){
		@Override
		protected AffinityCombo instantiate_() {
			return new NullAffinityCombo();
		}
	};
	
	@Override
	public Counter<Agent> newAffinities(Agent agent) {
		return agent.affinities();
	}
	
	@Override
	public Counter<Agent> initialAffinities(Agent agent) {
		return newAffinities(agent);
	}

}
