package colors.prefs;

import java.util.Random;

import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.Factory;
import colors.interfaces.PreferenceInitializer;
import colors.interfaces.PreferenceModel;
import colors.util.Util.SmartStaticFactory;

public class RandomPreferenceInitializer implements PreferenceInitializer {
	public static final Factory<RandomPreferenceInitializer> factory = new SmartStaticFactory<RandomPreferenceInitializer>(){
		@Override
		protected RandomPreferenceInitializer instantiate_() {
			return new RandomPreferenceInitializer();
		}
	};
	
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
