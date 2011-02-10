package colors.agents;

import colors.MultiAgentSystem;
import colors.affinities.RandomAffinityInitializer;
import colors.affinities.RandomAffinityUpdater;
import colors.artefacts.generators.RandomNamedColorGenerator;
import colors.artefacts.genplans.RandomGenerationPlanner;
import colors.artefacts.pubdec.RandomPublicationDecider;
import colors.prefs.NullPreferenceUpdater;
import colors.prefs.RandomPreferenceInitializer;
import colors.ratings.RandomRatingGenerator;

public class RandomAgent extends ModularAgent {
	private static final long serialVersionUID = 1L;
	public RandomAgent(MultiAgentSystem sys) {
		super(sys,
				new RandomGenerationPlanner(20),
				new RandomNamedColorGenerator(),
				new RandomPublicationDecider(0.5),
				new RandomRatingGenerator(0.5),
				new RandomAffinityInitializer(),
				new RandomAffinityUpdater(),
				new RandomPreferenceInitializer(),
				new NullPreferenceUpdater());
	}
}
