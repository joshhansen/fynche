package colors.prefs;

import colors.Factory;
import colors.interfaces.Agent;
import colors.interfaces.PreferenceInitializer;
import colors.interfaces.PreferenceModel;

public class NullPreferenceInitializer implements PreferenceInitializer {
	public static Factory<PreferenceInitializer> factory() {
		return new Factory<PreferenceInitializer>(){
			@Override
			public PreferenceInitializer instantiate() {
				return new NullPreferenceInitializer();
			}
		};
	}
	@Override
	public PreferenceModel initialPreferences(Agent agentA, Agent agentB) {
		return new NullPreferenceModel();
	}
}
