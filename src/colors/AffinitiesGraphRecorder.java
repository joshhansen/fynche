package colors;

import colors.interfaces.Agent;
import colors.util.DynamicGraph;
import colors.util.DynamicGraph.Edge;

class AffinitiesGraphRecorder implements RoundFinishedListener {
	private final DynamicGraph graph;
	public AffinitiesGraphRecorder(final MultiAgentSystem sys) {
		this.graph = new DynamicGraph(DynamicGraph.Directionality.DIRECTED);
		for(Agent agent : sys.agents) {
			graph.addNode(agent.toString());
		}
		
//		for(Agent agent : agents) {
//			for(Agent other : agents) {
//				if(agent.toString().compareTo(other.toString()) > 0) {
//					graph.addEdge(agent.toString(), other.toString());
//				}
//			}
//		}
	}

	@Override
	public void roundFinished(MultiAgentSystem sys, int round) {
		for(Agent agent1 : sys.agents) {
			for(Agent agent2 : sys.agents) {
				if(agent1.toString().compareTo(agent2.toString()) > 0) {
					final double affin1 = agent1.affinities().getCount(agent2);
					final double affin2 = agent2.affinities().getCount(agent1);
					final double affin = 100*(affin1+affin2)/2.0;
					final Edge e = graph.addEdge(agent1.toString(), agent2.toString(), agent1.toString()+"__"+agent2.toString()+"__"+round);
					e.enter(round);
					e.exit(round+1);
					e.setStaticAttribute("weight", String.valueOf(affin));
//					graph.getEdge(agent1.toString(), agent2.toString()).enterDynamicAttributeValue("weight", String.valueOf(affin), round);
//					graph.getEdge(agent1.toString(), agent2.toString()).exitDynamicAttributeValue("weight", String.valueOf(affin), round+1);
				}
			}
		}
	}
}