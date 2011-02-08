package colors.interfaces;


public interface PreferenceModel {
	/**
	 * Returns a number indicating degree of preference for Artefact <code>artefact</code>
	 */
	public double preference(final Artefact artefact);
}
