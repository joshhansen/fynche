package colors.interfaces;

import java.util.Set;

import colors.ratings.SimpleRating;

public interface RatingInitializer {
	public Set<SimpleRating> initialRatings(final Agent agent);
}
