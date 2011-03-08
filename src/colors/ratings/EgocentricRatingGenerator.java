package colors.ratings;

import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.Factory;
import colors.interfaces.Rating;
import colors.util.Util.SmartStaticFactory;

public class EgocentricRatingGenerator extends AbstractRatingGenerator {
	public static final Factory<EgocentricRatingGenerator> factory = new SmartStaticFactory<EgocentricRatingGenerator>(){
		@Override
		protected EgocentricRatingGenerator instantiate_() {
			return new EgocentricRatingGenerator();
		}
	};
	
	@Override
	protected Rating rate(Agent rater, Agent other, Artefact artefact) {
		final double value = rater.preferenceModels().get(rater).preference(artefact);
		return new SimpleRating(rater, other, artefact, value);
	}
}
