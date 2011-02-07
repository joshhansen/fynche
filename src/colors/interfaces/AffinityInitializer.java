package colors.interfaces;

import java.util.Map;


public interface AffinityInitializer {
	public Map<Agent,Double> initialAffinities(final Agent agent);
}
