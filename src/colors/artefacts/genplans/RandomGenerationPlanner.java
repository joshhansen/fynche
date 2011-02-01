package colors.artefacts.genplans;

import java.util.Random;

import colors.agents.Agent;

public class RandomGenerationPlanner implements GenerationPlanner {
	private final int max;
	private final Random rand = new Random();
	public RandomGenerationPlanner(int max) {
		this.max = max;
	}

	@Override
	public int numArtefactsToGenerate(Agent agent) {
		return rand.nextInt(max);
	}
}
