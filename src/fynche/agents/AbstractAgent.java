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
package fynche.agents;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fynche.interfaces.Agent;
import fynche.interfaces.Artefact;
import fynche.interfaces.PreferenceModel;
import fynche.interfaces.Rating;
import fynche.sys.MultiAgentSystem;
import fynche.util.Counter;
import fynche.util.PartitionedSet;

public abstract class AbstractAgent implements Agent {
	private static final long serialVersionUID = 1L;
	
	protected final MultiAgentSystem sys;
	protected final String id;
	protected Counter<Agent> affinities = new Counter<Agent>();
	protected Map<Agent,PreferenceModel> preferenceModels = new HashMap<Agent,PreferenceModel>();
	protected final PartitionedSet<Artefact,Integer> artefacts = new PartitionedSet<Artefact,Integer>();
	protected final Map<Agent,PartitionedSet<Artefact,Integer>> publishedArtefacts = new HashMap<Agent,PartitionedSet<Artefact,Integer>>(){
		@Override
		public PartitionedSet<Artefact, Integer> get(Object key) {
			PartitionedSet<Artefact, Integer> value = super.get(key);
			if(value == null) {
				value = new PartitionedSet<Artefact,Integer>();
				super.put((Agent) key, value);
			}
			return value;
		}
	};
	protected final PartitionedSet<Rating,Integer> ratings = new PartitionedSet<Rating,Integer>();

	public AbstractAgent(final MultiAgentSystem sys, final String id) {
		this.sys = sys;
		this.id = id;
	}
	
	public MultiAgentSystem system() {
		return sys;
	}

	@Override
	public Set<Artefact> artefacts() {
		return artefacts.all();
	}

        @Override
        public Set<Artefact> artefacts(final int roundNum) {
            return artefacts.partition(roundNum);
        }
	
	@Override
	public Set<Artefact> artefacts(Agent inquirer) {
		return publishedArtefacts.get(inquirer).all();
	}

	@Override
	public Set<Artefact> artefacts(Agent inquirer, int roundNum) {
		return publishedArtefacts.get(inquirer).partition(roundNum);
	}

	@Override
	public Set<Rating> ratings() {
		return ratings.all();
	}

	@Override
	public Set<Rating> ratings(int roundNum) {
		return ratings.partition(roundNum);
	}
	
	@Override
	public Counter<Agent> affinities() {
		return affinities;
	}

	public Map<Agent, PreferenceModel> preferenceModels() {
		return preferenceModels;
	}
	
	public String toString() {
		return id;
	}
	
//	public PreferenceModel preferences() {
//		return prefs;
//	}
	
//	public void create() {
//		for(int i = 0; i < numArtefactsToGenerate(); i++) {
//			Artefact a = generateArtefact();
//			if(shouldPublish(a))
//				publishedArtefacts.add(a, sys.round());
//		}
//	}
//	
//	protected abstract int numArtefactsToGenerate();
//	protected abstract Artefact generateArtefact();
//	protected abstract boolean shouldPublish(final Artefact a);

}
