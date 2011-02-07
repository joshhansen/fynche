package colors.prefs;

import colors.interfaces.Agent;
import colors.interfaces.PreferenceInitializer;
import colors.interfaces.PreferenceModel;

public class NullPreferenceInitializer implements PreferenceInitializer {
	@Override
	public PreferenceModel initialPreferences(Agent agentA, Agent agentB) {
		return new NullPreferenceModel();
	}
}
