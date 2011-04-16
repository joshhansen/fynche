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
package fynche.artefacts.initers;

import java.util.Collections;
import java.util.Set;

import fynche.interfaces.Agent;
import fynche.interfaces.Artefact;
import fynche.interfaces.ArtefactGenerator;
import fynche.interfaces.ArtefactInitializer;
import fynche.interfaces.Factory;
import fynche.util.Util.SmartStaticFactory;

public class NullArtefactCombo implements ArtefactInitializer, ArtefactGenerator {
	public static final Factory<NullArtefactCombo> factory = new SmartStaticFactory<NullArtefactCombo>(){
		@Override
		protected NullArtefactCombo instantiate_() {
			return new NullArtefactCombo();
		}
	};
	
	@Override
	public Set<Artefact> initialArtefacts(Agent agent) {
		return Collections.emptySet();
	}
	
	@Override
	public Artefact generate(Agent agent) {
		throw new UnsupportedOperationException("This generator doesn't actually generate.");
	}
}
