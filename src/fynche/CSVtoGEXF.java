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
package fynche;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fynche.util.Directionality;
import fynche.util.Graph;

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
