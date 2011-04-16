package colors;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import colors.BetterProperties.NoSuchPropException;
import colors.affinities.AverageRatingAffinityUpdater;
import colors.affinities.IndifferentAffinityCombo;
import colors.affinities.ModerateEntropyAffinityUpdater;
import colors.affinities.ModerateNormalizedEntropyAffinityUpdater;
import colors.affinities.MultiStrategyAffinityUpdater;
import colors.affinities.NullAffinityCombo;
import colors.affinities.ObsessiveAffinityCombo;
import colors.affinities.RandomAffinityCombo;
import colors.agents.ModularAgentFactory;
import colors.artefacts.NamedColor;
import colors.artefacts.generators.CopycatArtefactGenerator;
import colors.artefacts.generators.GroupedArtefactGenerator;
import colors.artefacts.generators.SamplingArtefactGenerator;
import colors.artefacts.genplans.RandomGenerationPlanner;
import colors.artefacts.initers.RandomAgentArtefactInitializer;
import colors.artefacts.pubdec.CliqueishPublicationDecider;
import colors.artefacts.pubdec.ExuberantPublicationDecider;
import colors.artefacts.pubdec.GroupedPublicationDecider;
import colors.interfaces.AffinityUpdater;
import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.ArtefactGenerator;
import colors.interfaces.ArtefactInitializer;
import colors.interfaces.Factory;
import colors.interfaces.GenerationPlanner;
import colors.interfaces.PreferenceUpdater;
import colors.interfaces.PublicationDecider;
import colors.interfaces.RatingGenerator;
import colors.prefs.DependentWordsPreferenceUpdater;
import colors.ratings.EccentricRatingGenerator;
import colors.ratings.EgocentricRatingGenerator;
import colors.ratings.GroupedRatingGenerator;
import colors.ratings.RandomRatingGenerator;
import colors.sys.AffinitiesGraphRecorder;
import colors.sys.AffinitiesRecorder;
import colors.sys.Gexf4JAffinityRecorder;
import colors.sys.MultiAgentSystem;
import colors.sys.PublicationMatrixRecorder;
import colors.sys.ResultDumper;
import colors.ui.ColorViewer;
import colors.util.Counter;
import colors.util.GroupedFactory;
import colors.util.Util;
import colors.util.Util.SmartStaticFactory;
import javax.swing.SwingUtilities;

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
		try {
			final double randomRatingWeight = props.getPropD(Props.RANDOM_RATING_WEIGHT);
			assert(randomRatingWeight > 0.0);
			final GroupedRatingGenerator grg = new GroupedRatingGenerator();
			grg.addGenerator(new EgocentricRatingGenerator(), 1.0-randomRatingWeight);
			grg.addGenerator(new RandomRatingGenerator(), randomRatingWeight);
			return Util.staticFactory( (RatingGenerator) grg);
		} catch (NoSuchPropException e) {
			return EgocentricRatingGenerator.factory;
		}
	}
	
	private Factory<? extends PublicationDecider> standardPubDecFact() {
		final int suckUpToThisManyAgents = props.getPropI(Props.SUCK_UP_TO_THIS_MANY_AGENTS);
		try {
			final double exuberantWeight = props.getPropD(Props.PUBDEC_EXUBERANT_WEIGHT);
			assert(exuberantWeight > 0.0);
			final GroupedPublicationDecider gpd = new GroupedPublicationDecider();
			gpd.addDecider(new CliqueishPublicationDecider(suckUpToThisManyAgents), 1.0-exuberantWeight);
			gpd.addDecider(new ExuberantPublicationDecider(), exuberantWeight);
			return Util.staticFactory( (PublicationDecider) gpd);
		} catch(NoSuchPropException e) {
			return Util.staticFactory(  (PublicationDecider)  new CliqueishPublicationDecider(suckUpToThisManyAgents));
		}
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
		
		final double offset = props.getPropD(Props.MOD_ENT_OFFSET);
		final double m = props.getPropD(Props.MOD_ENT_M);
		final double stepSize = props.getPropD(Props.STEP_SIZE);
		return Util.staticFactory((AffinityUpdater) new ModerateNormalizedEntropyAffinityUpdater(offset, m, stepSize));
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
		
		name.append("_modNormEnt_renormed_unifAffinInit");
		
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
		
		main.setIterations(1000);
		main.setProp(Props.ARTGEN_COPYCAT_WEIGHT, 0.1);
//		main.setProp(Props.PUBDEC_EXUBERANT_WEIGHT, 0.1);
//		main.setProp(RANDOM_RATING_WEIGHT, 0.2);
		
		main.setProp(Props.TOP_AGENTS_TO_PICK_FROM, 10);
		main.setProp(Props.MAX_INITIAL_ARTEFACTS_PER_AGENT, 5000);
		main.setProp(Props.SUCK_UP_TO_THIS_MANY_AGENTS, 5);
		main.setProp(Props.ORDER_ARTEFACTS_RANDOMLY, true);
		main.setProp(Props.STEP_SIZE, 0.001);
		main.setProp(Props.MOD_ENT_OFFSET, 0.5);
		main.setProp(Props.MOD_ENT_M, 0.0);
		
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
