package colors.agents;

import colors.MultiAgentSystem;
import colors.affinities.NullAffinityUpdater;
import colors.affinities.RandomAffinityInitializer;
import colors.artefacts.generators.RandomNamedColorGenerator;
import colors.artefacts.genplans.FixedGenerationPlanner;
import colors.artefacts.initers.NullArtefactInitializer;
import colors.artefacts.pubdec.ExuberantPublicationDecider;
import colors.prefs.NullPreferenceInitializer;
import colors.prefs.NullPreferenceUpdater;
import colors.ratings.NullRatingGenerator;
import colors.ratings.NullRatingInitializer;

public class DumbAgent extends ModularAgent {
	private static final long serialVersionUID = 1L;
	public DumbAgent(MultiAgentSystem sys, final String id) {
		super(sys,id,
				new ExuberantPublicationDecider(),
				new FixedGenerationPlanner(1),
				new NullArtefactInitializer(),
				new RandomNamedColorGenerator(),
				new NullPreferenceInitializer(),
				new NullPreferenceUpdater(),
				new NullRatingInitializer(),
				new NullRatingGenerator(),
				new RandomAffinityInitializer(),
				new NullAffinityUpdater());
	}
}
