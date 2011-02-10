package colors.ratings;

import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.Rating;

public class EgocentricRatingGenerator extends AbstractRatingGenerator {
	@Override
	protected Rating rate(Agent rater, Agent other, Artefact artefact) {
		final double value = rater.preferenceModels().get(rater).preference(artefact);
		return new SimpleRating(rater, other, artefact, value);
	}
}
