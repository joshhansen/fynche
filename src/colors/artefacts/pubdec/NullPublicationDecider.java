package colors.artefacts.pubdec;

import colors.interfaces.Artefact;
import colors.interfaces.PublicationDecider;

public class NullPublicationDecider implements PublicationDecider {
	@Override
	public boolean shouldPublish(Artefact artefact) {
		return false;
	}
}
