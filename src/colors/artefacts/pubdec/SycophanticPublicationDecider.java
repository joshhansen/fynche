package colors.artefacts.pubdec;

import java.util.ArrayList;
import java.util.List;

import colors.MultiAgentSystem;
import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.PublicationDecider;
import colors.interfaces.Rating;
import colors.util.Counter;

/**
 * Tries to show off to the most popular agents
 * @author Josh Hansen
 *
 */
public class SycophanticPublicationDecider implements PublicationDecider {
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
