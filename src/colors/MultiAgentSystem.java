package colors;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import colors.interfaces.Agent;

public class MultiAgentSystem implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(MultiAgentSystem.class.getName());
	
	private static MultiAgentSystem system;
	public static MultiAgentSystem instance() {
		if(system==null)
			system = new MultiAgentSystem();
		return system;
	}
	
	
	private int round = 0;
	private Set<RoundFinishedListener> roundFinishedListeners = new HashSet<RoundFinishedListener>();
	private Set<RunFinishedListener> runFinishedListeners = new HashSet<RunFinishedListener>();
	private MultiAgentSystem() {
	}
	
	public int round() {
		return round;
	}
	
	final Set<Agent> agents = new HashSet<Agent>();
	public Set<Agent> agents() {
		return Collections.unmodifiableSet(agents);
	}
	
	public void addAgent(final Agent agent) {
		agents.add(agent);
	}
	
	public void addRoundFinishedListener(final RoundFinishedListener rfl) {
		this.roundFinishedListeners.add(rfl);
	}
	
	public void addRunFinishedListener(final RunFinishedListener rfl) {
		this.runFinishedListeners.add(rfl);
	}
	
	/**
	 * 
	 * @param iterations How many iterations to run for
	 */
	public void run(final int iterations) {
		logger.info("Going to run for " + iterations + " iterations");
		logger.info("Setup");
		for(Agent agent : agents) {
			agent.setUp();
		}
		for(int i = 0; i < iterations; i++) {
			if(i % 10 == 0) System.out.print(" " + i);
			else System.out.print('.');
			
			logger.info("-----Starting round " + i + "-----");
			for(Agent agent : agents) {
				agent.roundStart();
			}
			
			logger.info("Beginning artefact generation");
			for(Agent agent : agents) {
				agent.create();
			}
			logger.info("Beginning rating generation");
			for(Agent agent : agents) {
				agent.rate();
			}
			
			logger.info("-----Finishing round " + i + "-----");
			for(Agent agent : agents) {
				agent.roundFinish();
			}
			fireRoundFinished();
//			updateGraphAffinities();
			round++;
		}
		
		logger.info("Beginning takedown");
		for(Agent agent : agents) {
			agent.takeDown();
		}
		fireRunFinished();
	}

	private void fireRoundFinished() {
		for(RoundFinishedListener rfl : roundFinishedListeners)
			rfl.roundFinished(this, round);
	}

	private void fireRunFinished() {
		for(RunFinishedListener rfl : runFinishedListeners)
			rfl.runFinished(this);
	}
	
	public void step() {
		run(1);
	}
}
