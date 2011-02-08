package colors.interfaces;

import colors.util.PartitionedSet;


public interface RatingGenerator {
	/**
	 * Generate ratings on behalf of <code>rater</code>.
	 * @param rater The agent whose ratings are being generated.
	 * @param ratings The actual ratings datastructure representing all ratings belonging to <code>rater</code>.
	 * This is given explicitly because the <code>ratings</code> field within AbstractAgent should only be
	 * manipulable by code owned by the agent itself. Thus <code>PartitionedSet<Rating,Integer> ratings</code>
	 * is kept protected and must be handed to this <code>RatingGenerator</code> explicitly.
	 */
	public void rate(final Agent rater, final PartitionedSet<Rating,Integer> ratings);
}