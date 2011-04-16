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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import fynche.interfaces.Agent;

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
