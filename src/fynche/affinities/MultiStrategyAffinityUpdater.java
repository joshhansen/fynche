/*
 * Fynche - a Framework for Multiagent Computational Creativity
 * Copyright 2011 Josh Hansen
 * 
 * This file is part of the Fynche <https://github.com/joshhansen/fynche>.
 * 
 * Fynche is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Fynche is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with Fynche.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * If you have inquiries regarding any further use of Fynche, please
 * contact Josh Hansen <http://joshhansen.net/>
 */
package fynche.affinities;

import fynche.interfaces.AffinityUpdater;
import fynche.interfaces.Agent;
import fynche.interfaces.Factory;
import fynche.util.Counter;
import fynche.util.Rand;

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
