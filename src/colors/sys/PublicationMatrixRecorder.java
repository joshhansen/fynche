package colors.sys;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Comparator;

import colors.interfaces.Agent;

public class PublicationMatrixRecorder extends SysAdaptor {
	private final Writer pubMatrixOutput;
	public PublicationMatrixRecorder(final String outputFilename) {
		Writer output = null;
		try {
			output = new BufferedWriter(new FileWriter(outputFilename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.pubMatrixOutput = output;
	}
	@Override
	public void roundFinished(MultiAgentSystem sys, int round) {
		if(pubMatrixOutput != null) {
			final StringBuilder publication_ = new StringBuilder();
			final Agent[] agentsArr = sys.agents.toArray(new Agent[0]);
			Arrays.sort(agentsArr, new Comparator<Agent>(){
				@Override
				public int compare(Agent o1, Agent o2) {
					return o1.toString().compareTo(o2.toString());
				}
			});
			publication_.append('\t');
			for(int i = 0; i < agentsArr.length; i++) {
				final Agent agent = agentsArr[i];
				publication_.append(agent);
				if(i < agentsArr.length-1) publication_.append('\t');
			}
			publication_.append('\n');
			
			for(Agent agentA : agentsArr) {
				
//				final Counter<Agent> affins = agentA.affinities();
				publication_.append(agentA);
				publication_.append('\t');
				for(int i = 0; i < agentsArr.length; i++) {
	//			for(Agent agentB : agentsArr) {
					final Agent agentB = agentsArr[i];
					final int publishedCount = agentA.artefacts(agentB, round).size();
					publication_.append(publishedCount);
					if(i < agentsArr.length-1) publication_.append('\t');
				}
				publication_.append('\n');
			}
		
			try {
				pubMatrixOutput.append(publication_);
				pubMatrixOutput.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}