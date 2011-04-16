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

import fynche.interfaces.Agent;
import fynche.interfaces.Artefact;
import fynche.interfaces.Factory;
import fynche.interfaces.Rating;
import fynche.util.Rand;
import fynche.util.Util.SmartStaticFactory;

public class RandomRatingGenerator extends AbstractRatingGenerator {
	public static Factory<RandomRatingGenerator> factory(final double p) {
		return new SmartStaticFactory<RandomRatingGenerator>(){
			@Override
			protected RandomRatingGenerator instantiate_() {
				return new RandomRatingGenerator(p);
			}
		};
	}
	
	private final double p;
	
	public RandomRatingGenerator() {
		this(1.0);
	}
	
	/**
	 * 
	 * @param p Probability of producing a rating
	 */
	public RandomRatingGenerator(double p) {
		if(p < 0.0 || p > 1.0)
			throw new IllegalArgumentException("p must be a probability (0.0 <= p <= 1.0)");
		this.p = p;
	}

	@Override
	protected Rating rate(Agent rater, Agent other, Artefact artefact) {
		return new SimpleRating(rater, other, artefact, Rand.nextDouble());
	}
	
	@Override
	protected boolean shouldRate(Agent rater, Agent other, Artefact artefact) {
		return Rand.nextDouble() <= p;
	}

}
