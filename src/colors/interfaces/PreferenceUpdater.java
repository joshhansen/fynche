package colors.interfaces;


public interface PreferenceUpdater {
	/**
	 * Generates an updated PreferenceModel representing <code>agentA</code>'s estimate of
	 * <code>agentB</code>'s preferences.
	 */
	public PreferenceModel newPreferences(final Agent agentA, final Agent agentB);
}
