package colors.artefacts.pubdec;

import colors.artefacts.Artefact;

public interface PublicationDecider {
	public boolean shouldPublish(final Artefact artefact);
}