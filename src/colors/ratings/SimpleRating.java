package colors.ratings;

import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.Rating;

public class SimpleRating implements Rating {
	private static final long serialVersionUID = 1L;
	
	private final Agent rater;
	private final Agent artefactCreator;
	private final Artefact artefact;
	private final double rating;
	
	public SimpleRating(Agent rater, Agent artefactCreator, Artefact artefact, double rating) {
		this.rater = rater;
		this.artefactCreator = artefactCreator;
		this.artefact = artefact;
		this.rating = rating;
	}

	@Override
	public Artefact artefact() {
		return artefact;
	}
	
	@Override
	public double rating() {
		return rating;
	}

	@Override
	public Agent rater() {
		return rater;
	}

	@Override
	public Agent artefactCreator() {
		return artefactCreator;
	}
}
