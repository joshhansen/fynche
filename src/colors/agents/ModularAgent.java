package colors.agents;

import java.util.logging.Logger;

import colors.MultiAgentSystem;
import colors.artefacts.generators.SamplingArtefactGenerator.NoSampleableModelException;
import colors.exceptions.ArtefactGenerationException;
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
import colors.interfaces.RatingGenerator;
import colors.interfaces.RatingInitializer;

public class ModularAgent extends AbstractAgent {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ModularAgent.class.getName());
	
	private final PublicationDecider pubDec;
	private final GenerationPlanner genPlan;
	private final ArtefactInitializer artIniter;
	private final ArtefactGenerator artGen;
	private final PreferenceInitializer prefIniter;
	private final PreferenceUpdater prefUpdater;
	private final RatingInitializer ratingIniter;
	private final RatingGenerator ratingGenerator;
	private final AffinityInitializer affinityIniter;
	private final AffinityUpdater affinityUpdater;
	
	public ModularAgent(
			final MultiAgentSystem sys,
			final String id,
			final PublicationDecider pubDec,
			final GenerationPlanner genPlan,
			final ArtefactInitializer artIniter,
			final ArtefactGenerator artGen, 
			final PreferenceInitializer prefIniter,
			final PreferenceUpdater prefUpdater,
			final RatingInitializer ratingIniter, 
			final RatingGenerator ratingStrategy,
			final AffinityInitializer affinityIniter,
			final AffinityUpdater affinityUpdater) {
		super(sys, id);
		this.pubDec = pubDec;
		this.genPlan = genPlan;
		this.artIniter = artIniter;
		this.artGen = artGen;
		this.prefIniter = prefIniter;
		this.prefUpdater = prefUpdater;
		this.ratingIniter = ratingIniter;
		this.ratingGenerator = ratingStrategy;
		this.affinityIniter = affinityIniter;
		this.affinityUpdater = affinityUpdater;
	}

	@Override
	public void setUp() {
		logger.fine("Setting up agent " + this);
		this.artefacts.add(artIniter.initialArtefacts(this), 0);
		this.ratings.add(ratingIniter.initialRatings(this), 0);
		
		affinities = affinityIniter.initialAffinities(this);
		
		for(Agent other : sys.agents()) {
			preferenceModels.put(other, prefIniter.initialPreferences(this, other));
		}
	}
	
	@Override
	public void roundStart() {
		//Do nothing
	}
	
	public void create() {
		for(int i = 0; i < genPlan.numArtefactsToGenerate(this); i++) {
			try {
				Artefact a = artGen.generate(this);
				final StringBuilder msg = new StringBuilder();
				msg.append("Agent ");
				msg.append(this.toString());
				msg.append(" generated artefact ");
				msg.append(a.toString());
				artefacts.add(a, sys.round());
				for(final Agent other : sys.agents()) {
					if(other != this && pubDec.shouldPublish(this, a, other))
						publishedArtefacts.get(other).add(a, sys.round());
				}
				logger.fine(msg.toString());
			} catch(NoSampleableModelException e) {
				if(sys.round() > 0)//It's normal to not find a sampleable model on the first round, so only worry about it later
					logger.severe("Error generating artefact: " + e.getMessage());
			} catch(ArtefactGenerationException e) {
				logger.severe("Error generating artefact: " + e.getMessage());
			}
		}
	}
	
	@Override
	public void rate() {
		ratingGenerator.generate(this, ratings);
	}
	
	@Override
	public void roundFinish() {
		affinities = affinityUpdater.newAffinities(this);
		logger.fine("affinities(" + this + ") => " + affinities);
		
		for(Agent other : sys.agents()) {
			preferenceModels.put(other, prefUpdater.newPreferences(this, other));
		}
	}

	@Override
	public void takeDown() {
		//Do nothing
	}
}
