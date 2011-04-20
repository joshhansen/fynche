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
package fynche;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;

import javax.swing.SwingUtilities;

import fynche.BetterProperties.NoSuchPropException;
import fynche.affinities.AverageRatingAffinityUpdater;
import fynche.affinities.IndifferentAffinityCombo;
import fynche.affinities.ModeratePreNormalizedEntropyAffinityUpdater;
import fynche.affinities.MultiStrategyAffinityUpdater;
import fynche.affinities.NullAffinityCombo;
import fynche.affinities.ObsessiveAffinityCombo;
import fynche.affinities.RandomAffinityCombo;
import fynche.agents.ModularAgentFactory;
import fynche.artefacts.NamedColor;
import fynche.artefacts.generators.CopycatArtefactGenerator;
import fynche.artefacts.generators.GroupedArtefactGenerator;
import fynche.artefacts.generators.SamplingArtefactGenerator;
import fynche.artefacts.genplans.RandomGenerationPlanner;
import fynche.artefacts.initers.RandomAgentArtefactInitializer;
import fynche.artefacts.pubdec.CliqueishPublicationDecider;
import fynche.artefacts.pubdec.ExuberantPublicationDecider;
import fynche.artefacts.pubdec.GroupedPublicationDecider;
import fynche.interfaces.AffinityUpdater;
import fynche.interfaces.Agent;
import fynche.interfaces.Artefact;
import fynche.interfaces.ArtefactGenerator;
import fynche.interfaces.ArtefactInitializer;
import fynche.interfaces.Factory;
import fynche.interfaces.GenerationPlanner;
import fynche.interfaces.PreferenceUpdater;
import fynche.interfaces.PublicationDecider;
import fynche.interfaces.RatingGenerator;
import fynche.prefs.DependentWordsPreferenceUpdater;
import fynche.ratings.EccentricRatingGenerator;
import fynche.ratings.EgocentricRatingGenerator;
import fynche.ratings.GroupedRatingGenerator;
import fynche.ratings.NullRatingCombo;
import fynche.ratings.RandomRatingGenerator;
import fynche.sys.AffinitiesRecorder;
import fynche.sys.MultiAgentSystem;
import fynche.sys.ResultDumper;
import fynche.ui.ColorViewer;
import fynche.util.Counter;
import fynche.util.Util;
import fynche.util.Util.SmartStaticFactory;

/*
 * Things to try:
 *  Filter preferences/ratings through Wundt curve.
 *  Make agents aware of only certain artefacts, e.g. based on affinity/prominence as in Bown&Wiggins 2005.
 */
public class ColorsMain {
	public static enum AgentType {
		STANDARD, RANDOM, ECCENTRIC, MULTI_STRATEGY
	}
	
	private final String outputDir;
	private final BetterProperties props = new BetterProperties();
	private int iterations = 100;
	private final ColorDB db;
	private final MultiAgentSystem sys;
	private final Counter<AgentType> agentTypeCounts = new Counter<AgentType>();
	
	public ColorsMain() {
		final String baseDir = System.getenv("HOME") + "/Courses/cs673";
		final String outputDir = baseDir + "/cs673svn/output";
		db = new ColorDB(baseDir + "/colors2.db");
		sys = MultiAgentSystem.instance();
		
		this.outputDir = outputDir;
	}
	
	public ColorsMain(int iterations) {
		this();
		this.iterations = iterations;
	}
	
	public void setAgentCount(final AgentType type, final double count) {
		agentTypeCounts.setCount(type, count);
	}
	
