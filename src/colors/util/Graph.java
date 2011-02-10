package colors.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Graph {
	/** This helps us maintain a global list of attributes based on which ones are set at the node and edge level */
	private static class SetUpdatingMap<K,V> extends HashMap<K,V> {
		private final Set<K> set;
		
		public SetUpdatingMap(Set<K> set) {
			this.set = set;
		}

		@Override
		public V put(K key, V value) {
			this.set.add(key);
			return super.put(key, value);
		}
	}
	
	public class Node {
		private final String id;
		private final String label;
		private final Map<String,String> attributes;
		private final Set<Edge> edges;
		public Node(String id, String label) {
			this.id = id;
			this.label = label;
			this.attributes = new SetUpdatingMap<String,String>(nodeAttributeNames);
			this.edges = new HashSet<Edge>();
		}
		
		public void setAttribute(final String attribute, final String value) {
			this.attributes.put(attribute, value);
		}

		public String getID() {
			return id;
		}

		public String getLabel() {
			return label;
		}

		public Map<String, String> getAttributes() {
			return Collections.unmodifiableMap(attributes);
		}

		public Set<Edge> getEdges() {
			return Collections.unmodifiableSet(edges);
		}
	}
	
	public class Edge {
		private final String id;
		private final Node node1;
		private final Node node2;
		private final Map<String,String> attributes;
		public Edge(final String id, final String node1ID, final String node2ID) {
			this.id = id;
			this.node1 = nodes.get(node1ID);
			this.node2 = nodes.get(node2ID);
			if(this.node1 == null) throw new IllegalArgumentException();
			if(this.node2 == null) throw new IllegalArgumentException();
			this.attributes = new SetUpdatingMap<String,String>(edgeAttributeNames);
			
			this.node1.edges.add(this);
			this.node2.edges.add(this);
		}
		
		public void setAttribute(final String attribute, final String value) {
			this.attributes.put(attribute, value);
		}

		public String getId() {
			return id;
		}

		public Node getNode1() {
			return node1;
		}

		public Node getNode2() {
			return node2;
		}

		public Map<String, String> getAttributes() {
			return Collections.unmodifiableMap(attributes);
		}
		
		
	}
	
	public static enum Directionality {
		DIRECTED,
		UNDIRECTED
	}
	private final Directionality directionality; 
	private int nextEdgeID = 0;
	private final Set<String> nodeAttributeNames = new HashSet<String>();
	private final Set<String> edgeAttributeNames = new HashSet<String>();
	private final Map<String,Node> nodes = new HashMap<String,Node>();
	private final Map<String,Edge> edges = new HashMap<String,Edge>();
	
	public Graph(final Directionality directionality) {
		this.directionality = directionality;
	}
	
	public Node addNode(final String id) {
		return addNode(id, id);
	}
	
	public Node addNode(final String id, final String label) {
		if(nodes.containsKey(id)) throw new IllegalArgumentException("Node with ID \"" + id + "\" already exists");
		final Node node = new Node(id, label);
		nodes.put(id, node);
		return node;
	}
	
	private String edgeKey(final String node1ID, final String node2ID) {
		String[] arr = new String[]{node1ID, node2ID};
		if(directionality.equals(Directionality.UNDIRECTED))
			Arrays.sort(arr);
		return arr[0] + "__" + arr[1];
	}
	
	public Edge addEdge(final String node1ID, final String node2ID) {
		final String key = edgeKey(node1ID, node2ID);
		if(!nodes.containsKey(node1ID)) throw new IllegalArgumentException("Node with ID \"" + node1ID + "\" doesn't exist");
		if(!nodes.containsKey(node2ID)) throw new IllegalArgumentException("Node with ID \"" + node2ID + "\" doesn't exist");
		if(edges.containsKey(key)) throw new IllegalArgumentException("Edge with endpoints \"" + node1ID + "\" and \"" + node2ID + "\" already exists");
		final Edge edge = new Edge(String.valueOf(nextEdgeID++), node1ID, node2ID);
		edges.put(key, edge);
		return edge;
	}
	
	public boolean containsNode(final String id) {
		return nodes.containsKey(id);
	}
	
	public boolean containsEdge(final String node1ID, final String node2ID) {
		return edges.containsKey(edgeKey(node1ID, node2ID));
	}
	
	public Set<Node> nodes() {
		return Collections.unmodifiableSet(new HashSet<Node>(nodes.values()));
	}
	
	public Set<Edge> edges() {
		return Collections.unmodifiableSet(new HashSet<Edge>(edges.values()));
	}
	
	public Set<Edge> edgesWithNode(final String nodeID) {
		return Collections.unmodifiableSet(nodes.get(nodeID).edges);
	}

	/** Boilerplate XML stuff. */
	private static final String gexfHeader = "<?xml version=\"1.0\" encoding=\"UTF−8\"?>\n"
	+ "<gexf xmlns=\"http://www.gexf.net/1.2draft\"\n"
	+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema−instance\"\n"
	+ "xsi:schemaLocation=\"http://www.gexf.net/1.2draft\n"
	+ "http://www.gexf.net/1.2draft/gexf.xsd\"\n"
	+ "version=\"1.2\">\n";
	/** GEXF is a graph exchange file format supported by Gephi */
	public String toGEXF() {
		final StringBuilder gexf = new StringBuilder(gexfHeader);
//		gexf.append("\t<meta lastmodifieddate=\"2009−03−20\">\n");
//		gexf.append("\t\t<creator>Gephi.org</creator>\n");
//		gexf.append("\t\t<description>A graph</description>\n");
//		gexf.append("\t</meta>\n");
		
		gexf.append("\t<graph defaultedgetype=\"");
		if(directionality.equals(Directionality.UNDIRECTED))
			gexf.append("un");
		gexf.append("directed\">\n");
		
		/*** Attributes Declaration ***/
		int nextNodeAttrID = 0;
		Map<String,Integer> nodeAttrIDs = new HashMap<String,Integer>();
		gexf.append("\t\t<attributes class=\"node\">\n");
		for(String nodeAttrName : nodeAttributeNames) {
			nodeAttrIDs.put(nodeAttrName, nextNodeAttrID);
			gexf.append("\t\t\t<attribute id=\"" + nextNodeAttrID + "\" title=\"" + nodeAttrName + "\" type=\"string\"/>\n");
			nextNodeAttrID++;
		}
		gexf.append("\t\t</attributes>\n");
		
		int nextEdgeAttrID = 0;
		Map<String,Integer> edgeAttrIDs = new HashMap<String,Integer>();
		gexf.append("\t\t<attributes class=\"edge\">\n");
		for(String edgeAttrName : edgeAttributeNames) {
			edgeAttrIDs.put(edgeAttrName, nextEdgeAttrID);
			gexf.append("\t\t\t<attribute id=\"" + nextEdgeAttrID + "\" title=\"" + edgeAttrName + "\" type=\"string\"/>\n");
			nextEdgeAttrID++;
		}
		gexf.append("\t\t</attributes>\n");
		
		/*** Nodes ***/
		gexf.append("\t\t<nodes>\n");
		for(Node node : nodes.values()) {
			gexf.append("\t\t\t<node id=\"" + node.id + "\" label=\"" + node.label + "\"");
			if(node.attributes.isEmpty()) {
				gexf.append("/>\n");
			} else {
				gexf.append(">\n");
				gexf.append("\t\t\t\t<attvalues>\n");
				for(Entry<String,String> entry : node.attributes.entrySet()) {
					gexf.append("\t\t\t\t\t<attvalue for=\"" + nodeAttrIDs.get(entry.getKey()) + "\" value=\"" + entry.getValue() + "\"/>\n");
				}
				gexf.append("\t\t\t\t</attvalues>\n");
				gexf.append("\t\t\t</node>\n");
			}
		}
		gexf.append("\t\t</nodes>\n");
		
		/*** Edges ***/
		gexf.append("\t\t<edges>\n");
		for(Edge edge : edges.values()) {
			gexf.append("\t\t\t<edge id=\"" + edge.id + "\" source=\"" + edge.node1.id + "\" target=\"" + edge.node2.id + "\"");
			if(edge.attributes.isEmpty()) {
				gexf.append("/>\n");
			} else {
				gexf.append(">\n");
				gexf.append("\t\t\t\t<attvalues>\n");
				for(Entry<String,String> entry : edge.attributes.entrySet()) {
					gexf.append("\t\t\t\t\t<attvalue for=\"" + edgeAttrIDs.get(entry.getKey()) + "\" value=\"" + entry.getValue() + "\"/>\n");
				}
				gexf.append("\t\t\t\t</attvalues>\n");
				gexf.append("\t\t\t</edge>\n");
			}
		}
		gexf.append("\t\t</edges>\n");
		gexf.append("\t</graph>\n");
		gexf.append("</gexf>");
		return gexf.toString();
	}
	
	public void toGEXF(final String filename) {
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(filename));
			w.append(toGEXF());
			w.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Graph g = new Graph(Directionality.UNDIRECTED);
		final Node n1 = g.addNode("a", "Cool node!");
		final Node n2 = g.addNode("b", "Other neat node");
		final Node n3 = g.addNode("c", "Third node");
		final Edge e1 = g.addEdge("a", "b");
		final Edge e2 = g.addEdge("a", "c");
		final Edge e3 = g.addEdge("b", "c");
		n1.setAttribute("coolness", "4.0");
		n2.setAttribute("coolness", "5.0");
		n2.setAttribute("awesomeness", "11.0");
		e1.setAttribute("radnicity", "44");
		System.out.println(g.toGEXF());
	}
}
