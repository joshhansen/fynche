package colors.affinities;

import java.util.Set;

import colors.interfaces.AffinityUpdater;
import colors.interfaces.Agent;
import colors.interfaces.Factory;
import colors.interfaces.Rating;
import colors.util.Counter;
import colors.util.Util.SmartStaticFactory;

public class AverageRatingAffinityUpdater implements AffinityUpdater {
	public static final Factory<AverageRatingAffinityUpdater> factory = new SmartStaticFactory<AverageRatingAffinityUpdater>(){
		@Override
		protected AverageRatingAffinityUpdater instantiate_() {
			return new AverageRatingAffinityUpdater();
		}
	};
	
	@Override
	public Counter<Agent> newAffinities(Agent rater) {
		final Set<Rating> ratings = rater.ratings();
		//If the agent hasn't rated anything yet, default to its previous affinities
		if(ratings.isEmpty()) return rater.affinities();
		
		final Counter<Agent> sums = new Counter<Agent>();
		final Counter<Agent> counts = new Counter<Agent>();
		for(final Rating rating : ratings) {
			final Agent creator = rating.artefactCreator();
			counts.increment(creator);
			sums.increment(creator, rating.rating());
		}
		
		final Counter<Agent> averages = new Counter<Agent>();
		for(Agent agent : sums.keySet()) {
			averages.setCount(agent, sums.getCount(agent) / counts.getCount(agent));
		}
		
		return averages.normalize();
	}

}