	private ModularAgentFactory standardAgentFact() {
		final ModularAgentFactory maf = new ModularAgentFactory(sys);
		
//		maf.setAffinityInitializerFactory(RandomAffinityCombo.factory);
		maf.setAffinityInitializerFactory(IndifferentAffinityCombo.factory);
//		maf(Util.staticFactory(   (PreferenceUpdater)   new IndependentKDEPreferenceUpdater()));
		maf.setPreferenceUpdaterFactory(Util.staticFactory( (PreferenceUpdater) new DependentWordsPreferenceUpdater(4)));
		maf.setArtefactInitializerFactory(Util.staticFactory( (ArtefactInitializer) new RandomAgentArtefactInitializer(db, props.getPropI(Props.TOP_AGENTS_TO_PICK_FROM), props.getPropI(Props.MAX_INITIAL_ARTEFACTS_PER_AGENT), props.getPropB(Props.ORDER_ARTEFACTS_RANDOMLY))));
		maf.setGenerationPlannerFactory(Util.staticFactory(   (GenerationPlanner)   new RandomGenerationPlanner(10)));
		maf.setRatingGeneratorFactory(standardRatGenFact());
		maf.setPublicationDeciderFactory(standardPubDecFact());
		maf.setArtefactGeneratorFactory(standardArtGenFact());
		maf.setAffinityUpdaterFactory(standardAffinUpFact());
		
		return maf;
	}
	
	private Factory<? extends RatingGenerator> standardRatGenFact() {
		return NullRatingCombo.factory;
//		try {
//			final double randomRatingWeight = props.getPropD(Props.RANDOM_RATING_WEIGHT);
//			assert(randomRatingWeight > 0.0);
//			final GroupedRatingGenerator grg = new GroupedRatingGenerator();
//			grg.addGenerator(new EgocentricRatingGenerator(), 1.0-randomRatingWeight);
//			grg.addGenerator(new RandomRatingGenerator(), randomRatingWeight);
//			return Util.staticFactory( (RatingGenerator) grg);
//		} catch (NoSuchPropException e) {
//			return EgocentricRatingGenerator.factory;
//		}
	}
	
	private Factory<? extends PublicationDecider> standardPubDecFact() {
		return ExuberantPublicationDecider.factory;
//		final int suckUpToThisManyAgents = props.getPropI(Props.SUCK_UP_TO_THIS_MANY_AGENTS);
//		try {
//			final double exuberantWeight = props.getPropD(Props.PUBDEC_EXUBERANT_WEIGHT);
//			assert(exuberantWeight > 0.0);
//			final GroupedPublicationDecider gpd = new GroupedPublicationDecider();
//			gpd.addDecider(new CliqueishPublicationDecider(suckUpToThisManyAgents), 1.0-exuberantWeight);
//			gpd.addDecider(new ExuberantPublicationDecider(), exuberantWeight);
//			return Util.staticFactory( (PublicationDecider) gpd);
//		} catch(NoSuchPropException e) {
//			return Util.staticFactory(  (PublicationDecider)  new CliqueishPublicationDecider(suckUpToThisManyAgents));
//		}
	}
	
	private Factory<? extends ArtefactGenerator> standardArtGenFact() {
		try {
			final double copycatWeight = props.getPropD(Props.ARTGEN_COPYCAT_WEIGHT);
			assert(copycatWeight > 0.0);
			final GroupedArtefactGenerator gag = new GroupedArtefactGenerator();
			gag.addGenerator(new SamplingArtefactGenerator(), 1.0-copycatWeight);
			gag.addGenerator(new CopycatArtefactGenerator(), copycatWeight);
			return Util.staticFactory( (ArtefactGenerator) gag);
		} catch(NoSuchPropException e) {
			return Util.staticFactory( (ArtefactGenerator) new SamplingArtefactGenerator());
		}
	}
	
