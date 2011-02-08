package colors.interfaces;

import java.io.Serializable;


public interface Rating extends Serializable {

	public abstract Artefact getArtefact();

	public abstract double getRating();

}