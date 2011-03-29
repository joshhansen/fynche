package colors;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import colors.affinities.AverageRatingAffinityUpdater;
import colors.affinities.IndifferentAffinityCombo;
import colors.affinities.ModerateEntropyAffinityUpdater;
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
	
	private static class NoSuchPropException extends Exception {
		public NoSuchPropException(String message) {
			super(message);
		}
	}
	
	private final Properties props = new Properties();
	private void setProp(final String propName, final Object value) {
		props.put(propName, value);
	}
	
	private String getPropS(final String propName) throws NoSuchPropException {
		final Object val = props.get(propName);
		if(val == null) throw new NoSuchPropException("Property '" + propName + "' not found.");
		if(!(val instanceof String)) throw new NoSuchPropException("Property '" + propName + "' is not a string.");
		return (String) val;
	}
	
	private int getPropI(final String propName) throws NoSuchPropException {
		final Object val = props.get(propName);
		if(val == null) throw new NoSuchPropException("Property '" + propName + "' not found.");
		if(!(val instanceof Integer)) throw new NoSuchPropException("Property '" + propName + "' is not an integer.");
		return ((Integer)val).intValue();
	}
	
	private boolean getPropB(final String propName) throws NoSuchPropException {
		final Object val = props.get(propName);
		if(val == null) throw new NoSuchPropException("Property '" + propName + "' not found.");
		if(!(val instanceof Boolean)) throw new NoSuchPropException("Property '" + propName + "' is not a boolean.");
		return ((Boolean) val).booleanValue();
	}
	
	private double getPropD(final String propName) throws NoSuchPropException {
		final Object val = props.get(propName);
		if(val == null) throw new NoSuchPropException("Property '" + propName + "' not found.");
		if(!(val instanceof Double)) throw new NoSuchPropException("Property '" + propName + "' is not a double.");
		return ((Double)val).doubleValue();
	}
//	private final Map<String,String> propsS = new HashMap<String,String>();
//	private final Map<String,Double> propsD = new HashMap<String,Double>();
//	private final Map<String,Boolean> propsB = new HashMap<String,Boolean>();
//	private final Map<String,Integer> propsI = new HashMap<String,Integer>();

//	private double copycatWeight = 0.4;
//	private double exuberantWeight = 0.1;
	private double randomRatingWeight = 0.2;
	private int iterations = 100;
	private int topAgentsToPickFrom = 100;
	private int maxInitialArtefactsPerAgent = 40;
	private int suckUpToThisManyAgents = 5;
	private boolean orderArtefactsRandomly = true;
	
	private final ColorDB db;
	private final MultiAgentSystem sys;
	private final Counter<AgentType> agentTypeCounts = new Counter<AgentType>();
