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
package fynche.interfaces;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import fynche.sys.MultiAgentSystem;
import fynche.util.Counter;

public interface Agent extends Serializable {
	/**
	 * The parent system in which this agent is operating
	 * @return The system
	 */
	public MultiAgentSystem system();
	
	/**
	 * Run before the simulation begins
	 */
	public void setUp();
	
	/**
	 * Run at the start of each simulation round
	 */
	public void roundStart();
	
	/**
	 * The first major step in each simulation round. Brings about creation and publication
	 * of artefacts.
	 */
	public void create();
	
	/**
	 * The second major step in each simulation round. Brings about creation of ratings,
	 * by which this agent rates the work of other agents.
	 */
	public void rate();
	
	/**
	 * Run at the end of each simulation round
	 */
	public void roundFinish();
	
	/**
	 * Run after the simulation completes
	 */
	public void takeDown();
	
	public Set<Artefact> artefacts();
//	
//	public Set<Artefact> artefacts(final int roundNum);
	
	/**
	 * This agent returns a set of agents that it wishes to make available to agent <code>inquirer</code>.
	 */
	public Set<Artefact> artefacts(final Agent inquirer);
	
	/**
	 * This agent returns a set of agents that it wishes to make available to agent <code>inquirer</code>
	 * in round <code>roundNum</code>.
	 */
	public Set<Artefact> artefacts(final Agent inquirer, final int roundNum);

        public Set<Artefact> artefacts(final int roundNum);

	public Set<Rating> ratings();
	
	public Set<Rating> ratings(final int roundNum);
	
	public Counter<Agent> affinities();
	
	public Map<Agent,PreferenceModel> preferenceModels();
}
