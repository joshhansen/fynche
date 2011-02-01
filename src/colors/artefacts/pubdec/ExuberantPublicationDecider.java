package colors.artefacts.pubdec;

import colors.artefacts.Artefact;

public class ExuberantPublicationDecider implements PublicationDecider {
	@Override
	public boolean shouldPublish(Artefact artefact) {
		return true;
	}
}
