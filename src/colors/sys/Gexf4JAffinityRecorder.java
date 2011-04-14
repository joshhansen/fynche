package colors.sys;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import colors.interfaces.Agent;

import com.ojn.gexf4j.core.Edge;
import com.ojn.gexf4j.core.EdgeType;
import com.ojn.gexf4j.core.Gexf;
import com.ojn.gexf4j.core.GexfWriter;
import com.ojn.gexf4j.core.Graph;
import com.ojn.gexf4j.core.Node;
import com.ojn.gexf4j.core.data.Attribute;
import com.ojn.gexf4j.core.data.AttributeClass;
import com.ojn.gexf4j.core.data.AttributeList;
import com.ojn.gexf4j.core.data.AttributeType;
import com.ojn.gexf4j.core.data.AttributeValue;
import com.ojn.gexf4j.core.impl.GexfImpl;
import com.ojn.gexf4j.core.impl.StaxGraphWriter;
import com.ojn.gexf4j.core.impl.data.AttributeListImpl;

public class Gexf4JAffinityRecorder extends DefaultAffinityRecorder {
	private final String outputFilename;
	private final Gexf gexf;
	private final Graph g;
	private final Map<String,Node> nodes = new HashMap<String,Node>();
	private final Map<String,Edge> edges = new HashMap<String,Edge>();
	private final Attribute attAffin;
	
	public Gexf4JAffinityRecorder(final String outputFilename) {
		this.outputFilename = outputFilename;
		gexf = new GexfImpl();
		gexf.getMetadata().setCreator("Gexf4JAffinityRecorder");
		g = gexf.getGraph();
		g.setStartDate(asDate(0));
		g.setEndDate(asDate(100));
		// attributes
		AttributeList attrList = new AttributeListImpl(AttributeClass.NODE);
		gexf.getGraph().getAttributeLists().add(attrList);

		attAffin = attrList.createAttribute("0", AttributeType.DOUBLE, "affinity");
		
//		Attribute attIndegree = attrList.createAttribute("1", AttributeType.FLOAT, "indegree");
//		Attribute attFrog = attrList.createAttribute("2", AttributeType.BOOLEAN, "frog").setDefaultValue("true");
	}
	
	private static String edgeKey(final Agent agent1, final Agent agent2) {
		final StringBuilder key = new StringBuilder();
		key.append(agent1.toString());
		key.append("__");
		key.append(agent2.toString());
		return key.toString();
	}
	
	@Override
	protected void recordAffinity(Agent agent1, Agent agent2, int round, double affinity) {
		final Edge e = edge(agent1, agent2);
		
		final AttributeValue av = e.getAttributeValues().createValue(attAffin, String.valueOf(affinity));
		av.setStartDate(asDate(round));
		av.setEndDate(asDate(round+1));
	}
	
	private Node node(final Agent a) {
		final String id = a.toString();
		Node n = nodes.get(id);
		if(n == null) {
			n = g.createNode(id);
			nodes.put(id, n);
		}
		return n;
	}
	
	private Edge edge(final Agent agent1, final Agent agent2) {
		final String key = edgeKey(agent1, agent2);
		Edge e = edges.get(key);
		if(e == null) {
			final Node n1 = node(agent1);
			final Node n2 = node(agent2);
			e = n1.connectTo(key, n2);
			e.setEdgeType(EdgeType.DIRECTED);
			edges.put(key, e);
		}
		return e;
	}
	
	private static Date asDate(final int i) {
		return new Date(Date.UTC(0, i/28, i%28, 0, 0, 0));
	}

	@Override
	public void runFinished(MultiAgentSystem sys) {
		final GexfWriter gw = new StaxGraphWriter();
		try {
			final FileOutputStream fos = new FileOutputStream(outputFilename);
			gw.writeToStream(gexf, fos);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
