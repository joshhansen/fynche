/**
 * 
 */
package colors.sys;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import colors.interfaces.Agent;
import colors.interfaces.Artefact;

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