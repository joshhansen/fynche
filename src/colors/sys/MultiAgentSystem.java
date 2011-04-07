package colors.sys;

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
	private Set<SysListener> listeners = new HashSet<SysListener>();
//	private Set<RoundListener> roundListeners = new HashSet<RoundListener>();
//	private Set<RunListener> runListeners = new HashSet<RunListener>();
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
	
//	public void addRoundFinishedListener(final RoundListener rfl) {
//		this.roundListeners.add(rfl);
//	}
//	
//	public void addRunFinishedListener(final RunListener rfl) {
//		this.runListeners.add(rfl);
//	}
	
	public void addListener(final SysListener sl) {
		listeners.add(sl);
	}
	
	/**
	 * 
	 * @param iterations How many iterations to run for
	 */
	public void run(final int iterations) {
		logger.info("Going to run for " + iterations + " iterations");
		logger.info("Setup");
		fireSetupStarted();
		for(Agent agent : agents) {
			agent.setUp();
		}
		fireRunStarted();
		for(int i = 0; i < iterations; i++) {
			fireRoundStarted();
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
			round++;
		}
		
		logger.info("Beginning takedown");
		for(Agent agent : agents) {
			agent.takeDown();
		}
		fireRunFinished();
	}
	
	private void fireSetupStarted() {
		for(SysListener sl : listeners) {
			sl.setupStarted(this);
		}
	}
	
	private void fireRoundStarted() {
		for(SysListener sl : listeners)
			sl.roundStarted(this, round);
	}

	private void fireRoundFinished() {
		for(SysListener sl : listeners)
			sl.roundFinished(this, round);
	}
	
	private void fireRunStarted() {
		for(SysListener sl : listeners)
			sl.runStarted(this);
	}

	private void fireRunFinished() {
		for(SysListener sl : listeners)
			sl.runFinished(this);
	}
	
	public void step() {
		run(1);
	}
}
