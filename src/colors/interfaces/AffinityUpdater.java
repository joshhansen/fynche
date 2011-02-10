package colors.interfaces;

import colors.util.Counter;


public interface AffinityUpdater {
	public Counter<Agent> newAffinities(final Agent agent);
}
