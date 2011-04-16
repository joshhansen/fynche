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
import java.util.List;

import fynche.interfaces.Agent;
import fynche.interfaces.Artefact;
import fynche.interfaces.Factory;
import fynche.interfaces.PublicationDecider;
import fynche.interfaces.Rating;
import fynche.sys.MultiAgentSystem;
import fynche.util.Counter;
import fynche.util.Util.SmartStaticFactory;

/**
 * Tries to show off to the most popular agents
 * @author Josh Hansen
 *
 */
public class SycophanticPublicationDecider implements PublicationDecider {
	public static Factory<SycophanticPublicationDecider> factory(final int topN) {
		return new SmartStaticFactory<SycophanticPublicationDecider>(){
			@Override
			protected SycophanticPublicationDecider instantiate_() {
				return new SycophanticPublicationDecider(topN);
			}
		};
	}
	
	private final int topN;
	private Integer lastUpdated = null;
//	private Map<Agent,Double> averageRatings = new HashMap<Agent,Double>();
	private List<Agent> topNAgents = new ArrayList<Agent>();
	private List<Double> topNScores = new ArrayList<Double>();
	
	public SycophanticPublicationDecider(final int topN) {
		this.topN = topN;
	}

	@Override
	public boolean shouldPublish(Agent potentialPublisher, Artefact artefact, Agent toAgent) {
		if(lastUpdated == null || lastUpdated.intValue() != MultiAgentSystem.instance().round()) {
			updateAverageRatings();
			lastUpdated = MultiAgentSystem.instance().round();
		}
		return topNAgents.contains(toAgent);
		
//		final double avgRating = averageRatings.get(toAgent);
//		return avgRating >= threshold;
	}
	
	/* TODO? A space<->time complexity tradeoff: keep counts and sums around and update the averages incrementally each round */
	private void updateAverageRatings() {
		topNAgents.clear();
		topNScores.clear();
		
		Counter<Agent> counts = new Counter<Agent>();
		Counter<Agent> ratingSums = new Counter<Agent>();
		
		for(final Agent agent : MultiAgentSystem.instance().agents()) {
			for(Rating rating : agent.ratings()) {
				counts.increment(rating.artefactCreator());
				ratingSums.increment(rating.artefactCreator(), rating.rating());
			}
		}
		for(final Agent agent : MultiAgentSystem.instance().agents()) {
			final Double avg = ratingSums.getCount(agent) / counts.getCount(agent);
			if(topNAgents.size() < topN) {
				topNAgents.add(agent);
				topNScores.add(avg);
			} else {
				for(int i = 0; i < topNAgents.size(); i++) {
					if(avg > topNScores.get(i)) {
						topNAgents.set(i, agent);
						topNScores.set(i, avg);
					}
				}
			}
		}
	}
	
	
}
