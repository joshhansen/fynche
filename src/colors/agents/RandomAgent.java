package colors.agents;

import java.util.Random;

import colors.MultiAgentSystem;
import colors.affinities.RandomAffinityInitializer;
import colors.affinities.RandomAffinityUpdater;
import colors.artefacts.generators.RandomNamedColorGenerator;
import colors.artefacts.genplans.RandomGenerationPlanner;
import colors.artefacts.initers.NullArtefactInitializer;
import colors.artefacts.pubdec.RandomPublicationDecider;
import colors.artefacts.pubdec.SycophanticPublicationDecider;
import colors.prefs.NullPreferenceUpdater;
import colors.prefs.RandomPreferenceInitializer;
import colors.ratings.NullRatingInitializer;
import colors.ratings.RandomRatingGenerator;

public class RandomAgent extends ModularAgent {
	private static final long serialVersionUID = 1L;
	
	private static final Random rand = new Random();
	public RandomAgent(MultiAgentSystem sys, final String id) {
		super(sys,id,
			new SycophanticPublicationDecider(5),
			new RandomGenerationPlanner(rand.nextInt(20)),
			new NullArtefactInitializer(),
			new RandomNamedColorGenerator(),
			new RandomPreferenceInitializer(),
			new NullPreferenceUpdater(),
			new NullRatingInitializer(),
			new RandomRatingGenerator(rand.nextDouble()),
			new RandomAffinityInitializer(),
			new RandomAffinityUpdater(rand.nextDouble()));
	}
}
