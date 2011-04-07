package colors.artefacts.pubdec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.PublicationDecider;
import colors.sys.MultiAgentSystem;

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
