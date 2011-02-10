package colors.interfaces;

import colors.util.Counter;


public interface AffinityInitializer {
	public Counter<Agent> initialAffinities(final Agent agent);
}
