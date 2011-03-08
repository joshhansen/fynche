package colors.affinities;

import colors.interfaces.AffinityUpdater;
import colors.interfaces.Agent;
import colors.interfaces.Factory;
import colors.util.Counter;
import colors.util.Rand;

public class MultiStrategyAffinityUpdater extends GroupedAffinityUpdater {
	public static final Factory<MultiStrategyAffinityUpdater> factory(final Counter<Factory<? extends AffinityUpdater>> affinUpFacts, final double transitionProb) {
		final Counter<AffinityUpdater> affinUps = new Counter<AffinityUpdater>();
		for(Factory<? extends AffinityUpdater> fact : affinUpFacts.keySet()) {
			affinUps.setCount(fact.instantiate(), affinUpFacts.getCount(fact));
		}
		return new Factory<MultiStrategyAffinityUpdater>(){
			@Override
			public MultiStrategyAffinityUpdater instantiate() {
				return new MultiStrategyAffinityUpdater(affinUps, transitionProb);
			}
		};
	}
	private final double transitionProb;
	private AffinityUpdater currentStrategy = null;
	public MultiStrategyAffinityUpdater(final double transitionProb) {
		this(new Counter<AffinityUpdater>(), transitionProb);
	}
	
	public MultiStrategyAffinityUpdater(Counter<AffinityUpdater> generators, final double transitionProb) {
		super(generators);
		this.transitionProb = transitionProb;
	}
	
	protected void ensureStrategy() {
		if(currentStrategy == null || Rand.nextDouble() < transitionProb)
			currentStrategy = generators.sample();
	}
	
	@Override
	public Counter<Agent> newAffinities(Agent agent) {
		ensureStrategy();
		return currentStrategy.newAffinities(agent);
	}

}
