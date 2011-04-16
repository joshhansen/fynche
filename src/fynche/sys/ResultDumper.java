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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import fynche.interfaces.Agent;
import fynche.interfaces.Artefact;

public class ResultDumper extends SysAdaptor {
	private final String outputFilename;
	
	public ResultDumper(String outputFilename) {
		this.outputFilename = outputFilename;
	}
	
	@Override
	public void runFinished(MultiAgentSystem sys) {
		try {
			final BufferedWriter w = new BufferedWriter(new FileWriter(outputFilename));
			for(Agent agent : sys.agents) {
				w.append("Agent: " + agent);
				w.newLine();
				for(int i = 0; i < sys.round(); i++) {
					final Set<Artefact> all = new HashSet<Artefact>();
					for(Agent other : sys.agents) {
						if(agent != other) all.addAll(agent.artefacts(other));
					}
					for(Artefact a : all) {
						w.append('\t');
						w.append(String.valueOf(i));
						w.append('\t');
						w.append(a.toString());
						w.newLine();
					}
				}
			}
			w.flush();
			w.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}