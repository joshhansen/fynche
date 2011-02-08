package colors.artefacts.genplans;

import colors.Factory;
import colors.interfaces.Agent;
import colors.interfaces.GenerationPlanner;

public class NullGenerationPlanner implements GenerationPlanner {
	public static Factory<GenerationPlanner> factory() {
		return new Factory<GenerationPlanner>(){
			@Override
			public GenerationPlanner instantiate() {
				return new NullGenerationPlanner();
			}
		};
	}
	@Override
	public int numArtefactsToGenerate(Agent agent) {
		return 0;
	}
}
