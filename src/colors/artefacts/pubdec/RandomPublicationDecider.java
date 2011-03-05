package colors.artefacts.pubdec;

import java.util.Random;

import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.PublicationDecider;

public class RandomPublicationDecider implements PublicationDecider {
	private static final Random rand = new Random();
	private final double p;
	
	public RandomPublicationDecider(double p) {
		this.p = p;
	}

	@Override
	public boolean shouldPublish(Agent potentialPublisher, Artefact artefact, Agent toAgent) {
		return rand.nextDouble() <= p;
	}

}
