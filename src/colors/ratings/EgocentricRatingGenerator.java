package colors.ratings;

import java.util.logging.Logger;

import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.Rating;

public class EgocentricRatingGenerator extends AbstractRatingGenerator {
	private static final Logger logger = Logger.getLogger(EgocentricRatingGenerator.class.getName());
	@Override
	protected Rating rate(Agent rater, Agent other, Artefact artefact) {
		final double value = rater.preferenceModels().get(rater).preference(artefact);
		logger.finer("Agent " + rater + " rated " + artefact + " as " + value);
		return new SimpleRating(artefact, value);
	}
}
