package colors.ratings;

import java.util.Collections;
import java.util.Set;

import colors.interfaces.Agent;
import colors.interfaces.Factory;
import colors.interfaces.RatingInitializer;

public class NullRatingInitializer implements RatingInitializer {
	public static Factory<RatingInitializer> factory() {
		return new Factory<RatingInitializer>(){
			@Override
			public RatingInitializer instantiate() {
				return new NullRatingInitializer();
			}
		};
	}
	@Override
	public Set<SimpleRating> initialRatings(Agent agent) {
		return Collections.emptySet();
	}
}
