package colors.ratings;

import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.Factory;
import colors.interfaces.Rating;
import colors.util.Util.SmartStaticFactory;

public class EccentricRatingGenerator extends AbstractRatingGenerator {
	public static final Factory<EccentricRatingGenerator> factory = new SmartStaticFactory<EccentricRatingGenerator>(){
		@Override
		protected EccentricRatingGenerator instantiate_() {
			return new EccentricRatingGenerator();
		}
	};
	
	@Override
	protected Rating rate(Agent rater, Agent other, Artefact artefact) {
		final double value = rater.preferenceModels().get(rater).preference(artefact);
		return new SimpleRating(rater, other, artefact, 1.0-value);
	}
}
