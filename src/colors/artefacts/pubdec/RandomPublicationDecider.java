package colors.artefacts.pubdec;

import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.PublicationDecider;
import colors.util.Rand;

public class RandomPublicationDecider implements PublicationDecider {
	private final double p;
	
	public RandomPublicationDecider(double p) {
		this.p = p;
	}

	@Override
	public boolean shouldPublish(Agent potentialPublisher, Artefact artefact, Agent toAgent) {
		return Rand.nextDouble() <= p;
	}

}
