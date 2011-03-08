package colors.ratings;

import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.Factory;
import colors.interfaces.Rating;
import colors.util.Rand;
import colors.util.Util.SmartStaticFactory;

public class RandomRatingGenerator extends AbstractRatingGenerator {
	public static Factory<RandomRatingGenerator> factory(final double p) {
		return new SmartStaticFactory<RandomRatingGenerator>(){
			@Override
			protected RandomRatingGenerator instantiate_() {
				return new RandomRatingGenerator(p);
			}
		};
	}
	
	private final double p;
	
	public RandomRatingGenerator() {
		this(1.0);
	}
	
	/**
	 * 
	 * @param p Probability of producing a rating
	 */
	public RandomRatingGenerator(double p) {
		if(p < 0.0 || p > 1.0)
			throw new IllegalArgumentException("p must be a probability (0.0 <= p <= 1.0)");
		this.p = p;
	}

	@Override
	protected Rating rate(Agent rater, Agent other, Artefact artefact) {
		return new SimpleRating(rater, other, artefact, Rand.nextDouble());
	}
	
	@Override
	protected boolean shouldRate(Agent rater, Agent other, Artefact artefact) {
		return Rand.nextDouble() <= p;
	}

}
