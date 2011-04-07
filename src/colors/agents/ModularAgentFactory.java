package colors.agents;

import colors.affinities.NullAffinityCombo;
import colors.affinities.RandomAffinityCombo;
import colors.artefacts.generators.RandomNamedColorGenerator;
import colors.artefacts.genplans.FixedGenerationPlanner;
import colors.artefacts.genplans.NullGenerationPlanner;
import colors.artefacts.genplans.RandomGenerationPlanner;
import colors.artefacts.initers.NullArtefactCombo;
import colors.artefacts.pubdec.ExuberantPublicationDecider;
import colors.artefacts.pubdec.SycophanticPublicationDecider;
import colors.interfaces.AffinityInitializer;
import colors.interfaces.AffinityUpdater;
import colors.interfaces.ArtefactGenerator;
import colors.interfaces.ArtefactInitializer;
import colors.interfaces.Factory;
import colors.interfaces.GenerationPlanner;
import colors.interfaces.PreferenceInitializer;
import colors.interfaces.PreferenceUpdater;
import colors.interfaces.PublicationDecider;
import colors.interfaces.RatingGenerator;
import colors.interfaces.RatingInitializer;
import colors.prefs.NullPreferenceCombo;
import colors.prefs.RandomPreferenceInitializer;
import colors.ratings.NullRatingCombo;
import colors.ratings.RandomRatingGenerator;
import colors.sys.MultiAgentSystem;
import colors.util.Rand;

public class ModularAgentFactory implements Factory<ModularAgent> {
	public static ModularAgentFactory dumbAgentFactory(final MultiAgentSystem sys) {
		final ModularAgentFactory maf = new ModularAgentFactory(sys);
		maf.setPublicationDeciderFactory(ExuberantPublicationDecider.factory);
		maf.setGenerationPlannerFactory(FixedGenerationPlanner.factory(1));
		maf.setArtefactInitializerFactory(NullArtefactCombo.factory);
		maf.setArtefactGeneratorFactory(RandomNamedColorGenerator.factory);
		maf.setPreferenceInitializerFactory(NullPreferenceCombo.factory);
		maf.setPreferenceUpdaterFactory(NullPreferenceCombo.factory);
		maf.setRatingInitializerFactory(NullRatingCombo.factory);
		maf.setRatingGeneratorFactory(NullRatingCombo.factory);
		maf.setAffinityInitializerFactory(RandomAffinityCombo.factory);
		maf.setAffinityUpdaterFactory(NullAffinityCombo.factory);
		return maf;
	}
	
	public static ModularAgentFactory randomAgentFactory(final MultiAgentSystem sys) {
		final ModularAgentFactory maf = new ModularAgentFactory(sys);
		maf.setPublicationDeciderFactory(SycophanticPublicationDecider.factory(5));
		maf.setGenerationPlannerFactory(RandomGenerationPlanner.factory(Rand.nextInt(20)));
		maf.setArtefactInitializerFactory(NullArtefactCombo.factory);
		maf.setArtefactGeneratorFactory(RandomNamedColorGenerator.factory);
		maf.setPreferenceInitializerFactory(RandomPreferenceInitializer.factory);
		maf.setPreferenceUpdaterFactory(NullPreferenceCombo.factory);
		maf.setRatingInitializerFactory(NullRatingCombo.factory);
		maf.setRatingGeneratorFactory(RandomRatingGenerator.factory(Rand.nextDouble()));
		maf.setAffinityInitializerFactory(RandomAffinityCombo.factory);
		maf.setAffinityUpdaterFactory(RandomAffinityCombo.factory);
		return maf;
	}
	
	private int nextAgentNum = 0;
	
	private Factory<? extends PublicationDecider> publicationDeciderFactory = ExuberantPublicationDecider.factory;
	private Factory<? extends GenerationPlanner> generationPlannerFactory = NullGenerationPlanner.factory;
	
	private Factory<? extends ArtefactInitializer> artefactInitializerFactory = NullArtefactCombo.factory;
	private Factory<? extends ArtefactGenerator> artefactGeneratorFactory = NullArtefactCombo.factory;
	
	private Factory<? extends RatingInitializer> ratingInitializerFactory = NullRatingCombo.factory;
	private Factory<? extends RatingGenerator> ratingStrategyFactory = NullRatingCombo.factory;
	
