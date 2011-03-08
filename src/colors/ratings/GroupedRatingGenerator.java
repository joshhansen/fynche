package colors.ratings;

import colors.interfaces.Agent;
import colors.interfaces.Rating;
import colors.interfaces.RatingGenerator;
import colors.util.Counter;
import colors.util.PartitionedSet;

public class GroupedRatingGenerator implements RatingGenerator {
	private final Counter<RatingGenerator> generators;
	
	public GroupedRatingGenerator() {
		this(new Counter<RatingGenerator>());
	}
	
	public GroupedRatingGenerator(Counter<RatingGenerator> generators) {
		this.generators = generators;
	}

	public void addGenerator(final RatingGenerator rateGen, final double weight) {
		generators.setCount(rateGen, weight);
	}

	@Override
	public void generate(Agent rater, PartitionedSet<Rating, Integer> ratings) {
		generators.sample().generate(rater, ratings);
	}
}
