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
package fynche.prefs;

import fynche.interfaces.Agent;
import fynche.interfaces.Factory;
import fynche.interfaces.PreferenceInitializer;
import fynche.interfaces.PreferenceModel;
import fynche.interfaces.PreferenceUpdater;
import fynche.util.Util.SmartStaticFactory;

public class NullPreferenceCombo implements PreferenceInitializer, PreferenceUpdater {
	public static final Factory<NullPreferenceCombo> factory = new SmartStaticFactory<NullPreferenceCombo>(){
		@Override
		protected NullPreferenceCombo instantiate_() {
			return new NullPreferenceCombo();
		}
	};
	
	@Override
	public PreferenceModel initialPreferences(Agent agentA, Agent agentB) {
		return new NullPreferenceModel();
	}
	
	@Override
	public PreferenceModel newPreferences(Agent agentA, Agent agentB) {
		return agentA.preferenceModels().get(agentB);
	}
}
