package colors.agents;

import colors.MultiAgentSystem;
import colors.affinities.NullAffinityUpdater;
import colors.affinities.RandomAffinityInitializer;
import colors.artefacts.generators.NullArtefactGenerator;
import colors.artefacts.genplans.NullGenerationPlanner;
import colors.artefacts.initers.NullArtefactInitializer;
import colors.artefacts.pubdec.ExuberantPublicationDecider;
import colors.interfaces.AffinityInitializer;
import colors.interfaces.AffinityUpdater;
import colors.interfaces.Agent;
import colors.interfaces.ArtefactGenerator;
import colors.interfaces.ArtefactInitializer;
import colors.interfaces.Factory;
import colors.interfaces.GenerationPlanner;
import colors.interfaces.PreferenceInitializer;
import colors.interfaces.PreferenceUpdater;
import colors.interfaces.PublicationDecider;
import colors.interfaces.RatingGenerator;
import colors.interfaces.RatingInitializer;
import colors.prefs.NullPreferenceInitializer;
import colors.prefs.NullPreferenceUpdater;
import colors.ratings.NullRatingGenerator;
import colors.ratings.NullRatingInitializer;

public class ModularAgentFactory implements Factory<Agent> {
	private int nextAgentNum = 0;
	
	private Factory<PublicationDecider> publicationDeciderFactory = ExuberantPublicationDecider.factory();
	private Factory<GenerationPlanner> generationPlannerFactory = NullGenerationPlanner.factory();
	
	private Factory<ArtefactInitializer> artefactInitializerFactory = NullArtefactInitializer.factory();
	private Factory<ArtefactGenerator> artefactGeneratorFactory = NullArtefactGenerator.factory();
	
	private Factory<RatingInitializer> ratingInitializerFactory = NullRatingInitializer.factory();
	private Factory<RatingGenerator> ratingStrategyFactory = NullRatingGenerator.factory();
	
	private Factory<AffinityInitializer> affinityInitializerFactory = RandomAffinityInitializer.factory();
	private Factory<AffinityUpdater> affinityUpdaterFactory = NullAffinityUpdater.factory();
	
	private Factory<PreferenceInitializer> preferenceInitializerFactory = NullPreferenceInitializer.factory();
	private Factory<PreferenceUpdater> preferenceUpdaterFactory = NullPreferenceUpdater.factory();
	
	
	
	private MultiAgentSystem system;
	public ModularAgentFactory(final MultiAgentSystem system) {
		this.system = system;
	}
 
	@Override
	public ModularAgent instantiate() {
		final String id = "agent" + nextAgentNum++;
		return new ModularAgent(
			system,id,
			publicationDeciderFactory.instantiate(),
			generationPlannerFactory.instantiate(),
			artefactInitializerFactory.instantiate(),
			artefactGeneratorFactory.instantiate(),
			preferenceInitializerFactory.instantiate(),
			preferenceUpdaterFactory.instantiate(),
			ratingInitializerFactory.instantiate(),
			ratingStrategyFactory.instantiate(),
			affinityInitializerFactory.instantiate(),
			affinityUpdaterFactory.instantiate());
	}

	public Factory<GenerationPlanner> getGenerationPlannerFactory() {
		return generationPlannerFactory;
	}

	public void setGenerationPlannerFactory(
			Factory<GenerationPlanner> generationPlannerFactory) {
		this.generationPlannerFactory = generationPlannerFactory;
	}

	public Factory<PublicationDecider> getPublicationDeciderFactory() {
		return publicationDeciderFactory;
	}

	public void setPublicationDeciderFactory(
			Factory<PublicationDecider> publicationDeciderFactory) {
		this.publicationDeciderFactory = publicationDeciderFactory;
	}

	public Factory<ArtefactInitializer> getArtefactInitializerFactory() {
		return artefactInitializerFactory;
	}

	public void setArtefactInitializerFactory(
			Factory<ArtefactInitializer> artefactInitializerFactory) {
		this.artefactInitializerFactory = artefactInitializerFactory;
	}

	public Factory<ArtefactGenerator> getArtefactGeneratorFactory() {
		return artefactGeneratorFactory;
	}

	public void setArtefactGeneratorFactory(
			Factory<ArtefactGenerator> artefactGeneratorFactory) {
		this.artefactGeneratorFactory = artefactGeneratorFactory;
	}

	public Factory<RatingInitializer> getRatingInitializerFactory() {
		return ratingInitializerFactory;
	}

	public void setRatingInitializerFactory(
			Factory<RatingInitializer> ratingInitializerFactory) {
		this.ratingInitializerFactory = ratingInitializerFactory;
	}

	public Factory<RatingGenerator> getRatingGeneratorFactory() {
		return ratingStrategyFactory;
	}

	public void setRatingGeneratorFactory(
			Factory<RatingGenerator> ratingStrategyFactory) {
		this.ratingStrategyFactory = ratingStrategyFactory;
	}

	public Factory<AffinityInitializer> getAffinityInitializerFactory() {
		return affinityInitializerFactory;
	}

	public void setAffinityInitializerFactory(
			Factory<AffinityInitializer> affinityInitializerFactory) {
		this.affinityInitializerFactory = affinityInitializerFactory;
	}

	public Factory<AffinityUpdater> getAffinityUpdaterFactory() {
		return affinityUpdaterFactory;
	}

	public void setAffinityUpdaterFactory(
			Factory<AffinityUpdater> affinityUpdaterFactory) {
		this.affinityUpdaterFactory = affinityUpdaterFactory;
	}

	public Factory<PreferenceInitializer> getPreferenceInitializerFactory() {
		return preferenceInitializerFactory;
	}

	public void setPreferenceInitializerFactory(
			Factory<PreferenceInitializer> preferenceInitializerFactory) {
		this.preferenceInitializerFactory = preferenceInitializerFactory;
	}

	public Factory<PreferenceUpdater> getPreferenceUpdaterFactory() {
		return preferenceUpdaterFactory;
	}

	public void setPreferenceUpdaterFactory(
			Factory<PreferenceUpdater> preferenceUpdaterFactory) {
		this.preferenceUpdaterFactory = preferenceUpdaterFactory;
	}

	public MultiAgentSystem getSystem() {
		return system;
	}

	public void setSystem(MultiAgentSystem system) {
		this.system = system;
	}
}
