package colors;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import colors.agents.Agent;

public class MultiAgentSystem implements Serializable {
	private static final long serialVersionUID = 1L;
	private static MultiAgentSystem system;
	public static MultiAgentSystem instance() {
		if(system==null)
			system = new MultiAgentSystem();
		return system;
	}
	
	
	private int round = 0;
	private MultiAgentSystem() {
	}
	
	private final Set<Agent> agents = new HashSet<Agent>();
	public Set<Agent> agents() {
		return Collections.unmodifiableSet(agents);
	}
	
	public void addAgent(final Agent agent) {
		agents.add(agent);
	}
	
	/**
	 * 
	 * @param iterations How many iterations to run for
	 */
	public void run(final int iterations) {
		for(int i = 0; i < iterations; i++) {
			for(Agent agent : agents) {
				agent.roundStart();
			}
			for(Agent agent : agents) {
				agent.create();
			}
			for(Agent agent : agents) {
				agent.rate();
			}
			for(Agent agent : agents) {
				agent.roundFinish();
			}
			round++;
		}
	}
	
	public void step() {
		run(1);
	}
	
	public static void main(String[] args) {
		final MultiAgentSystem sys = new MultiAgentSystem();
		
	}
}
