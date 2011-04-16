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
package fynche.ratings;

import java.util.logging.Logger;

import fynche.interfaces.Agent;
import fynche.interfaces.Artefact;
import fynche.interfaces.Rating;
import fynche.interfaces.RatingGenerator;
import fynche.util.PartitionedSet;

public abstract class AbstractRatingGenerator implements RatingGenerator {
	private final Logger logger = Logger.getLogger(AbstractRatingGenerator.class.getName());
	@Override
	public void generate(Agent rater, final PartitionedSet<Rating,Integer> ratings) {
		logger.fine("Generating ratings for agent " + rater);
		final int currentRound = rater.system().round();
		for(Agent other : rater.system().agents()) {
			if(other != rater) {
				for(Artefact artefact : other.artefacts(rater, currentRound - 1)) {
					if(shouldRate(rater, other, artefact)) {
						Rating rating = rate(rater, other, artefact);
						if(rating != null) {
	//						logger.finer("Agent " + rater + " rated " + artefact + " as " + rating.rating());
							ratings.add(rating, currentRound);
						}
					}
				}
			}
		}
	}
	
	protected boolean shouldRate(final Agent rater, final Agent other, final Artefact artefact) {
		return true;
	}
	
	/**
	 * 
	 * @param rater
	 * @param other
	 * @param artefact
	 * @return A Rating representing <code>rater</code>'s assessment of <code>other</code>'s artefact <code>artefact</code>.
	 */
	protected abstract Rating rate(Agent rater, Agent other, Artefact artefact);
}
