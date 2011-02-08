package colors.prefs;

import colors.interfaces.Agent;
import colors.interfaces.Factory;
import colors.interfaces.PreferenceModel;
import colors.interfaces.PreferenceUpdater;

public class NullPreferenceUpdater implements PreferenceUpdater {
	public static Factory<PreferenceUpdater> factory() {
		return new Factory<PreferenceUpdater>(){
			@Override
			public PreferenceUpdater instantiate() {
				return new NullPreferenceUpdater();
			}
		};
	}
	@Override
	public PreferenceModel newPreferences(Agent agentA, Agent agentB) {
		return agentA.preferenceModels().get(agentB);
	}
}
