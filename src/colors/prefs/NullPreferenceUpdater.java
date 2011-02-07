package colors.prefs;

import colors.interfaces.Agent;
import colors.interfaces.PreferenceModel;
import colors.interfaces.PreferenceUpdater;

public class NullPreferenceUpdater implements PreferenceUpdater {
	@Override
	public PreferenceModel newPreferences(Agent agentA, Agent agentB) {
		return agentA.preferenceModels().get(agentB);
	}
}
