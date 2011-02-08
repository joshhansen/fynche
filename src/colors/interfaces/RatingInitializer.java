package colors.interfaces;

import java.util.Set;

import colors.SimpleRating;

public interface RatingInitializer {
	public Set<SimpleRating> initialRatings(final Agent agent);
}
