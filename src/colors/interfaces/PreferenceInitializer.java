package colors.interfaces;


public interface PreferenceInitializer {
	/**
	 * Generates an initial PreferenceModel representing <code>agentA</code>'s estimate of
	 * <code>agentB</code>'s preferences.
	 */
	public PreferenceModel initialPreferences(final Agent agentA, final Agent agentB);
}
