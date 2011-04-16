/*
 * Fynche - a Framework for Multiagent Computational Creativity
 * Copyright 2011 Josh Hansen
 * 
 * This file is part of the Fynche <https://github.com/joshhansen/fynche>.
 * 
 * Fynche is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Fynche is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with Fynche.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * If you have inquiries regarding any further use of Fynche, please
 * contact Josh Hansen <http://joshhansen.net/>
 */
package fynche.interfaces;

import fynche.util.PartitionedSet;


public interface RatingGenerator {
	/**
	 * Generate ratings on behalf of <code>rater</code>.
	 * @param rater The agent whose ratings are being generated.
	 * @param ratings The actual ratings datastructure representing all ratings belonging to <code>rater</code>.
	 * This is given explicitly because the <code>ratings</code> field within AbstractAgent should only be
	 * manipulable by code owned by the agent itself. Thus <code>PartitionedSet<Rating,Integer> ratings</code>
	 * is kept protected and must be handed to this <code>RatingGenerator</code> explicitly.
	 */
	public void generate(final Agent rater, final PartitionedSet<Rating,Integer> ratings);
}