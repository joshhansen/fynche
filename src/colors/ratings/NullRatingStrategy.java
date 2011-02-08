package colors.ratings;

import colors.interfaces.Agent;
import colors.interfaces.Factory;
import colors.interfaces.RatingGenerator;

public class NullRatingStrategy implements RatingGenerator {
	public static Factory<RatingGenerator> factory() {
		return new Factory<RatingGenerator>(){
			@Override
			public RatingGenerator instantiate() {
				return new NullRatingStrategy();
			}
		};
	}
	@Override
	public void rate(Agent rater) {
		//Do nothing
	}
}
