package colors.ratings;

import colors.interfaces.Artefact;
import colors.interfaces.Rating;

public class SimpleRating implements Rating {
	private static final long serialVersionUID = 1L;
	
	private final Artefact artefact;
	private final double rating;
	
	public SimpleRating(Artefact artefact, double rating) {
		this.artefact = artefact;
		this.rating = rating;
	}
	
	@Override
	public Artefact getArtefact() {
		return artefact;
	}
	
	@Override
	public double getRating() {
		return rating;
	}
	
}
