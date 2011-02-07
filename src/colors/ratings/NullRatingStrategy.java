package colors.ratings;

import colors.interfaces.Agent;
import colors.interfaces.RatingStrategy;

public class NullRatingStrategy implements RatingStrategy {
	@Override
	public void rate(Agent rater) {
		//Do nothing
	}
}
