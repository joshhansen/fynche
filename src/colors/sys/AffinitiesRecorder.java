package colors.sys;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Comparator;

import colors.interfaces.Agent;
import colors.util.Counter;

public class AffinitiesRecorder extends SysAdaptor {
	private final Writer affinitiesOutput;
	public AffinitiesRecorder(final String outputFilename) {
		Writer output = null;
		try {
			output = new BufferedWriter(new FileWriter(outputFilename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.affinitiesOutput = output;
	}
	
	@Override
	public void roundFinished(MultiAgentSystem sys, int round) {
		if(affinitiesOutput != null) {
			recordAffinities(sys);
		}
	}

	private void recordAffinities(MultiAgentSystem sys) {
		final StringBuilder affinities_ = new StringBuilder();
		final Agent[] agentsArr = sys.agents.toArray(new Agent[0]);
		Arrays.sort(agentsArr, new Comparator<Agent>(){
			@Override
			public int compare(Agent o1, Agent o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});
		affinities_.append('\t');
		for(int i = 0; i < agentsArr.length; i++) {
			final Agent agent = agentsArr[i];
			affinities_.append(agent);
			if(i < agentsArr.length-1) affinities_.append('\t');
		}
		affinities_.append('\n');
		
		for(Agent agentA : agentsArr) {
			final Counter<Agent> affins = agentA.affinities();
			affinities_.append(agentA);
			affinities_.append('\t');
			for(int i = 0; i < agentsArr.length; i++) {
//			for(Agent agentB : agentsArr) {
				final Agent agentB = agentsArr[i];
				affinities_.append(affins.getCount(agentB));
				if(i < agentsArr.length-1) affinities_.append('\t');
			}
			affinities_.append('\n');
		}

		try {
			affinitiesOutput.append(affinities_);
			affinitiesOutput.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void runStarted(MultiAgentSystem sys) {
		recordAffinities(sys);//Save the initial state
	}
}