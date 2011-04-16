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
import fynche.interfaces.Rating;

public class SimpleRating implements Rating {
	private static final long serialVersionUID = 1L;
	
	private final Agent rater;
	private final Agent artefactCreator;
	private final Artefact artefact;
	private final double rating;
	
	public SimpleRating(Agent rater, Agent artefactCreator, Artefact artefact, double rating) {
		if(rater == artefactCreator) throw new IllegalArgumentException("Somebody is rating themselves. This is probably not what is wanted.");
		this.rater = rater;
		this.artefactCreator = artefactCreator;
		this.artefact = artefact;
		this.rating = rating;
	}

	@Override
	public Artefact artefact() {
		return artefact;
	}
	
	@Override
	public double rating() {
		return rating;
	}

	@Override
	public Agent rater() {
		return rater;
	}

	@Override
	public Agent artefactCreator() {
		return artefactCreator;
	}
}
