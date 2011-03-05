package colors.interfaces;

import java.io.Serializable;

/**
 * @author Josh Hansen
 *
 */
public interface Rating extends Serializable {
	public Agent rater();
	public Agent artefactCreator();
	public Artefact artefact();
	public double rating();
}