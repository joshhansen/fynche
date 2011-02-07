package colors.artefacts.generators;

import colors.interfaces.Artefact;
import colors.interfaces.ArtefactGenerator;

public class NullArtefactGenerator implements ArtefactGenerator {
	@Override
	public Artefact generate() {
		throw new UnsupportedOperationException("This generator doesn't actually generate.");
	}
}
