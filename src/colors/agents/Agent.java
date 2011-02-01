package colors.agents;

import java.io.Serializable;
import java.util.Set;

import colors.Artefact;
import colors.Rating;

public interface Agent extends Serializable {
	public abstract void roundStart();
	
	public abstract void create();
	
	public abstract void rate();
	
	public abstract void roundFinish();
	
	public abstract Set<Artefact> publishedArtefacts();
	
	public abstract Set<Artefact> publishedArtefacts(final int roundNum);
	
	public abstract Set<Rating> ratings();
	
	public abstract Set<Rating> ratings(final int roundNum);
}