	private Factory<? extends AffinityUpdater> standardAffinUpFact() {
//		final Counter<Factory<? extends AffinityUpdater>> affinUpFacts = new Counter<Factory<? extends AffinityUpdater>>();
//		affinUpFacts.setCount(NullAffinityCombo.factory, 1);
//		final Factory<ObsessiveAffinityCombo> obsessiveAffinFact = new SmartStaticFactory<ObsessiveAffinityCombo>() {
//			@Override
//			protected ObsessiveAffinityCombo instantiate_() {
//				return new ObsessiveAffinityCombo(0.1);
//			}
//		};
//		affinUpFacts.setCount(obsessiveAffinFact, 10);
//		affinUpFacts.setCount(IndifferentAffinityCombo.factory, 10);
//		affinUpFacts.setCount(RandomAffinityCombo.factory, 10);
//		affinUpFacts.setCount(AverageRatingAffinityUpdater.factory, 10);
//		affinUpFacts.normalize();
//		return new GroupedFactory<AffinityUpdater>(affinUpFacts);
		
		final double offset = props.getPropD(Props.MOD_ENT_HORIZ_OFFSET);
		final double steepness = props.getPropD(Props.MOD_ENT_STEEPNESS);
		final double stepSize = props.getPropD(Props.OPT_STEP_SIZE);
		return Util.staticFactory((AffinityUpdater) new ModeratePreNormalizedEntropyAffinityUpdater(steepness, offset, stepSize));
	}
	
	private ModularAgentFactory randomAgentFact() {
		return ModularAgentFactory.randomAgentFactory(sys);
	}
	
	private ModularAgentFactory eccentricAgentFact() {
		final ModularAgentFactory maf = standardAgentFact();
		
		maf.setRatingGeneratorFactory(EccentricRatingGenerator.factory);
		
		return maf;
	}
	
	private ModularAgentFactory multiStrategyAgentFact() {
		final ModularAgentFactory maf = standardAgentFact();
		
		maf.setAffinityUpdaterFactory(multiStratAffinUpFact());
		
		return maf;
	}
	
	private Factory<? extends AffinityUpdater> multiStratAffinUpFact() {
		final Counter<Factory<? extends AffinityUpdater>> affinUpFacts = new Counter<Factory<? extends AffinityUpdater>>();
		affinUpFacts.setCount(NullAffinityCombo.factory, 1);
		final Factory<ObsessiveAffinityCombo> obsessiveAffinFact = new SmartStaticFactory<ObsessiveAffinityCombo>() {
			@Override
			protected ObsessiveAffinityCombo instantiate_() {
				return new ObsessiveAffinityCombo(0.1);
			}
		};
		affinUpFacts.setCount(obsessiveAffinFact, 10);
		affinUpFacts.setCount(IndifferentAffinityCombo.factory, 10);
		affinUpFacts.setCount(RandomAffinityCombo.factory, 10);
		affinUpFacts.setCount(AverageRatingAffinityUpdater.factory, 10);
		affinUpFacts.normalize();
		
		return MultiStrategyAffinityUpdater.factory(affinUpFacts, 0.01);
	}
	
	private ModularAgentFactory agentFact(final AgentType type) {
		switch(type) {
			case STANDARD:
				return standardAgentFact();
			case RANDOM:
				return randomAgentFact();
			case ECCENTRIC:
				return eccentricAgentFact();
			case MULTI_STRATEGY:
				return multiStrategyAgentFact();
			default:
				throw new IllegalArgumentException("Unsupported agent type " + type.name());
		}
	}
	
	public String runIdentifier() {
		final StringBuilder name = new StringBuilder();
		name.append("iterations");
		name.append(iterations);
		
		for(Entry<AgentType,Double> entry : agentTypeCounts.entrySet()) {
			final int count = entry.getValue().intValue();
			if(count > 0) {
				name.append('_');
				name.append(entry.getKey().name());
				name.append(count);
			}
		}
		
		for(final Entry<Object,Object> entry : props.entrySet()) {
			final String propName = entry.getKey().toString();
			name.append('_');
			
			final Object val = entry.getValue();
			if(val instanceof String || val instanceof Double || val instanceof Integer) {
				name.append(propName);
				name.append(':');
				name.append(val.toString());
			} else if(val instanceof Boolean) {
				if(((Boolean)val).booleanValue())
					name.append(propName);
			}
		}
		
		name.append("_modPreNormEnt_unifAffinInit");
		
		return name.toString();
	}
	
