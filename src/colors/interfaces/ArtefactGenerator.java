package colors.interfaces;

import colors.exceptions.ArtefactGenerationException;


public interface ArtefactGenerator {
	/**
	 * Generate an artefact on behalf of <code>agent</code>.
	 * @param agent
	 * @return
	 */
	public Artefact generate(final Agent agent) throws ArtefactGenerationException;
}
