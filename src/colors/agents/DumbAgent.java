package colors.agents;

import colors.MultiAgentSystem;
import colors.affinities.NullAffinityUpdater;
import colors.affinities.RandomAffinityInitializer;
import colors.artefacts.generators.RandomNamedColorGenerator;
import colors.artefacts.genplans.FixedGenerationPlanner;
import colors.artefacts.pubdec.ExuberantPublicationDecider;
import colors.prefs.NullPreferenceInitializer;
import colors.prefs.NullPreferenceUpdater;
import colors.ratings.NullRatingStrategy;

public class DumbAgent extends ModularAgent {
	private static final long serialVersionUID = 1L;
	public DumbAgent(MultiAgentSystem sys) {
		super(sys,
				new FixedGenerationPlanner(1),
				new RandomNamedColorGenerator(),
				new ExuberantPublicationDecider(),
				new NullRatingStrategy(),
				new RandomAffinityInitializer(),
				new NullAffinityUpdater(),
				new NullPreferenceInitializer(),
				new NullPreferenceUpdater());
	}
}
