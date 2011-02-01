package colors.artefacts.genplans;

import colors.agents.Agent;

public class NullGenerationPlanner implements GenerationPlanner {
	@Override
	public int numArtefactsToGenerate(Agent agent) {
		return 0;
	}
}
