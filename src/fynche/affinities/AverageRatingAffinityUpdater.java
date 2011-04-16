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

import java.util.Set;

import fynche.interfaces.AffinityUpdater;
import fynche.interfaces.Agent;
import fynche.interfaces.Factory;
import fynche.interfaces.Rating;
import fynche.util.Counter;
import fynche.util.Util.SmartStaticFactory;

public class AverageRatingAffinityUpdater implements AffinityUpdater {
	public static final Factory<AverageRatingAffinityUpdater> factory = new SmartStaticFactory<AverageRatingAffinityUpdater>(){
		@Override
		protected AverageRatingAffinityUpdater instantiate_() {
			return new AverageRatingAffinityUpdater();
		}
	};
	
	@Override
	public Counter<Agent> newAffinities(Agent rater) {
		final Set<Rating> ratings = rater.ratings();
		//If the agent hasn't rated anything yet, default to its previous affinities
		if(ratings.isEmpty()) return rater.affinities();
		
		final Counter<Agent> sums = new Counter<Agent>();
		final Counter<Agent> counts = new Counter<Agent>();
		for(final Rating rating : ratings) {
			final Agent creator = rating.artefactCreator();
			counts.increment(creator);
			sums.increment(creator, rating.rating());
		}
		
		final Counter<Agent> averages = new Counter<Agent>();
		for(Agent agent : sums.keySet()) {
			averages.setCount(agent, sums.getCount(agent) / counts.getCount(agent));
		}
		
		return averages.normalize();
	}

}
