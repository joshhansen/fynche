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

import fynche.interfaces.AffinityCombo;
import fynche.interfaces.Agent;
import fynche.interfaces.Factory;
import fynche.util.Counter;
import fynche.util.Rand;
import fynche.util.Util.SmartStaticFactory;

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
