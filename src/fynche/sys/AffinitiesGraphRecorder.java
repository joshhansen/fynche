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
import fynche.util.Directionality;
import fynche.util.DynamicGraph;
import fynche.util.DynamicGraph.Edge;

public class AffinitiesGraphRecorder extends DefaultAffinityRecorder {
	public static enum Type {
		DYNAMIC_EDGES, DYNAMIC_ATTRS
	}
	
	private final Type type;
	private final DynamicGraph graph;
	private final String saveToFilename;
	
	
	
	public AffinitiesGraphRecorder(final MultiAgentSystem sys) {
		this(sys, Type.DYNAMIC_ATTRS);
	}
	
	public AffinitiesGraphRecorder(final MultiAgentSystem sys, final Type type) {
		this(sys, type, null);
	}
	
	public AffinitiesGraphRecorder(final MultiAgentSystem sys, final String saveToFilename) {
		this(sys, Type.DYNAMIC_ATTRS, saveToFilename);
	}
	
	public AffinitiesGraphRecorder(final MultiAgentSystem sys, final Type type, final String saveToFilename) {
		this.type = type;
		this.saveToFilename = saveToFilename;
		this.graph = new DynamicGraph(Directionality.DIRECTED);
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



	protected void recordAverageAffinity(final Agent agent1, final Agent agent2, final int round, final double affinity) {
		final String id1 = agent1.toString();
		final String id2 = agent2.toString();
		final String affinityS = String.valueOf(affinity);
		switch(type) {
			case DYNAMIC_EDGES:
				final String edgeID = graph.edgeKey(id1, id2) + "__" + round;
				final Edge e = graph.addEdge(id1, id2, edgeID);
				e.enter(round);
				e.exit(round+1);
				e.setStaticAttribute("weight", affinityS);
				break;
			case DYNAMIC_ATTRS:
				final String edgeID2 = graph.edgeKey(id1, id2);
				final Edge e2 = graph.containsEdge(edgeID2) ? graph.getEdge(edgeID2) : graph.addEdge(id1, id2);
				e2.enterDynamicAttributeValue("weight", affinityS, round);
				e2.exitDynamicAttributeValue("weight", affinityS, round+1);
				break;
			default:
				break;
		}
	}

	@Override
	public void runFinished(MultiAgentSystem sys) {
		if(saveToFilename != null)
			graph.toGEXF(saveToFilename);
	}
}