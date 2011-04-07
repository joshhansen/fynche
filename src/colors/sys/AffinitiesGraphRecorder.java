package colors.sys;

import colors.interfaces.Agent;
import colors.util.DynamicGraph;
import colors.util.DynamicGraph.Edge;

public class AffinitiesGraphRecorder extends SysAdaptor {
	private final DynamicGraph graph;
	private final String saveToFilename;
	
	
	
	public AffinitiesGraphRecorder(final MultiAgentSystem sys) {
		this(sys, null);
	}
	
	public AffinitiesGraphRecorder(final MultiAgentSystem sys, final String saveToFilename) {
		this.saveToFilename = saveToFilename;
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
			final String id1 = agent1.toString();
			for(Agent agent2 : sys.agents) {
				final String id2 = agent2.toString();
				if(id1.compareTo(id2) > 0) {
					final double affin1 = agent1.affinities().getCount(agent2);
					final double affin2 = agent2.affinities().getCount(agent1);
					final double affin = 100*(affin1+affin2)/2.0;
					final String edgeID = graph.edgeKey(id1, id2);
//					final String edgeID = id1+"__"+id2;//+"__"+round;
					final Edge e = graph.containsEdge(edgeID) ? graph.getEdge(edgeID) : graph.addEdge(id1, id2);
					e.enter(round);
					e.exit(round+1);
					e.setStaticAttribute("weight", String.valueOf(affin));
//					graph.getEdge(agent1.toString(), agent2.toString()).enterDynamicAttributeValue("weight", String.valueOf(affin), round);
//					graph.getEdge(agent1.toString(), agent2.toString()).exitDynamicAttributeValue("weight", String.valueOf(affin), round+1);
				}
			}
		}
	}

	@Override
	public void runFinished(MultiAgentSystem sys) {
		if(saveToFilename != null)
			graph.toGEXF(saveToFilename);
	}
}