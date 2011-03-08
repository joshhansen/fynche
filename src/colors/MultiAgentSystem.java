/*
 * Things to try:
 *  Filter preferences/ratings through Wundt curve.
 *  Make agents aware of only certain artefacts, e.g. based on affinity/prominence as in Bown&Wiggins 2005.
 */
package colors;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import colors.affinities.AverageRatingAffinityUpdater;
import colors.affinities.IndifferentAffinityCombo;
import colors.affinities.MultiStrategyAffinityUpdater;
import colors.affinities.NullAffinityCombo;
import colors.affinities.ObsessiveAffinityCombo;
import colors.affinities.RandomAffinityCombo;
import colors.agents.ModularAgentFactory;
import colors.agents.RandomAgent;
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
import colors.util.Counter;
import colors.util.Util;
import colors.util.Util.SmartStaticFactory;

public class MultiAgentSystem implements Serializable {
	public static final Random rand = new Random();
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(MultiAgentSystem.class.getName());
	
	private static MultiAgentSystem system;
	public static MultiAgentSystem instance() {
		if(system==null)
			system = new MultiAgentSystem();
		return system;
	}
	
	
	private int round = 0;
	private Set<RoundFinishedListener> roundFinishedListeners = new HashSet<RoundFinishedListener>();
	private Set<RunFinishedListener> runFinishedListeners = new HashSet<RunFinishedListener>();
	private MultiAgentSystem() {
	}
	
	public int round() {
		return round;
	}
	
	final Set<Agent> agents = new HashSet<Agent>();
	public Set<Agent> agents() {
		return Collections.unmodifiableSet(agents);
	}
	
	public void addAgent(final Agent agent) {
		agents.add(agent);
	}
	
	public void addRoundFinishedListener(final RoundFinishedListener rfl) {
		this.roundFinishedListeners.add(rfl);
	}
	
	public void addRunFinishedListener(final RunFinishedListener rfl) {
		this.runFinishedListeners.add(rfl);
	}
	
	/**
	 * 
	 * @param iterations How many iterations to run for
	 */
	public void run(final int iterations) {
		logger.info("Going to run for " + iterations + " iterations");
		logger.info("Setup");
		for(Agent agent : agents) {
			agent.setUp();
		}
		for(int i = 0; i < iterations; i++) {
			if(i % 10 == 0) System.out.print(" " + i);
			else System.out.print('.');
			
			logger.info("-----Starting round " + i + "-----");
			for(Agent agent : agents) {
				agent.roundStart();
			}
			
			logger.info("Beginning artefact generation");
			for(Agent agent : agents) {
				agent.create();
			}
			logger.info("Beginning rating generation");
			for(Agent agent : agents) {
				agent.rate();
			}
			
			logger.info("-----Finishing round " + i + "-----");
			for(Agent agent : agents) {
				agent.roundFinish();
			}
			for(RoundFinishedListener rfl : roundFinishedListeners)
				rfl.roundFinished(this, round);
//			updateGraphAffinities();
			round++;
		}
		
		logger.info("Beginning takedown");
		for(Agent agent : agents) {
			agent.takeDown();
		}
		for(RunFinishedListener rfl : runFinishedListeners)
			rfl.runFinished(this);
	}
	
	public void step() {
		run(1);
	}
	
	public static class ResultDumper implements RunFinishedListener {
		private final String outputFilename;
		
		public ResultDumper(String outputFilename) {
			this.outputFilename = outputFilename;
		}
		
