package colors.artefacts.genplans;

import colors.interfaces.Agent;
import colors.interfaces.GenerationPlanner;

public class NullGenerationPlanner implements GenerationPlanner {
	@Override
	public int numArtefactsToGenerate(Agent agent) {
		return 0;
	}
}
