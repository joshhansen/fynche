package colors.ratings;

import colors.interfaces.Agent;
import colors.interfaces.Factory;
import colors.interfaces.Rating;
import colors.interfaces.RatingGenerator;
import colors.util.PartitionedSet;

public class NullRatingGenerator implements RatingGenerator {
	public static Factory<RatingGenerator> factory() {
		return new Factory<RatingGenerator>(){
			@Override
			public RatingGenerator instantiate() {
				return new NullRatingGenerator();
			}
		};
	}
	@Override
	public void rate(Agent rater, final PartitionedSet<Rating,Integer> ratings) {
		//Do nothing
	}
}
