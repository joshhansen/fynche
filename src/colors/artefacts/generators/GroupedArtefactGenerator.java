package colors.artefacts.generators;

import colors.exceptions.ArtefactGenerationException;
import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.ArtefactGenerator;
import colors.util.Counter;

public class GroupedArtefactGenerator implements ArtefactGenerator {
	private final Counter<ArtefactGenerator> generators;
	
	public GroupedArtefactGenerator() {
		this(new Counter<ArtefactGenerator>());
	}
	
	public GroupedArtefactGenerator(Counter<ArtefactGenerator> generators) {
		this.generators = generators;
	}

	@Override
	public Artefact generate(Agent agent) throws ArtefactGenerationException {
		return generators.sample().generate(agent);
	}

	public void addGenerator(final ArtefactGenerator artGen, final double weight) {
		generators.setCount(artGen, weight);
	}
}
