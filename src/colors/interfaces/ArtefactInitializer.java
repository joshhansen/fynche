package colors.interfaces;

import java.util.Set;

public interface ArtefactInitializer {
	/**
	 * @return The set of artefacts that an agent starts out with
	 */
	public Set<Artefact> initialArtefacts(final Agent agent);
}
