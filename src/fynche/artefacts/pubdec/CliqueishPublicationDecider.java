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
package fynche.artefacts.pubdec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fynche.interfaces.Agent;
import fynche.interfaces.Artefact;
import fynche.interfaces.PublicationDecider;
import fynche.sys.MultiAgentSystem;

public class CliqueishPublicationDecider implements PublicationDecider {
	private final int topN;
	private Integer lastUpdated = null;
	private Map<Agent,List<Agent>> bestBuddies = new HashMap<Agent,List<Agent>>();
	private Map<Agent,List<Double>> bestBudAffinities = new HashMap<Agent,List<Double>>();
	
	public CliqueishPublicationDecider(int topN) {
		this.topN = topN;
	}

	@Override
	public boolean shouldPublish(Agent potentialPublisher, Artefact artefact, Agent toAgent) {
		if(lastUpdated == null) {
			for(Agent agent : MultiAgentSystem.instance().agents()) {
				bestBuddies.put(agent, new ArrayList<Agent>());
				bestBudAffinities.put(agent, new ArrayList<Double>());
			}
		}
		if(lastUpdated == null || lastUpdated.intValue() != MultiAgentSystem.instance().round()) {
			updateBestBuddies();
			lastUpdated = MultiAgentSystem.instance().round();
		}
		
		return bestBuddies.get(potentialPublisher).contains(toAgent);
	}

	// TODO Optimize with different sort?
	private void updateBestBuddies() {
		for(Agent agent : MultiAgentSystem.instance().agents()) {
			final List<Agent> buddies = bestBuddies.get(agent);
			final List<Double> affinities = bestBudAffinities.get(agent);
			buddies.clear();
			affinities.clear();
			for(Entry<Agent,Double> affinity : agent.affinities().entrySet()) {
				if(buddies.size() < topN) {
					buddies.add(affinity.getKey());
					affinities.add(affinity.getValue());
				} else {
					for(int i = 0; i < buddies.size(); i++) {
						if(affinity.getValue() > affinities.get(i)) {
							buddies.set(i, affinity.getKey());
							affinities.set(i, affinity.getValue());
						}
					}
				}
			}
		}
	}

}
