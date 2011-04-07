package colors.affinities;

import java.util.Collection;

import colors.interfaces.AffinityCombo;
import colors.interfaces.Agent;
import colors.interfaces.Factory;
import colors.util.Counter;
import colors.util.Util.SmartStaticFactory;

public class MisanthropeAffinityCombo implements AffinityCombo {
	public static final Factory<MisanthropeAffinityCombo> factory = new SmartStaticFactory<MisanthropeAffinityCombo>() {
		@Override
		protected MisanthropeAffinityCombo instantiate_() {
			return new MisanthropeAffinityCombo();
		}
	};
	
	private static final Counter<Agent> heartlessAffinities = new Counter<Agent>(){
		@Override
		public void increment(Agent item) {
			throw new UnsupportedOperationException();
		}
		@Override
		public void incrementAll(Collection<Agent> items) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void increment(Agent item, double inc) {
			throw new UnsupportedOperationException();
		}
		@Override
		public double getCount(Agent item) {
			return 0.0;
		}
		@Override
		public double totalCount() {
			return 0.0;
		}
		@Override
		public void setCount(Agent item, double weight) {
			throw new UnsupportedOperationException();
		}
	};

	@Override
	public Counter<Agent> initialAffinities(Agent agent) {
		return heartlessAffinities;
	}

	@Override
	public Counter<Agent> newAffinities(Agent agent) {
		return heartlessAffinities;
	}

}
