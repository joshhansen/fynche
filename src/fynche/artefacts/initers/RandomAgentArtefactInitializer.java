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
package fynche.artefacts.initers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import fynche.ColorDB;
import fynche.interfaces.Agent;
import fynche.interfaces.Artefact;
import fynche.interfaces.ArtefactInitializer;
import fynche.util.Rand;

public class RandomAgentArtefactInitializer implements ArtefactInitializer {
	private static final Logger logger = Logger.getLogger(RandomAgentArtefactInitializer.class.getName());
	private final ColorDB db;
	private final List<Integer> topNids;
	private final int maxArtefacts;
	private final boolean orderArtefactsRandomly;
	public RandomAgentArtefactInitializer(final ColorDB db, final int topN, final int maxArtefacts, final boolean orderArtefactsRandomly) {
		this.db = db;
		this.maxArtefacts = maxArtefacts;
		this.orderArtefactsRandomly = orderArtefactsRandomly;
		
		List<Integer> ids = new ArrayList<Integer>();
		try {
			Statement s = db.connection().createStatement();
			final String sql = "select user_id from color_counts limit " + topN + ";";
			ResultSet rs = s.executeQuery(sql);
			
			
			while(rs.next()) {
				ids.add(rs.getInt("user_id"));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		this.topNids = ids;
	}
	
	@Override
	public Set<Artefact> initialArtefacts(Agent agent) {
		logger.finer("Generating initial artefacts for agent " + agent);
		Integer id = topNids.get(Rand.nextInt(topNids.size()));
		Iterator<Integer> colorIDs = db.getUserColorIDs(id, orderArtefactsRandomly);
		
		Set<Artefact> arts = new HashSet<Artefact>();
		int i = 0;
		while(colorIDs.hasNext() && i < maxArtefacts) {
			arts.add(db.getColor(colorIDs.next()));
			i++;
		}
		return arts;
	}
}
