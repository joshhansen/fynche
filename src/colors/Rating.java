package colors;

import java.io.Serializable;

import colors.interfaces.Artefact;

public interface Rating extends Serializable {

	public abstract Artefact getArtefact();

	public abstract double getRating();

}