package colors.agents;

import colors.MultiAgentSystem;
import colors.affinities.AffinityInitializer;
import colors.affinities.AffinityUpdater;
import colors.artefacts.Artefact;
import colors.artefacts.generators.ArtefactGenerator;
import colors.artefacts.genplans.GenerationPlanner;
import colors.artefacts.pubdec.PublicationDecider;
import colors.ratings.RatingStrategy;

public class ModularAgent extends AbstractAgent {
	private static final long serialVersionUID = 1L;
	
	private final AffinityInitializer affinityIniter;
	private final GenerationPlanner genPlan;
	private final PublicationDecider pubDec;
	private final ArtefactGenerator artGen;
	private final AffinityUpdater affinityUpdater;
	private final RatingStrategy ratingStrategy;

	public ModularAgent(MultiAgentSystem sys,
			AffinityInitializer affinityIniter, GenerationPlanner genPlan,
			PublicationDecider pubDec, ArtefactGenerator artGen,
			AffinityUpdater affinityUpdater, RatingStrategy ratingStrategy) {
		super(sys);
		this.affinityIniter = affinityIniter;
		this.genPlan = genPlan;
		this.pubDec = pubDec;
		this.artGen = artGen;
		this.affinityUpdater = affinityUpdater;
		this.ratingStrategy = ratingStrategy;
	}

	@Override
	public void setUp() {
		affinities = affinityIniter.initialAffinities(this);
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
	}

	@Override
	public void takeDown() {
		// TODO Auto-generated method stub
	}
}
