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
package fynche.artefacts.generators;

import java.util.Map.Entry;

import fynche.exceptions.ArtefactGenerationException;
import fynche.interfaces.Agent;
import fynche.interfaces.Artefact;
import fynche.interfaces.ArtefactGenerator;
import fynche.interfaces.PreferenceModel;
import fynche.interfaces.Sampleable;
import fynche.util.Counter;

/**
 * Generates artefacts using the preferences of another.
 */
public class CopycatArtefactGenerator implements ArtefactGenerator {
	@Override
	public Artefact generate(Agent agent) throws ArtefactGenerationException {
		final Counter<Sampleable<?>> affins = new Counter<Sampleable<?>>();
		for(Entry<Agent,Double> affin : agent.affinities().entrySet()) {
			Agent other = affin.getKey();
			final PreferenceModel preferenceModel = other.preferenceModels().get(other);
			if(preferenceModel instanceof Sampleable)
				affins.setCount((Sampleable<?>)preferenceModel, affin.getValue());
		}
		
		if(affins.isEmpty())
			throw new ArtefactGenerationException("No neighbors had sampleable preferences");
		if(affins.totalCount() == 0.0)
			throw new ArtefactGenerationException("Affinity towards all neighbors was zero");
		
		return (Artefact) affins.sample().sample();
	}
}
