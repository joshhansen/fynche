package colors.agents;

import java.util.Random;

import colors.MultiAgentSystem;
import colors.affinities.RandomAffinityCombo;
import colors.artefacts.generators.RandomNamedColorGenerator;
import colors.artefacts.genplans.RandomGenerationPlanner;
import colors.artefacts.initers.NullArtefactCombo;
import colors.artefacts.pubdec.SycophanticPublicationDecider;
import colors.prefs.NullPreferenceCombo;
import colors.prefs.RandomPreferenceInitializer;
import colors.ratings.NullRatingCombo;
import colors.ratings.RandomRatingGenerator;

public class RandomAgent extends ModularAgent {
	private static final long serialVersionUID = 1L;
	
	private static final Random rand = new Random();
	public RandomAgent(MultiAgentSystem sys, final String id) {
		super(sys,id,
			new SycophanticPublicationDecider(5),
			new RandomGenerationPlanner(rand.nextInt(20)),
			NullArtefactCombo.factory.instantiate(),
			new RandomNamedColorGenerator(),
			RandomPreferenceInitializer.factory.instantiate(),
			NullPreferenceCombo.factory.instantiate(),
			NullRatingCombo.factory.instantiate(),
			new RandomRatingGenerator(rand.nextDouble()),
			RandomAffinityCombo.factory.instantiate(),
			RandomAffinityCombo.factory.instantiate());
	}
}
