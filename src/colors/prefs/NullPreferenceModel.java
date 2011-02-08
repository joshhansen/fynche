package colors.prefs;

import colors.interfaces.Artefact;
import colors.interfaces.PreferenceModel;

public class NullPreferenceModel implements PreferenceModel {
	@Override
	public double preference(Artefact artefact) {
		return 0;
	}
}