		@Override
		public void runFinished(MultiAgentSystem sys) {
			try {
				final BufferedWriter w = new BufferedWriter(new FileWriter(outputFilename));
				for(Agent agent : sys.agents) {
					w.append("Agent: " + agent);
					w.newLine();
					for(int i = 0; i < sys.round; i++) {
						final Set<Artefact> all = new HashSet<Artefact>();
						for(Agent other : sys.agents) {
							if(agent != other) all.addAll(agent.artefacts(other));
						}
						for(Artefact a : all) {
							w.append('\t');
							w.append(String.valueOf(i));
							w.append('\t');
							w.append(a.toString());
							w.newLine();
						}
					}
				}
				w.flush();
				w.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Util.initLogging();
		
		final String baseDir = System.getenv("HOME") + "/Courses/cs673";
		final String outputDir = baseDir + "/cs673svn/output";
		final ColorDB db = new ColorDB(baseDir + "/colors2.db");
		final MultiAgentSystem sys = MultiAgentSystem.instance();
		
		//--------Settings----------
		final int standardAgents = 20;
		final int randomAgents = 0;
		final int eccentricRaters = 0;
		final double copycatWeight = 0.4;
		final double exuberantWeight = 0.1;
		final double randomRatingWeight = 0.2;
		final int iterations = 100;
		final int topAgentsToPickFrom = 100;
		final int maxInitialArtefactsPerAgent = 40;
		final int suckUpToThisManyAgents = 5;
		final boolean orderArtefactsRandomly = true;
		
		//---------- Name of Experiment ----------
		final StringBuilder name = new StringBuilder();
		name.append("iterations");
		name.append(iterations);
		if(standardAgents > 0) {
			name.append("_stdAgents");
			name.append(standardAgents);
		}
		if(randomAgents > 0) {
			name.append("_random");
			name.append(randomAgents);
		}
		if(eccentricRaters > 0) {
			name.append("_eccentric");
			name.append(eccentricRaters);
		}
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
		name.append("_multiStratAffinUpFact");
		name.append("_2");
		
		//Begin constructing the agent factory
		final ModularAgentFactory agentFact = new ModularAgentFactory(sys);
		
		//---------Affinities--------
		//Initializer:
		agentFact.setAffinityInitializerFactory(RandomAffinityCombo.factory);
		
		//Updater:
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
//		final Factory<AffinityUpdater> affinUpFact = new GroupedFactory<AffinityUpdater>(affinUpFacts);
//		agentFact.setAffinityUpdaterFactory(affinUpFact);
		
		final Factory<? extends AffinityUpdater> multiStratAffinUp = MultiStrategyAffinityUpdater.factory(affinUpFacts, 0.01);
		agentFact.setAffinityUpdaterFactory(multiStratAffinUp);
		
//		agentFact.setPreferenceUpdaterFactory(Util.staticFactory(   (PreferenceUpdater)   new IndependentKDEPreferenceUpdater()));
		agentFact.setPreferenceUpdaterFactory(Util.staticFactory( (PreferenceUpdater) new DependentWordsPreferenceUpdater(4)));
		agentFact.setArtefactInitializerFactory(Util.staticFactory( (ArtefactInitializer) new RandomAgentArtefactInitializer(db, topAgentsToPickFrom, maxInitialArtefactsPerAgent, orderArtefactsRandomly)));
		agentFact.setGenerationPlannerFactory(Util.staticFactory(   (GenerationPlanner)   new RandomGenerationPlanner(10)));
		
		if(copycatWeight > 0.0) {
			final GroupedArtefactGenerator gag = new GroupedArtefactGenerator();
			gag.addGenerator(new SamplingArtefactGenerator(), 1.0-copycatWeight);
			gag.addGenerator(new CopycatArtefactGenerator(), copycatWeight);
			agentFact.setArtefactGeneratorFactory(Util.staticFactory( (ArtefactGenerator) gag));
		} else {
			agentFact.setArtefactGeneratorFactory(Util.staticFactory( (ArtefactGenerator) new SamplingArtefactGenerator()));
		}
		if(exuberantWeight > 0.0) {
			final GroupedPublicationDecider gpd = new GroupedPublicationDecider();
			gpd.addDecider(new CliqueishPublicationDecider(suckUpToThisManyAgents), 1.0-exuberantWeight);
			gpd.addDecider(new ExuberantPublicationDecider(), exuberantWeight);
			agentFact.setPublicationDeciderFactory(Util.staticFactory( (PublicationDecider) gpd));
		} else {
			agentFact.setPublicationDeciderFactory(Util.staticFactory(  (PublicationDecider)  new CliqueishPublicationDecider(suckUpToThisManyAgents)));
		}
		if(randomRatingWeight > 0.0) {
			final GroupedRatingGenerator grg = new GroupedRatingGenerator();
			grg.addGenerator(new EgocentricRatingGenerator(), 1.0-randomRatingWeight);
			grg.addGenerator(new RandomRatingGenerator(), randomRatingWeight);
			agentFact.setRatingGeneratorFactory(Util.staticFactory( (RatingGenerator) grg));
		} else {
			agentFact.setRatingGeneratorFactory(EgocentricRatingGenerator.factory);
		}
		
		for(int i = 0; i < standardAgents; i++) {
			sys.addAgent(agentFact.instantiate("agent"+i));
		}
		
		
		agentFact.setRatingGeneratorFactory(EccentricRatingGenerator.factory);
		for(int i = 0; i < eccentricRaters; i++) {
			sys.addAgent(agentFact.instantiate("eccentric"+i));
		}
		
		for(int i = 0; i < randomAgents; i++) {
			sys.addAgent(new RandomAgent(sys,"random"+i));
		}
		
		sys.addRoundFinishedListener(new AffinitiesRecorder(outputDir + "/affinities/" + name + ".txt"));
		sys.addRoundFinishedListener(new PublicationMatrixRecorder(outputDir + "/pubmatrix/" + name + ".txt"));
		sys.addRunFinishedListener(new ResultDumper(outputDir+"/results/" + name + ".txt"));
		sys.run(iterations);
		
		System.out.println("\nGenerated " + name);
	}
}
