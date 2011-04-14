package colors.sys;

import colors.interfaces.Agent;

public class DefaultAffinityRecorder extends SysAdaptor {
	@Override
	public void roundFinished(MultiAgentSystem sys, int round) {
		for(Agent agent1 : sys.agents) {
			final String id1 = agent1.toString();
			for(Agent agent2 : sys.agents) {
				final String id2 = agent2.toString();
				final double affin1 = agent1.affinities().getCount(agent2);
				recordAffinity(agent1, agent2, round, affin1);
				
				if(id1.compareTo(id2) > 0) {
					final double affin2 = agent2.affinities().getCount(agent1);
					final double affin = 100*(affin1+affin2)/2.0;
					
					recordAverageAffinity(agent1, agent2, round, affin);
				}
			}
		}
	}

	protected void recordAffinity(Agent agent1, Agent agent2, int round, double affinity) {
		
	}
	
	protected void recordAverageAffinity(Agent agent1, Agent agent2, int round, double affinity) {
		
	}
}
