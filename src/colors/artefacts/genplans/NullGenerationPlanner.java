package colors.artefacts.genplans;

import colors.interfaces.Agent;
import colors.interfaces.Factory;
import colors.interfaces.GenerationPlanner;
import colors.util.Util.SmartStaticFactory;

public class NullGenerationPlanner implements GenerationPlanner {
	public static final Factory<GenerationPlanner> factory = new SmartStaticFactory<GenerationPlanner>(){
		@Override
		protected GenerationPlanner instantiate_() {
			return new NullGenerationPlanner();
		}
	};
	
	@Override
	public int numArtefactsToGenerate(Agent agent) {
		return 0;
	}
}
