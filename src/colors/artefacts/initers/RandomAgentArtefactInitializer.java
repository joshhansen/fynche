package colors.artefacts.initers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import colors.ColorDB;
import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.ArtefactInitializer;

public class RandomAgentArtefactInitializer implements ArtefactInitializer {
	private static final Logger logger = Logger.getLogger(RandomAgentArtefactInitializer.class.getName());
	private final ColorDB db;
	private final List<Integer> topNids;
	private static final Random rand = new Random();
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
		Integer id = topNids.get(rand.nextInt(topNids.size()));
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
