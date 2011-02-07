package colors.interfaces;


public interface PreferenceModel {
	/**
	 * Returns a number indicating how much Agent <code>agent</code> likes Artefact <code>artefact</code>
	 */
	public double preference(final Agent agent, final Artefact artefact);
}
