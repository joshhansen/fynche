package colors.artefacts.generators;

import java.util.logging.Logger;

import colors.exceptions.ArtefactGenerationException;
import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.ArtefactGenerator;
import colors.interfaces.PreferenceModel;
import colors.prefs.IndependentKDEPreferenceUpdater.Sampleable;

public class SamplingArtefactGenerator<A extends Artefact> implements ArtefactGenerator {
	private static final Logger logger = Logger.getLogger(SamplingArtefactGenerator.class.getName());
	@Override
	public A generate(Agent agent) throws ArtefactGenerationException {
		PreferenceModel prefs = agent.preferenceModels().get(agent);
		if(prefs instanceof Sampleable) {
			Sampleable<A> sampleMe = (Sampleable<A>) prefs;
			A artefact = sampleMe.sample();
			logger.finer("Agent " + agent + " generated artefact " + artefact);
			return artefact;
		} else {
			logger.warning("Didn't find Sampleable preference model; no artefact generated");
			throw new ArtefactGenerationException("Didn't find Sampleable preference model; no artefact generated");
		}
	}

}
