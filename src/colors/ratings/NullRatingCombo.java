package colors.ratings;

import java.util.Collections;
import java.util.Set;

import colors.interfaces.Agent;
import colors.interfaces.Factory;
import colors.interfaces.Rating;
import colors.interfaces.RatingGenerator;
import colors.interfaces.RatingInitializer;
import colors.util.PartitionedSet;
import colors.util.Util.SmartStaticFactory;

public class NullRatingCombo implements RatingInitializer, RatingGenerator {
	public static final Factory<NullRatingCombo> factory = new SmartStaticFactory<NullRatingCombo>(){
		@Override
		protected NullRatingCombo instantiate_() {
			return new NullRatingCombo();
		}
	};
	
	@Override
	public Set<Rating> initialRatings(Agent agent) {
		return Collections.emptySet();
	}
	
	@Override
	public void generate(Agent rater, final PartitionedSet<Rating,Integer> ratings) {
		//Do nothing
	}
}
