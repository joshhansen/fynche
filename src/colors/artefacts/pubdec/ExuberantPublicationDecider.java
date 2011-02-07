package colors.artefacts.pubdec;

import colors.interfaces.Artefact;
import colors.interfaces.PublicationDecider;

public class ExuberantPublicationDecider implements PublicationDecider {
	@Override
	public boolean shouldPublish(Artefact artefact) {
		return true;
	}
}
