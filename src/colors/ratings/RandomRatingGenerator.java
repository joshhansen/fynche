package colors.ratings;

import java.util.Random;

import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.Rating;

public class RandomRatingGenerator extends AbstractRatingGenerator {
	private static final Random rand = new Random();
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
		return new SimpleRating(rater, other, artefact, rand.nextDouble());
	}
	
	@Override
	protected boolean shouldRate(Agent rater, Agent other, Artefact artefact) {
		return rand.nextDouble() <= p;
	}

}
