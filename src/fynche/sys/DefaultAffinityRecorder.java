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
package fynche.sys;

import fynche.interfaces.Agent;

public class DefaultAffinityRecorder extends SysAdaptor {
	@Override
	public void roundFinished(MultiAgentSystem sys, int round) {
		for(Agent agent1 : sys.agents) {
			final String id1 = agent1.toString();
			for(Agent agent2 : sys.agents) {
				final String id2 = agent2.toString();
				final double affin1 = agent1.affinities().getCount(agent2);
				recordAffinity(agent1, agent2, round, affin1);
				
				if(id1.compareTo(id2) > 0) {
					final double affin2 = agent2.affinities().getCount(agent1);
					final double affin = 100*(affin1+affin2)/2.0;
					
					recordAverageAffinity(agent1, agent2, round, affin);
				}
			}
		}
	}

	protected void recordAffinity(Agent agent1, Agent agent2, int round, double affinity) {
		
	}
	
	protected void recordAverageAffinity(Agent agent1, Agent agent2, int round, double affinity) {
		
	}
}
