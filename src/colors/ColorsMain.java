package colors;

import java.util.Map.Entry;

import colors.affinities.AverageRatingAffinityUpdater;
import colors.affinities.IndifferentAffinityCombo;
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
	
	private double copycatWeight = 0.4;
	private double exuberantWeight = 0.1;
	private double randomRatingWeight = 0.2;
	private int iterations = 100;
	private int topAgentsToPickFrom = 100;
	private int maxInitialArtefactsPerAgent = 40;
	private int suckUpToThisManyAgents = 5;
	private boolean orderArtefactsRandomly = true;
	
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
	
	public ColorsMain(double copycatWeight, double exuberantWeight,
			double randomRatingWeight, int iterations, int topAgentsToPickFrom,
			int maxInitialArtefactsPerAgent, int suckUpToThisManyAgents,
			boolean orderArtefactsRandomly) {
		this();
		this.copycatWeight = copycatWeight;
		this.exuberantWeight = exuberantWeight;
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
		if(exuberantWeight > 0.0) {
			final GroupedPublicationDecider gpd = new GroupedPublicationDecider();
			gpd.addDecider(new CliqueishPublicationDecider(suckUpToThisManyAgents), 1.0-exuberantWeight);
			gpd.addDecider(new ExuberantPublicationDecider(), exuberantWeight);
			return Util.staticFactory( (PublicationDecider) gpd);
		} else {
			return Util.staticFactory(  (PublicationDecider)  new CliqueishPublicationDecider(suckUpToThisManyAgents));
		}
	}
	
	private Factory<? extends ArtefactGenerator> standardArtGenFact() {
		if(copycatWeight > 0.0) {
			final GroupedArtefactGenerator gag = new GroupedArtefactGenerator();
			gag.addGenerator(new SamplingArtefactGenerator(), 1.0-copycatWeight);
			gag.addGenerator(new CopycatArtefactGenerator(), copycatWeight);
			return Util.staticFactory( (ArtefactGenerator) gag);
		} else {
			return Util.staticFactory( (ArtefactGenerator) new SamplingArtefactGenerator());
		}
	}
	
	private Factory<? extends AffinityUpdater> standardAffinUpFact() {
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
		return new GroupedFactory<AffinityUpdater>(affinUpFacts);
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
		if(copycatWeight > 0.0) {
			name.append("_copycat");
			name.append(copycatWeight);
		}
		if(exuberantWeight > 0.0) {
			name.append("_exuberant");
			name.append(exuberantWeight);
		}
		if(randomRatingWeight > 0.0) {
			name.append("_randRating");
			name.append(randomRatingWeight);
		}
		
		
		if(orderArtefactsRandomly)
			name.append("_artRandOrder");
//		name.append("_multiStratAffinUpFact");
//		name.append("_2");
		
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
		sys.addRoundFinishedListener(new AffinitiesRecorder(outputDir + "/affinities/" + runID + ".txt"));
		sys.addRoundFinishedListener(new PublicationMatrixRecorder(outputDir + "/pubmatrix/" + runID + ".txt"));
		sys.addRunFinishedListener(new ResultDumper(outputDir+"/results/" + runID + ".txt"));
		
		sys.run(iterations);
		System.out.println("\nGenerated " + runIdentifier());
	}
	
	
	
	public void setCopycatWeight(double copycatWeight) {
		this.copycatWeight = copycatWeight;
	}

	public void setExuberantWeight(double exuberantWeight) {
		this.exuberantWeight = exuberantWeight;
	}

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
		
		main.setCopycatWeight(0);
		main.setExuberantWeight(0);
		main.setRandomRatingWeight(0);
		main.setIterations(100);
		main.setTopAgentsToPickFrom(100);
		main.setMaxInitialArtefactsPerAgent(5000);
		main.setSuckUpToThisManyAgents(5);
		main.setOrderArtefactsRandomly(true);
		
		main.setAgentCount(AgentType.STANDARD, 10);
		main.addAgents();
		main.run();
		
		final ColorViewer viewer = new ColorViewer();
		
		for(Agent agent : main.sys.agents()) {
                        for(int i = main.iterations - 30; i < main.iterations; i++) {
                            for(Artefact a : agent.artefacts(i)) {
                                if(a instanceof NamedColor)
                                    viewer.addColor(agent.toString(), (NamedColor)a);
                            }
                        }
//			for(Artefact a : agent.artefacts(main.iterations-1)) {
//				if(a instanceof NamedColor)
//					viewer.addColor(agent.toString(), (NamedColor)a);
//			}
		}

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        viewer.setVisible(true);
                    }
                });
	}
}
