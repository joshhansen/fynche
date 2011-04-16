package colors;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import colors.util.Directionality;
import colors.util.Graph;

public class CSVtoGEXF {
	public static void main(String[] args) {
		final String name = "high_uniform";
		final String filename = System.getenv("HOME") + "/Courses/cs673/affinity_configs/" + name + ".csv";
		final String gexfFilename = System.getenv("HOME") + "/Courses/cs673/affinity_configs/" + name + ".gexf";
		final Graph g = new Graph(Directionality.DIRECTED);
		try {
			final BufferedReader r = new BufferedReader(new FileReader(filename));
			final List<String> agents = new ArrayList<String>(Arrays.asList(r.readLine().split(",")));
			agents.remove(0);
			for(final String agent : agents)
				g.addNode(agent, agent);
			
			String tmp = null;
			while( (tmp=r.readLine()) != null) {
				String[] parts = tmp.split(",");
				final String agent = parts[0];
				final List<String> affins = new ArrayList<String>(Arrays.asList(parts));
				affins.remove(0);
				
				for(int i = 0; i < affins.size(); i++) {
					final String otherAgent = agents.get(i);
					if(!agent.equals(otherAgent)) {
						final Double affin = Double.valueOf(affins.get(i));
						g.addEdge(agent, otherAgent).setAttribute("affinity", affin);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.saveToGEXF(gexfFilename);
	}

}