	private void addAgents() {
		for(Entry<AgentType,Double> entry : agentTypeCounts.entrySet()) {
			final AgentType type = entry.getKey();
			final ModularAgentFactory fact = agentFact(type);
			for(int i = 0; i < entry.getValue().intValue(); i++) {
				sys.addAgent(fact.instantiate(type.name()+i));
			}
		}
	}
	
	public void run() {
		Util.initLogging();
		final String runID = runIdentifier();
		saveProps(outputDir + "/props/" + runID + ".properties");
		
		configureListeners(runID);
		
		sys.run(iterations);
		System.out.println("\nGenerated " + runIdentifier());
	}
	
	private void configureListeners(final String runID) {
		sys.addListener(new AffinitiesRecorder(outputDir + "/affinities/" + runID + ".txt"));
//		sys.addListener(new PublicationMatrixRecorder(outputDir + "/pubmatrix/" + runID + ".txt"));
		sys.addListener(new ResultDumper(outputDir+"/results/" + runID + ".txt"));
		
//		final String affinitiesGraphFilename = outputDir + "/gexf/" + runID + ".gexf";
//		final AffinitiesGraphRecorder agr = new AffinitiesGraphRecorder(sys, affinitiesGraphFilename);
//		sys.addListener(agr);
//		final Gexf4JAffinityRecorder g4jar = new Gexf4JAffinityRecorder(affinitiesGraphFilename);
//		sys.addListener(g4jar);
	}
	
	private void saveProps(final String toFile) {
		try {
			final BufferedWriter w = new BufferedWriter(new FileWriter(toFile));
			for(Entry<Object,Object> entry : props.entrySet()) {
				w.append(entry.getKey().toString());
				w.append(':');
				w.append(entry.getValue().toString());
				w.newLine();
			}
			w.flush();
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}
	
	public void setProp(String propName, Object value) {
		props.setProp(propName, value);
	}

	public static void main(String[] args) {
		final ColorsMain main = new ColorsMain();
		
		main.setIterations(100);
		main.setProp(Props.ARTGEN_COPYCAT_WEIGHT, 0.1);
//		main.setProp(Props.PUBDEC_EXUBERANT_WEIGHT, 0.1);
//		main.setProp(RANDOM_RATING_WEIGHT, 0.2);
		
		main.setProp(Props.TOP_AGENTS_TO_PICK_FROM, 10);
		main.setProp(Props.MAX_INITIAL_ARTEFACTS_PER_AGENT, 500);//00);
		main.setProp(Props.SUCK_UP_TO_THIS_MANY_AGENTS, 5);
		main.setProp(Props.ORDER_ARTEFACTS_RANDOMLY, true);
		
		main.setProp(Props.OPT_STEP_SIZE, 0.001);
//		main.setProp(Props.MOD_ENT_OFFSET, 0.5);
//		main.setProp(Props.MOD_ENT_M, 0.0);
		
		main.setProp(Props.MOD_ENT_HORIZ_OFFSET, 0.5);
		main.setProp(Props.MOD_ENT_STEEPNESS, 1.0);
		
		main.setAgentCount(AgentType.STANDARD, 10);
		main.addAgents();
		main.run();
		
		showViewer(main);
	}

	private static void showViewer(final ColorsMain main) {
		final ColorViewer viewer = new ColorViewer();
		viewer.setTitle(main.runIdentifier());
		for (Agent agent : main.sys.agents()) {
			for (int i = main.iterations - 30; i < main.iterations; i++) {
				for (Artefact a : agent.artefacts(i)) {
					if (a instanceof NamedColor)
						viewer.addColor(agent.toString(), (NamedColor) a);
				}
			}
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				viewer.setVisible(true);
			}
		});
	}
}