//	private final Properties props = new Properties();
	
	public ColorsMain() {
		final String baseDir = System.getenv("HOME") + "/Courses/cs673";
		final String outputDir = baseDir + "/cs673svn/output";
		db = new ColorDB(baseDir + "/colors2.db");
		sys = MultiAgentSystem.instance();
		
		this.outputDir = outputDir;
	}
	
	public ColorsMain(double randomRatingWeight, int iterations, int topAgentsToPickFrom,
			int maxInitialArtefactsPerAgent, int suckUpToThisManyAgents,
			boolean orderArtefactsRandomly) {
		this();
//		this.copycatWeight = copycatWeight;
//		this.exuberantWeight = exuberantWeight;
		this.randomRatingWeight = randomRatingWeight;
		this.iterations = iterations;
		this.topAgentsToPickFrom = topAgentsToPickFrom;
		this.maxInitialArtefactsPerAgent = maxInitialArtefactsPerAgent;
		this.suckUpToThisManyAgents = suckUpToThisManyAgents;
		this.orderArtefactsRandomly = orderArtefactsRandomly;
	}
	
	public void setAgentCount(final AgentType type, final double count) {
		agentTypeCounts.setCount(type, count);
	}
	
	private ModularAgentFactory standardAgentFact() {
		final ModularAgentFactory maf = new ModularAgentFactory(sys);
		maf.setAffinityInitializerFactory(RandomAffinityCombo.factory);
//		maf(Util.staticFactory(   (PreferenceUpdater)   new IndependentKDEPreferenceUpdater()));
		maf.setPreferenceUpdaterFactory(Util.staticFactory( (PreferenceUpdater) new DependentWordsPreferenceUpdater(4)));
		maf.setArtefactInitializerFactory(Util.staticFactory( (ArtefactInitializer) new RandomAgentArtefactInitializer(db, topAgentsToPickFrom, maxInitialArtefactsPerAgent, orderArtefactsRandomly)));
		maf.setGenerationPlannerFactory(Util.staticFactory(   (GenerationPlanner)   new RandomGenerationPlanner(10)));
		
		maf.setRatingGeneratorFactory(standardRatGenFact());
		maf.setPublicationDeciderFactory(standardPubDecFact());
		maf.setArtefactGeneratorFactory(standardArtGenFact());
		maf.setAffinityUpdaterFactory(standardAffinUpFact());
		
		return maf;
	}
	
	private Factory<? extends RatingGenerator> standardRatGenFact() {
		if(randomRatingWeight > 0.0) {
			final GroupedRatingGenerator grg = new GroupedRatingGenerator();
			grg.addGenerator(new EgocentricRatingGenerator(), 1.0-randomRatingWeight);
			grg.addGenerator(new RandomRatingGenerator(), randomRatingWeight);
			return Util.staticFactory( (RatingGenerator) grg);
		} else {
			return EgocentricRatingGenerator.factory;
		}
	}
	
	private Factory<? extends PublicationDecider> standardPubDecFact() {
		try {
			final double exuberantWeight = getPropD(Props.PUBDEC_EXUBERANT_WEIGHT);
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
			final double copycatWeight = getPropD(Props.ARTGEN_COPYCAT_WEIGHT);
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
		
		try {
			final double offset = getPropD(Props.MOD_ENT_OFFSET);
			final double stepSize = getPropD(Props.STEP_SIZE);
			return Util.staticFactory((AffinityUpdater) new ModerateEntropyAffinityUpdater(offset, stepSize));
		} catch(NoSuchPropException e) {
			e.printStackTrace();
			return null;
		}
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
//		if(standardAgents > 0) {
//			name.append("_stdAgents");
//			name.append(standardAgents);
//		}
//		if(randomAgents > 0) {
//			name.append("_random");
//			name.append(randomAgents);
//		}
//		if(eccentricRaters > 0) {
//			name.append("_eccentric");
//			name.append(eccentricRaters);
//		}
		name.append("_top");
		name.append(topAgentsToPickFrom);
		name.append("_maxArtInit");
		name.append(maxInitialArtefactsPerAgent);
		name.append("_cliquish");
		name.append(suckUpToThisManyAgents);
//		if(copycatWeight > 0.0) {
//			name.append("_copycat");
//			name.append(copycatWeight);
//		}
//		if(exuberantWeight > 0.0) {
//			name.append("_exuberant");
//			name.append(exuberantWeight);
//		}
		if(randomRatingWeight > 0.0) {
			name.append("_randRating");
			name.append(randomRatingWeight);
		}
		
		
		if(orderArtefactsRandomly)
			name.append("_artRandOrder");
//		name.append("_multiStratAffinUpFact");
//		name.append("_2");
		
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
		
		name.append("_moderateEntropy");
		
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
		sys.addRoundFinishedListener(new AffinitiesRecorder(outputDir + "/affinities/" + runID + ".txt"));
		sys.addRoundFinishedListener(new PublicationMatrixRecorder(outputDir + "/pubmatrix/" + runID + ".txt"));
		sys.addRunFinishedListener(new ResultDumper(outputDir+"/results/" + runID + ".txt"));
		
		sys.run(iterations);
		System.out.println("\nGenerated " + runIdentifier());
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
	
	
	
//	public void setCopycatWeight(double copycatWeight) {
//		this.copycatWeight = copycatWeight;
//	}

//	public void setExuberantWeight(double exuberantWeight) {
//		this.exuberantWeight = exuberantWeight;
//	}

	public void setRandomRatingWeight(double randomRatingWeight) {
		this.randomRatingWeight = randomRatingWeight;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public void setTopAgentsToPickFrom(int topAgentsToPickFrom) {
		this.topAgentsToPickFrom = topAgentsToPickFrom;
	}

	public void setMaxInitialArtefactsPerAgent(int maxInitialArtefactsPerAgent) {
		this.maxInitialArtefactsPerAgent = maxInitialArtefactsPerAgent;
	}

	public void setSuckUpToThisManyAgents(int suckUpToThisManyAgents) {
		this.suckUpToThisManyAgents = suckUpToThisManyAgents;
	}

	public void setOrderArtefactsRandomly(boolean orderArtefactsRandomly) {
		this.orderArtefactsRandomly = orderArtefactsRandomly;
	}
	
	//	double copycatWeight, double exuberantWeight,
//	double randomRatingWeight, int iterations, int topAgentsToPickFrom,
//	int maxInitialArtefactsPerAgent, int suckUpToThisManyAgents,
//	boolean orderArtefactsRandomly)
	public static void main(String[] args) {
//		final ColorsMain main = new ColorsMain(0, 0, 0, 100, 100, 1000, 5, true);
		final ColorsMain main = new ColorsMain();
		
		main.setProp(Props.ARTGEN_COPYCAT_WEIGHT, 0.1);
//		main.setProp(Props.PUBDEC_EXUBERANT_WEIGHT, 0.0);
		main.setRandomRatingWeight(0);
		main.setIterations(100);
		main.setTopAgentsToPickFrom(10);
		main.setMaxInitialArtefactsPerAgent(5000);
		main.setSuckUpToThisManyAgents(5);
		main.setOrderArtefactsRandomly(true);
		
		main.setProp(Props.STEP_SIZE, 0.01);
		main.setProp(Props.MOD_ENT_OFFSET, 3.0);
		
		main.setAgentCount(AgentType.STANDARD, 10);
		main.addAgents();
		main.run();
		
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
