package colors;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class Agent implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected final Set<Artefact> publishedArtefacts = new HashSet<Artefact>();
	protected final Set<Rating> ratings = new HashSet<Rating>();
	
	public abstract void roundStart();
	
	public abstract void create();
	
	public abstract void rate();
	
	public abstract void roundFinish();
	
	public Set<Artefact> publishedArtefacts() {
		return Collections.unmodifiableSet(publishedArtefacts);
	}
	
	public Set<Rating> ratings() {
		return Collections.unmodifiableSet(ratings);
	}
}
