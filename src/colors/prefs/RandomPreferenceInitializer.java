package colors.prefs;

import java.util.Random;

import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.PreferenceInitializer;
import colors.interfaces.PreferenceModel;

public class RandomPreferenceInitializer implements PreferenceInitializer {
	private static final Random rand = new Random();
	@Override
	public PreferenceModel initialPreferences(Agent agentA, Agent agentB) {
		return new PreferenceModel() {
			@Override
			public double preference(Artefact artefact) {
				return rand.nextDouble();
			}
		};
	}

}
