package colors.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class DynamicGraph {
	private static enum TransitionType {
		ENTER, EXIT
	}
	private static class Transition {
		private final Integer atTime;
		private final TransitionType transitionType;
		public Transition(Integer atTime, TransitionType transitionType) {
			this.atTime = atTime;
			this.transitionType = transitionType;
		}
		
		public String toString() {
			return transitionType.name() + "(" + atTime + ")";
		}
	}
	private class Times {
		private final SortedSet<Transition> times = new TreeSet<Transition>(new Comparator<Transition>(){
			@Override
			public int compare(Transition o1, Transition o2) {
				return o1.atTime.compareTo(o2.atTime);
			}
		});
		public void enter() {
			enter(time);
		}
		
		public void enter(final int atTime) {
			times.add(new Transition(atTime, TransitionType.ENTER));
		}
		
		public void exit() {
			exit(time);
		}
		
		public void exit(final int atTime) {
			times.add(new Transition(atTime, TransitionType.EXIT));
		}
		
		public Set<Transition> times() {
			return Collections.unmodifiableSet(times);
		}
		
		private String asSpell(Integer enter, Integer exit) {
			final StringBuilder spell = new StringBuilder();
			spell.append("\t\t\t\t\t<spell");
			if(enter != null) {
				spell.append(" start=\"");
				spell.append(enter);
				spell.append("\"");
			}
			if(exit != null) {
				spell.append(" end=\"");
				spell.append(exit);
				spell.append("\"");
			}
			spell.append("/>\n");
			return spell.toString();
		}
		
		public String toGEXFSpells() {
			final StringBuilder spells = new StringBuilder();
			spells.append("<spells>\n");
			Integer enter = null;
			Integer exit = null;
			for(Transition transition : times) {
				switch(transition.transitionType) {
				case ENTER:
					if(enter == null) {
						if(exit == null) {
							enter = transition.atTime;
						} else {
							spells.append(asSpell(enter, exit));
							enter = transition.atTime;
							exit = null;
						}
					} else {
						if(exit == null) {
							spells.append(asSpell(enter, exit));
							enter = transition.atTime;
						} else {
							spells.append(asSpell(enter, exit));
							enter = transition.atTime;
							exit = null;
						}
					}
					break;
				case EXIT:
					if(enter == null) {
						if(exit == null) {
							spells.append(asSpell(null, transition.atTime));
						} else {
							spells.append(asSpell(enter, exit));
							exit = transition.atTime;
						}
					} else {
						if(exit == null) {
							spells.append(asSpell(enter, transition.atTime));
							enter = null;
						} else {
							spells.append(asSpell(enter, exit));
							spells.append(asSpell(null, transition.atTime));
							enter = null;
							exit = null;
						}
					}
					break;
				default:
					break;
				}
			}
			if(enter != null || exit != null) spells.append(asSpell(enter, exit));
			spells.append("\t\t\t\t</spells>\n");
			return spells.toString();
		}
	}
	
	private static class MutableInt {
		private int value;
		public MutableInt(final int value) {
			this.value = value;
		}
		
		public void set(final int value) {
			this.value = value;
		}
		
		public int value() {
			return value;
		}
		
		public void increment() {
			value++;
		}
	}
	

	/** This helps us maintain a global list of attributes based on which ones are set at the node and edge level */
	private  class SetUpdatingMap<K,V> extends HashMap<K,V> {
		private final MutableInt nextID;
		private final Map<K,Integer> idMap;
		
		public SetUpdatingMap(Map<K,Integer> idMap, MutableInt nextID) {
			this.idMap = idMap;
			this.nextID = nextID;
		}

		@Override
		public V put(K key, V value) {
			this.idMap.put(key, nextID.value());
			nextID.increment();
			return super.put(key, value);
		}
	}
	
	private class DynamicAttributes {
		private final MutableInt nextID;
		private final Map<String,Integer> idMap;
		private final Map<String,Map<String,Times>> map = new HashMap<String,Map<String,Times>>();
		
		public DynamicAttributes(Map<String,Integer> idMap, MutableInt nextID) {
			this.idMap = idMap;
			this.nextID = nextID;
		}
		
		public Times get(final String attr, final String value) {
			idMap.put(attr, nextID.value());
			nextID.increment();
			
			Map<String,Times> submap = map.get(attr);
			if(submap == null) {
				submap = new HashMap<String,Times>();
				map.put(attr, submap);
			}
			Times times = submap.get(value);
			if(times == null) {
				times = new Times();
				submap.put(value, times);
			}
			return times;
		}
		
		public boolean isEmpty() {
			return map.isEmpty();
		}
		
		private String asAttValue(String attr, String value, Integer enter, Integer exit) {
			final StringBuilder attValue = new StringBuilder();
			attValue.append("\t\t\t\t\t<attvalue for=\"");
			attValue.append(idMap.get(attr));
			attValue.append("\" value=\"");
			attValue.append(value);
			attValue.append("\"");
			if(enter != null) {
				attValue.append(" start=\"");
				attValue.append(enter);
				attValue.append("\"");
			}
			if(exit != null) {
				attValue.append(" end=\"");
				attValue.append(exit);
				attValue.append("\"");
			}
			attValue.append("/>\n");
			return attValue.toString();
		}
		
		public String toGEXFAttValues() {
			final StringBuilder gexf = new StringBuilder();
			for(Entry<String,Map<String,Times>> entry : map.entrySet()) {
				for(Entry<String,Times> subEntry : entry.getValue().entrySet()) {
					final String attr = entry.getKey();
					final String value = subEntry.getKey();
					final Times times = subEntry.getValue();
					Integer enter = null;
					Integer exit = null;
					for(Transition transition : times.times) {
						System.out.print(transition);
						System.out.print(" ");
						switch(transition.transitionType) {
						case ENTER:
							if(enter == null) {
								if(exit == null) {
									enter = transition.atTime;
								} else {
									gexf.append(asAttValue(attr, value, enter, exit));
									enter = transition.atTime;
									exit = null;
								}
							} else {
								if(exit == null) {
									gexf.append(asAttValue(attr, value, enter, exit));
									enter = transition.atTime;
								} else {
									gexf.append(asAttValue(attr, value, enter, exit));
									enter = transition.atTime;
									exit = null;
								}
							}
							break;
						case EXIT:
							if(enter == null) {
								if(exit == null) {
									gexf.append(asAttValue(attr, value, null, transition.atTime));
								} else {
									gexf.append(asAttValue(attr, value, enter, exit));
									exit = transition.atTime;
								}
							} else {
								if(exit == null) {
									gexf.append(asAttValue(attr, value, enter, transition.atTime));
									enter = null;
								} else {
									gexf.append(asAttValue(attr, value, enter, exit));
									gexf.append(asAttValue(attr, value, null, transition.atTime));
									enter = null;
									exit = null;
								}
							}
							break;
						default:
							break;
						}
					}
					if(enter != null || exit != null) gexf.append(asAttValue(attr, value, enter, exit));
					System.out.println();
				}
			}
			
			return gexf.toString();
		}
	}
	
	public class Node {
		private final String id;
		private final String label;
		private final Map<String,String> staticAttrs;
		private final DynamicAttributes dynamicAttrs;
		private final Set<Edge> edges;
		private final Times times = new Times();
		public Node(String id, String label) {
			this.id = id;
			this.label = label;
			this.staticAttrs = new SetUpdatingMap<String,String>(nodeStaticAttributeIDs, nextNodeAttrID);
			this.dynamicAttrs = new DynamicAttributes(nodeDynamicAttributeIDs, nextNodeAttrID);
			this.edges = new HashSet<Edge>();
		}
		
		public void setStaticAttribute(final String attribute, final String value) {
			staticAttrs.put(attribute, value);
		}
		
		public void enterDynamicAttributeValue(final String attr, final String value) {
			dynamicAttrs.get(attr, value).enter();
		}
		
		public void enterDynamicAttributeValue(final String attr, final String value, final int atTime) {
			dynamicAttrs.get(attr, value).enter(atTime);
		}
		
		public void exitDynamicAttributeValue(final String attr, final String value) {
			dynamicAttrs.get(attr, value).exit();
		}
		
		public void exitDynamicAttributeValue(final String attr, final String value, final int atTime) {
			dynamicAttrs.get(attr, value).exit(atTime);
		}
		
		public String toGEXF() {
			final StringBuilder gexf = new StringBuilder();
			
			gexf.append("<node id=\"");
			gexf.append(id);
			gexf.append("\" label=\"");
			gexf.append(label);
			gexf.append("\"");
			if(dynamicAttrs.isEmpty() && staticAttrs.isEmpty()) {
				gexf.append("/>");
			} else {
				gexf.append(">\n");
				gexf.append("\t\t\t\t<attvalues>\n");
				
				for(Entry<String,String> entry : staticAttrs.entrySet()) {
					gexf.append("\t\t\t\t\t<attvalue for=\"" + nodeStaticAttributeIDs.get(entry.getKey()) + "\" value=\"" + entry.getValue() + "\"/>\n");
				}
				gexf.append(dynamicAttrs.toGEXFAttValues());
				
				gexf.append("\t\t\t\t</attvalues>\n");
				gexf.append("\t\t\t\t");
				if(!times.times().isEmpty()) {
					gexf.append(times.toGEXFSpells());
					gexf.append("\n");
				}
				gexf.append("\t\t\t</node>");
			}
			
			return gexf.toString();
		}

		public void enter() {
			times.enter();
		}

		public void exit() {
			times.exit();
		}

		public void enter(int atTime) {
			times.enter(atTime);
		}

		public void exit(int atTime) {
			times.exit(atTime);
		}
	}
	
	public class Edge {
		private final String id;
		private final Node node1;
		private final Node node2;
		private final Map<String,String> staticAttrs;
		private final DynamicAttributes dynamicAttrs;
		private final Times times = new Times();
		public Edge(final String id, final String node1ID, final String node2ID) {
			this.id = id;
			this.node1 = nodes.get(node1ID);
			this.node2 = nodes.get(node2ID);
			if(this.node1 == null) throw new IllegalArgumentException();
			if(this.node2 == null) throw new IllegalArgumentException();
			this.staticAttrs = new SetUpdatingMap<String,String>(edgeStaticAttributeIDs, nextEdgeAttrID);
			this.dynamicAttrs = new DynamicAttributes(edgeDynamicAttributeIDs, nextEdgeAttrID);
			
			this.node1.edges.add(this);
			this.node2.edges.add(this);
		}
		
		public void setStaticAttribute(final String attribute, final String value) {
			staticAttrs.put(attribute, value);
		}
		
		public void enterDynamicAttributeValue(final String attr, final String value) {
			dynamicAttrs.get(attr, value).enter();
		}
		
		public void enterDynamicAttributeValue(final String attr, final String value, final int atTime) {
			dynamicAttrs.get(attr, value).enter(atTime);
		}
		
		public void exitDynamicAttributeValue(final String attr, final String value) {
			dynamicAttrs.get(attr, value).exit();
		}
		
		public void exitDynamicAttributeValue(final String attr, final String value, final int atTime) {
			dynamicAttrs.get(attr, value).exit(atTime);
		}
		
		public String toGEXF() {
			final StringBuilder gexf = new StringBuilder();
			gexf.append("<edge id=\"");
			gexf.append(id);
			gexf.append("\" source=\"");
			gexf.append(node1.id);
			gexf.append("\" target=\"");
			gexf.append(node2.id);
			gexf.append("\"");
			if(staticAttrs.isEmpty() && dynamicAttrs.isEmpty()) {
				gexf.append("/>");
			} else {
				gexf.append(">\n");
				gexf.append("\t\t\t\t<attvalues>\n");
				for(Entry<String,String> entry : staticAttrs.entrySet()) {
					gexf.append("\t\t\t\t\t<attvalue for=\"" + edgeStaticAttributeIDs.get(entry.getKey()) + "\" value=\"" + entry.getValue() + "\"/>\n");
				}
				gexf.append(dynamicAttrs.toGEXFAttValues());
				gexf.append("\t\t\t\t</attvalues>\n");
				gexf.append("\t\t\t\t");
				if(!times.times().isEmpty()) {
					gexf.append(times.toGEXFSpells());
					gexf.append("\n");
				}
				gexf.append("\t\t\t</edge>");
			}
			return gexf.toString();
		}

		public void enter() {
			times.enter();
		}

		public void exit() {
			times.exit();
		}

		public void enter(int atTime) {
			times.enter(atTime);
		}

		public void exit(int atTime) {
			times.exit(atTime);
		}
	}
	
	public static enum Directionality {
		DIRECTED,
		UNDIRECTED
	}
	private int time = 0;
	private final Directionality directionality; 
	private int nextEdgeID = 0;
	private final MutableInt nextNodeAttrID = new MutableInt(0);
	private final MutableInt nextEdgeAttrID = new MutableInt(0);
	private final Map<String,Node> nodes = new HashMap<String,Node>();
	private final Map<String,Edge> edges = new HashMap<String,Edge>();
	private final Map<String,Integer> nodeStaticAttributeIDs = new HashMap<String,Integer>();
	private final Map<String,Integer> nodeDynamicAttributeIDs = new HashMap<String,Integer>();
	private final Map<String,Integer> edgeStaticAttributeIDs = new HashMap<String,Integer>();
	private final Map<String,Integer> edgeDynamicAttributeIDs = new HashMap<String,Integer>();
	
	public DynamicGraph(final Directionality directionality) {
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
		return addEdge(node1ID, node2ID, edgeKey(node1ID, node2ID));
	}
	
	public Edge addEdge(final String node1ID, final String node2ID, final String edgeID) {
		if(!nodes.containsKey(node1ID)) throw new IllegalArgumentException("Node with ID \"" + node1ID + "\" doesn't exist");
		if(!nodes.containsKey(node2ID)) throw new IllegalArgumentException("Node with ID \"" + node2ID + "\" doesn't exist");
		if(edges.containsKey(edgeID)) throw new IllegalArgumentException("Edge with endpoints \"" + node1ID + "\" and \"" + node2ID + "\" already exists");
		final Edge edge = new Edge(String.valueOf(nextEdgeID++), node1ID, node2ID);
		edges.put(edgeID, edge);
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
		
		gexf.append("\t<graph mode=\"dynamic\" defaultedgetype=\"");
		if(directionality.equals(Directionality.UNDIRECTED))
			gexf.append("un");
		gexf.append("directed\">\n");
		
		/*** Attributes Declaration ***/
		
		if(!nodeStaticAttributeIDs.isEmpty()) {
			gexf.append("\t\t<attributes class=\"node\" mode=\"static\">\n");
			for(Entry<String,Integer> entry : nodeStaticAttributeIDs.entrySet()) {
				final String staticAttr = entry.getKey();
				final Integer id = entry.getValue();
				gexf.append("\t\t\t<attribute id=\"" + id + "\" title=\"" + staticAttr + "\" type=\"string\"/>\n");
			}
			gexf.append("\t\t</attributes>\n");
		}
		
		if(!nodeDynamicAttributeIDs.isEmpty()) {
			gexf.append("\t\t<attributes class=\"node\" mode=\"dynamic\">\n");
			for(Entry<String,Integer> entry : nodeDynamicAttributeIDs.entrySet()) {
				final String dynamicAttr = entry.getKey();
				final Integer id = entry.getValue();
				gexf.append("\t\t\t<attribute id=\"" + id + "\" title=\"" + dynamicAttr + "\" type=\"string\"/>\n");
			}
			gexf.append("\t\t</attributes>\n");
		}
		
		if(!edgeStaticAttributeIDs.isEmpty()) {
			gexf.append("\t\t<attributes class=\"edge\" mode=\"static\">\n");
			for(Entry<String,Integer> entry : edgeStaticAttributeIDs.entrySet()) {
				final String staticAttr = entry.getKey();
				final Integer id = entry.getValue();
				gexf.append("\t\t\t<attribute id=\"" + id + "\" title=\"" + staticAttr + "\" type=\"float\"/>\n");
			}
			gexf.append("\t\t</attributes>\n");
		}
		
		if(!edgeDynamicAttributeIDs.isEmpty()) {
			gexf.append("\t\t<attributes class=\"edge\" mode=\"dynamic\">\n");
			for(Entry<String,Integer> entry : edgeDynamicAttributeIDs.entrySet()) {
				final String dynamicAttr = entry.getKey();
				final Integer id = entry.getValue();
				gexf.append("\t\t\t<attribute id=\"" + id + "\" title=\"" + dynamicAttr + "\" type=\"float\"/>\n");
			}
			gexf.append("\t\t</attributes>\n");
		}
		
		/*** Nodes ***/
		gexf.append("\t\t<nodes>\n");
		for(Node node : nodes.values()) {
			gexf.append("\t\t\t");
			gexf.append(node.toGEXF());
			gexf.append("\n");
		}
		gexf.append("\t\t</nodes>\n");
		
		/*** Edges ***/
		gexf.append("\t\t<edges>\n");
		for(Edge edge : edges.values()) {
			gexf.append("\t\t\t");
			gexf.append(edge.toGEXF());
			gexf.append("\n");
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
	
	public void advanceTime() {
		time++;
	}
	
	public Node getNode(final String id) {
		return nodes.get(id);
	}
	
	public Edge getEdge(final String node1ID, final String node2ID) {
		return getEdge(edgeKey(node1ID, node2ID));
	}
	
	public Edge getEdge(final String edgeID) {
		return edges.get(edgeID);
	}
	
	public static void main(String[] args) {
		DynamicGraph g = new DynamicGraph(Directionality.UNDIRECTED);
		final Node n1 = g.addNode("a", "Cool node!");
		n1.enter();
		n1.enterDynamicAttributeValue("dynamism", "55");
		g.advanceTime();
		n1.exit();
		final Node n2 = g.addNode("b", "Other neat node");
		n2.enter();
		final Node n3 = g.addNode("c", "Third node");
		final Edge e1 = g.addEdge("a", "b");
		final Edge e2 = g.addEdge("a", "c");
		final Edge e3 = g.addEdge("b", "c");
		n1.setStaticAttribute("coolness", "4.0");
		
		n2.setStaticAttribute("coolness", "5.0");
		n2.setStaticAttribute("awesomeness", "11.0");
		e1.setStaticAttribute("radnicity", "44");
		System.out.println(g.toGEXF());
	}
	
}
