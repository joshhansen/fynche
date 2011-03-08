package colors.artefacts.pubdec;

import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.PublicationDecider;
import colors.util.Counter;

public class GroupedPublicationDecider implements PublicationDecider {
	private final Counter<PublicationDecider> generators;
	
	public GroupedPublicationDecider() {
		this(new Counter<PublicationDecider>());
	}
	
	public GroupedPublicationDecider(Counter<PublicationDecider> generators) {
		this.generators = generators;
	}

	public void addDecider(final PublicationDecider pubDec, final double weight) {
		generators.setCount(pubDec, weight);
	}

	@Override
	public boolean shouldPublish(Agent potentialPublisher, Artefact artefact, Agent toAgent) {
		return generators.sample().shouldPublish(potentialPublisher, artefact, toAgent);
	}
}
