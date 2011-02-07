package colors.prefs;

import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.PreferenceModel;

public class NullPreferenceModel implements PreferenceModel {
	@Override
	public double preference(Agent agent, Artefact artefact) {
		return 0;
	}
}
