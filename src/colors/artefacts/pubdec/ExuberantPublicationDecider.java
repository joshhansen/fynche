package colors.artefacts.pubdec;

import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.Factory;
import colors.interfaces.PublicationDecider;
import colors.util.Util.SmartStaticFactory;

public class ExuberantPublicationDecider implements PublicationDecider {
	public static final Factory<PublicationDecider> factory = new SmartStaticFactory<PublicationDecider>(){
		@Override
		protected PublicationDecider instantiate_() {
			return new ExuberantPublicationDecider();
		}
	};
	
	@Override
	public boolean shouldPublish(Agent potentialPublisher, Artefact artefact, Agent toAgent) {
		return true;
	}
}
