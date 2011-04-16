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
import fynche.util.Util.SmartStaticFactory;

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
