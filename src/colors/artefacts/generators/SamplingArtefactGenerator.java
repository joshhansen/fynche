package colors.artefacts.generators;

import colors.exceptions.ArtefactGenerationException;
import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.ArtefactGenerator;
import colors.interfaces.PreferenceModel;
import colors.interfaces.Sampleable;

public class SamplingArtefactGenerator implements ArtefactGenerator {
	public static class NoSampleableModelException extends ArtefactGenerationException {
		public NoSampleableModelException(String message) {
			super(message);
		}
	}
	@Override
	public Artefact generate(Agent agent) throws ArtefactGenerationException {
		PreferenceModel prefs = agent.preferenceModels().get(agent);
		if(prefs instanceof Sampleable<?>) {
			Sampleable<?> sampleMe = (Sampleable<?>) prefs;
			return (Artefact) sampleMe.sample();
		} else {
			throw new NoSampleableModelException("Didn't find Sampleable preference model");
		}
	}
	
}
