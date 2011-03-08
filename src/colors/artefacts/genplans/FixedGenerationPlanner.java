package colors.artefacts.genplans;

import colors.interfaces.Agent;
import colors.interfaces.Factory;
import colors.interfaces.GenerationPlanner;
import colors.util.Util.SmartStaticFactory;

public class FixedGenerationPlanner implements GenerationPlanner {
	public static Factory<FixedGenerationPlanner> factory(final int numArtefacts) {
		return new SmartStaticFactory<FixedGenerationPlanner>(){
			@Override
			protected FixedGenerationPlanner instantiate_() {
				return new FixedGenerationPlanner(numArtefacts);
			}
		};
	}
	
	private final int numArtefacts;
	
	public FixedGenerationPlanner(int numArtefacts) {
		this.numArtefacts = numArtefacts;
	}

	@Override
	public int numArtefactsToGenerate(Agent agent) {
		return numArtefacts;
	}
}
