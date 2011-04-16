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
import fynche.util.CounterMap;
import fynche.util.Rand;
import fynche.util.Util.SmartStaticFactory;

public class ObsessiveAffinityCombo implements AffinityCombo {
	public static final Factory<ObsessiveAffinityCombo> factory = new SmartStaticFactory<ObsessiveAffinityCombo>() {
		@Override
		protected ObsessiveAffinityCombo instantiate_() {
			return new ObsessiveAffinityCombo();
		}
	};
	
	private final double changeProb;
	private CounterMap<Agent,Agent> affins = new CounterMap<Agent,Agent>();
	public ObsessiveAffinityCombo() {
		this(0.0);
	}
	
	public ObsessiveAffinityCombo(final double changeProb) {
		this.changeProb = changeProb;
	}
	
	private Counter<Agent> chooseNewObsession(final Agent obsesser) {
		final Agent[] agents = obsesser.system().agents().toArray(new Agent[0]);
		Agent objectOfObsession = null;
		do {
			final int chosen = Rand.nextInt(agents.length);
			objectOfObsession = agents[chosen];
		} while(objectOfObsession==obsesser);
		
		final Counter<Agent> agentAffins = affins.getCounter(obsesser);
		agentAffins.setCount(objectOfObsession, 1.0);
		
		for(Agent other : obsesser.system().agents()) {
			if(other != obsesser && other != objectOfObsession)
				agentAffins.setCount(other, 1.0/(double)(agents.length-2));
		}
		agentAffins.normalize();
		return agentAffins;
	}
	
	@Override
	public Counter<Agent> newAffinities(Agent agent) {
		if(!affins.containsKey(agent) || Rand.nextDouble() < changeProb)
			return chooseNewObsession(agent);
		return affins.getCounter(agent);
	}

	@Override
	public Counter<Agent> initialAffinities(Agent agent) {
		return newAffinities(agent);
	}

}
