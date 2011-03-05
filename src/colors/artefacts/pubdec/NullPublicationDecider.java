package colors.artefacts.pubdec;

import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.Factory;
import colors.interfaces.PublicationDecider;

public class NullPublicationDecider implements PublicationDecider {
	public static Factory<PublicationDecider> factory() {
		return new Factory<PublicationDecider>(){
			@Override
			public PublicationDecider instantiate() {
				return new NullPublicationDecider();
			}};
	}
	@Override
	public boolean shouldPublish(Agent potentialPublisher, Artefact artefact, Agent toAgent) {
		return false;
	}
}
