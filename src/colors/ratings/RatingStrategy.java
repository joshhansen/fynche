package colors.ratings;

import colors.agents.Agent;

public interface RatingStrategy {
	public void rate(final Agent rater);
}