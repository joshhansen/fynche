package colors.artefacts.genplans;

import colors.interfaces.Agent;
import colors.interfaces.GenerationPlanner;

public class FixedGenerationPlanner implements GenerationPlanner {
	private final int numArtefacts;
	
	public FixedGenerationPlanner(int numArtefacts) {
		this.numArtefacts = numArtefacts;
	}

	@Override
	public int numArtefactsToGenerate(Agent agent) {
		return numArtefacts;
	}
}
