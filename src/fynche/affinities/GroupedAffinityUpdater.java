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
package fynche.affinities;

import fynche.interfaces.AffinityUpdater;
import fynche.interfaces.Agent;
import fynche.util.Counter;

public class GroupedAffinityUpdater implements AffinityUpdater {
	protected final Counter<AffinityUpdater> generators;
	
	public GroupedAffinityUpdater() {
		this(new Counter<AffinityUpdater>());
	}
	
	public GroupedAffinityUpdater(Counter<AffinityUpdater> generators) {
		this.generators = generators;
	}

	public void addGenerator(final AffinityUpdater artGen, final double weight) {
		generators.setCount(artGen, weight);
	}

	@Override
	public Counter<Agent> newAffinities(Agent agent) {
		return generators.sample().newAffinities(agent);
	}
}
