package colors.artefacts.genplans;

import colors.interfaces.Agent;
import colors.interfaces.Factory;
import colors.interfaces.GenerationPlanner;
import colors.util.Rand;
import colors.util.Util.SmartStaticFactory;

public class RandomGenerationPlanner implements GenerationPlanner {
	public static Factory<RandomGenerationPlanner> factory(final int max) {
		return new SmartStaticFactory<RandomGenerationPlanner>(){
			@Override
			protected RandomGenerationPlanner instantiate_() {
				return new RandomGenerationPlanner(max);
			}
		};
	}
	
	private final int max;
	public RandomGenerationPlanner(int max) {
		this.max = max;
	}

	@Override
	public int numArtefactsToGenerate(Agent agent) {
		return Rand.nextInt(max);
	}
}