	private Factory<? extends AffinityInitializer> affinityInitializerFactory = NullAffinityCombo.factory;
	private Factory<? extends AffinityUpdater> affinityUpdaterFactory = NullAffinityCombo.factory;
	
	private Factory<? extends PreferenceInitializer> preferenceInitializerFactory = NullPreferenceCombo.factory;
	private Factory<? extends PreferenceUpdater> preferenceUpdaterFactory = NullPreferenceCombo.factory;
	
	
	
	private MultiAgentSystem system;
	public ModularAgentFactory(final MultiAgentSystem system) {
		this.system = system;
	}
 
	@Override
	public ModularAgent instantiate() {
		final String id = "agent" + nextAgentNum++;
		return instantiate(id);
	}
	
	public ModularAgent instantiate(final String agentID) {
		return new ModularAgent(
			system,agentID,
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

	public Factory<? extends GenerationPlanner> getGenerationPlannerFactory() {
		return generationPlannerFactory;
	}

	public void setGenerationPlannerFactory(
			Factory<? extends GenerationPlanner> generationPlannerFactory) {
		this.generationPlannerFactory = generationPlannerFactory;
	}

	public Factory<? extends PublicationDecider> getPublicationDeciderFactory() {
		return publicationDeciderFactory;
	}

	public void setPublicationDeciderFactory(
			Factory<? extends PublicationDecider> publicationDeciderFactory) {
		this.publicationDeciderFactory = publicationDeciderFactory;
	}

	public Factory<? extends ArtefactInitializer> getArtefactInitializerFactory() {
		return artefactInitializerFactory;
	}

	public void setArtefactInitializerFactory(
			Factory<? extends ArtefactInitializer> artefactInitializerFactory) {
		this.artefactInitializerFactory = artefactInitializerFactory;
	}

	public Factory<? extends ArtefactGenerator> getArtefactGeneratorFactory() {
		return artefactGeneratorFactory;
	}

	public void setArtefactGeneratorFactory(
			Factory<? extends ArtefactGenerator> artefactGeneratorFactory) {
		this.artefactGeneratorFactory = artefactGeneratorFactory;
	}

	public Factory<? extends RatingInitializer> getRatingInitializerFactory() {
		return ratingInitializerFactory;
	}

	public void setRatingInitializerFactory(
			Factory<? extends RatingInitializer> ratingInitializerFactory) {
		this.ratingInitializerFactory = ratingInitializerFactory;
	}

	public Factory<? extends RatingGenerator> getRatingGeneratorFactory() {
		return ratingStrategyFactory;
	}

	public void setRatingGeneratorFactory(
			Factory<? extends RatingGenerator> ratingStrategyFactory) {
		this.ratingStrategyFactory = ratingStrategyFactory;
	}

	public Factory<? extends AffinityInitializer> getAffinityInitializerFactory() {
		return affinityInitializerFactory;
	}

	public void setAffinityInitializerFactory(
			Factory<? extends AffinityInitializer> affinityInitializerFactory) {
		this.affinityInitializerFactory = affinityInitializerFactory;
	}

	public Factory<? extends AffinityUpdater> getAffinityUpdaterFactory() {
		return affinityUpdaterFactory;
	}

	public void setAffinityUpdaterFactory(
			Factory<? extends AffinityUpdater> affinityUpdaterFactory) {
		this.affinityUpdaterFactory = affinityUpdaterFactory;
	}

	public Factory<? extends PreferenceInitializer> getPreferenceInitializerFactory() {
		return preferenceInitializerFactory;
	}

	public void setPreferenceInitializerFactory(
			Factory<? extends PreferenceInitializer> preferenceInitializerFactory) {
		this.preferenceInitializerFactory = preferenceInitializerFactory;
	}

	public Factory<? extends PreferenceUpdater> getPreferenceUpdaterFactory() {
		return preferenceUpdaterFactory;
	}

	public void setPreferenceUpdaterFactory(
			Factory<? extends PreferenceUpdater> preferenceUpdaterFactory) {
		this.preferenceUpdaterFactory = preferenceUpdaterFactory;
	}

	public MultiAgentSystem getSystem() {
		return system;
	}

	public void setSystem(MultiAgentSystem system) {
		this.system = system;
	}
}
