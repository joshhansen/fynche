package colors.artefacts.generators;

import java.util.Map.Entry;

import colors.exceptions.ArtefactGenerationException;
import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.ArtefactGenerator;
import colors.interfaces.PreferenceModel;
import colors.interfaces.Sampleable;
import colors.util.Counter;

/**
 * Generates artefacts using the preferences of another.
 */
public class CopycatArtefactGenerator implements ArtefactGenerator {
	@Override
	public Artefact generate(Agent agent) throws ArtefactGenerationException {
		final Counter<Sampleable<?>> affins = new Counter<Sampleable<?>>();
		for(Entry<Agent,Double> affin : agent.affinities().entrySet()) {
			Agent other = affin.getKey();
			final PreferenceModel preferenceModel = other.preferenceModels().get(other);
			if(preferenceModel instanceof Sampleable)
				affins.setCount((Sampleable<?>)preferenceModel, affin.getValue());
		}
		
		if(affins.isEmpty())
			throw new ArtefactGenerationException("No neighbors had sampleable preferences");
		
		return (Artefact) affins.sample().sample();
	}
}
