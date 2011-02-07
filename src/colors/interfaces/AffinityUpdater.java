package colors.interfaces;

import java.util.Map;


public interface AffinityUpdater {
	public Map<Agent,Double> newAffinities(final Agent agent);
}
