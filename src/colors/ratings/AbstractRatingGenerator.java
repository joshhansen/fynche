package colors.ratings;

import java.util.logging.Logger;

import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.Rating;
import colors.interfaces.RatingGenerator;
import colors.util.PartitionedSet;

public abstract class AbstractRatingGenerator implements RatingGenerator {
	private final Logger logger = Logger.getLogger(AbstractRatingGenerator.class.getName());
	@Override
	public void generate(Agent rater, final PartitionedSet<Rating,Integer> ratings) {
		logger.fine("Generating ratings for agent " + rater);
		final int currentRound = rater.system().round();
		for(Agent other : rater.system().agents()) {
			if(other != rater) {
				for(Artefact artefact : other.artefacts(rater, currentRound - 1)) {
					if(shouldRate(rater, other, artefact)) {
						Rating rating = rate(rater, other, artefact);
						if(rating != null) {
	//						logger.finer("Agent " + rater + " rated " + artefact + " as " + rating.rating());
							ratings.add(rating, currentRound);
						}
					}
				}
			}
		}
	}
	
	protected boolean shouldRate(final Agent rater, final Agent other, final Artefact artefact) {
		return true;
	}
	
	/**
	 * 
	 * @param rater
	 * @param other
	 * @param artefact
	 * @return A Rating representing <code>rater</code>'s assessment of <code>other</code>'s artefact <code>artefact</code>.
	 */
	protected abstract Rating rate(Agent rater, Agent other, Artefact artefact);
}
