package colors.artefacts.generators;

import java.util.logging.Logger;

import colors.exceptions.ArtefactGenerationException;
import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.ArtefactGenerator;
import colors.interfaces.PreferenceModel;
import colors.prefs.IndependentKDEPreferenceUpdater.Sampleable;

public class SamplingArtefactGenerator implements ArtefactGenerator {
	private static final Logger logger = Logger.getLogger(SamplingArtefactGenerator.class.getName());
	@Override
	public Artefact generate(Agent agent) throws ArtefactGenerationException {
		PreferenceModel prefs = agent.preferenceModels().get(agent);
		if(prefs instanceof Sampleable) {
			Sampleable<?> sampleMe = (Sampleable<?>) prefs;
			return (Artefact) sampleMe.sample();
		} else {
			throw new ArtefactGenerationException("Didn't find Sampleable preference model");
		}
	}

}
