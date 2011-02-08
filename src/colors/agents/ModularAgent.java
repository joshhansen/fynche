package colors.agents;

import colors.MultiAgentSystem;
import colors.artefacts.NullArtefactInitializer;
import colors.interfaces.AffinityInitializer;
import colors.interfaces.AffinityUpdater;
import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.ArtefactGenerator;
import colors.interfaces.ArtefactInitializer;
import colors.interfaces.GenerationPlanner;
import colors.interfaces.PreferenceInitializer;
import colors.interfaces.PreferenceUpdater;
import colors.interfaces.PublicationDecider;
import colors.interfaces.RatingInitializer;
import colors.interfaces.RatingGenerator;
import colors.ratings.NullRatingInitializer;

public class ModularAgent extends AbstractAgent {
	private static final long serialVersionUID = 1L;
	
	private final AffinityInitializer affinityIniter;
	private final AffinityUpdater affinityUpdater;
	private final GenerationPlanner genPlan;
	private final PublicationDecider pubDec;
	private final ArtefactGenerator artGen;
	private final RatingGenerator ratingStrategy;
	private final PreferenceInitializer prefIniter;
	private final PreferenceUpdater prefUpdater;
	private final ArtefactInitializer artIniter;
	private final RatingInitializer ratingIniter;
	public ModularAgent(
			MultiAgentSystem sys,
			GenerationPlanner genPlan,
			ArtefactGenerator artGen,
			PublicationDecider pubDec, 
			RatingGenerator ratingStrategy,
			AffinityInitializer affinityIniter,
			AffinityUpdater affinityUpdater, 
			PreferenceInitializer prefIniter,
			PreferenceUpdater prefUpdater) {
		this(sys, genPlan, artGen, pubDec, ratingStrategy, affinityIniter, affinityUpdater, prefIniter, prefUpdater,
			new NullArtefactInitializer(), new NullRatingInitializer());
	}
	
	public ModularAgent(
			MultiAgentSystem sys,
			GenerationPlanner genPlan,
			ArtefactGenerator artGen,
			PublicationDecider pubDec, 
			RatingGenerator ratingStrategy,
			AffinityInitializer affinityIniter,
			AffinityUpdater affinityUpdater, 
			PreferenceInitializer prefIniter,
			PreferenceUpdater prefUpdater,
			ArtefactInitializer artIniter,
			RatingInitializer ratingIniter) {
		super(sys);
		this.affinityIniter = affinityIniter;
		this.genPlan = genPlan;
		this.pubDec = pubDec;
		this.artGen = artGen;
		this.affinityUpdater = affinityUpdater;
		this.ratingStrategy = ratingStrategy;
		this.prefIniter = prefIniter;
		this.prefUpdater = prefUpdater;
		this.ratingIniter = ratingIniter;
		this.artIniter = artIniter;
	}

	@Override
	public void setUp() {
		this.publishedArtefacts.add(artIniter.initialArtefacts(this), 0);
		this.ratings.add(ratingIniter.initialRatings(this), 0);
		
		affinities = affinityIniter.initialAffinities(this);
		
		for(Agent other : sys.agents()) {
			preferenceModels.put(other, prefIniter.initialPreferences(this, other));
		}
	}
	
	@Override
	public void roundStart() {
		
	}
	
	public void create() {
		for(int i = 0; i < genPlan.numArtefactsToGenerate(this); i++) {
			Artefact a = artGen.generate();
			if(pubDec.shouldPublish(a))
				publishedArtefacts.add(a, sys.round());
		}
	}
	
	@Override
	public void rate() {
		ratingStrategy.rate(this);
	}
	
	@Override
	public void roundFinish() {
		affinities = affinityUpdater.newAffinities(this);
		for(Agent other : sys.agents()) {
			preferenceModels.put(other, prefUpdater.newPreferences(this, other));
		}
	}

	@Override
	public void takeDown() {
		// TODO Auto-generated method stub
	}
}
