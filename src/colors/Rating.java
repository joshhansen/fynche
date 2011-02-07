package colors;

import java.io.Serializable;

import colors.interfaces.Artefact;

public class Rating implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final Artefact artefact;
	private final double rating;
	
	public Rating(Artefact artefact, double rating) {
		this.artefact = artefact;
		this.rating = rating;
	}
	
	public Artefact getArtefact() {
		return artefact;
	}
	
	public double getRating() {
		return rating;
	}
	
}
