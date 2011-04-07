package colors.sys;

public interface SysListener {
	public void setupStarted(final MultiAgentSystem sys);
	public void runStarted(final MultiAgentSystem sys);
	public void runFinished(final MultiAgentSystem sys);
	public void roundStarted(final MultiAgentSystem sys, final int round);
	public void roundFinished(final MultiAgentSystem sys, final int round);
}
