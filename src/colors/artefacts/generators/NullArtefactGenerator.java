package colors.artefacts.generators;

import colors.artefacts.Artefact;

public class NullArtefactGenerator implements ArtefactGenerator {
	@Override
	public Artefact generate() {
		throw new UnsupportedOperationException("This generator doesn't actually generate.");
	}
}
