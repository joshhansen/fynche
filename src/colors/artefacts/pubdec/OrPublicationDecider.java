package colors.artefacts.pubdec;

import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.PublicationDecider;

public class OrPublicationDecider implements PublicationDecider {
	private final PublicationDecider[] deciders;
	
	public OrPublicationDecider(PublicationDecider... deciders) {
		this.deciders = deciders;
	}

	@Override
	public boolean shouldPublish(Agent potentialPublisher, Artefact artefact, Agent toAgent) {
		for(PublicationDecider decider : deciders) {
			if(decider.shouldPublish(potentialPublisher, artefact, toAgent))
				return true;
		}
		return false;
	}

}
