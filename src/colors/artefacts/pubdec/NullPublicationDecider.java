package colors.artefacts.pubdec;

import colors.artefacts.Artefact;

public class NullPublicationDecider implements PublicationDecider {
	@Override
	public boolean shouldPublish(Artefact artefact) {
		return false;
	}
}
