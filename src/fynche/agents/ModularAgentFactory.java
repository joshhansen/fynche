/*
 * Fynche - a Framework for Multiagent Computational Creativity
 * Copyright 2011 Josh Hansen
 * 
 * This file is part of the Fynche <https://github.com/joshhansen/fynche>.
 * 
 * Fynche is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Fynche is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with Fynche.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * If you have inquiries regarding any further use of Fynche, please
 * contact Josh Hansen <http://joshhansen.net/>
 */
package fynche.agents;

import fynche.affinities.NullAffinityCombo;
import fynche.affinities.RandomAffinityCombo;
import fynche.artefacts.generators.RandomNamedColorGenerator;
import fynche.artefacts.genplans.FixedGenerationPlanner;
import fynche.artefacts.genplans.NullGenerationPlanner;
import fynche.artefacts.genplans.RandomGenerationPlanner;
import fynche.artefacts.initers.NullArtefactCombo;
import fynche.artefacts.pubdec.ExuberantPublicationDecider;
import fynche.artefacts.pubdec.SycophanticPublicationDecider;
import fynche.interfaces.AffinityInitializer;
import fynche.interfaces.AffinityUpdater;
import fynche.interfaces.ArtefactGenerator;
import fynche.interfaces.ArtefactInitializer;
import fynche.interfaces.Factory;
import fynche.interfaces.GenerationPlanner;
import fynche.interfaces.PreferenceInitializer;
import fynche.interfaces.PreferenceUpdater;
import fynche.interfaces.PublicationDecider;
import fynche.interfaces.RatingGenerator;
import fynche.interfaces.RatingInitializer;
import fynche.prefs.NullPreferenceCombo;
import fynche.prefs.RandomPreferenceInitializer;
import fynche.ratings.NullRatingCombo;
import fynche.ratings.RandomRatingGenerator;
import fynche.sys.MultiAgentSystem;
import fynche.util.Rand;

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
