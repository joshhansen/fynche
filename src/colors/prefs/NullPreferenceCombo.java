package colors.prefs;

import colors.interfaces.Agent;
import colors.interfaces.Factory;
import colors.interfaces.PreferenceInitializer;
import colors.interfaces.PreferenceModel;
import colors.interfaces.PreferenceUpdater;
import colors.util.Util.SmartStaticFactory;

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
