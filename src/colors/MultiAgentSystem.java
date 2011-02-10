package colors;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import colors.affinities.AverageRatingAffinityUpdater;
import colors.agents.ModularAgentFactory;
import colors.agents.RandomAgent;
import colors.artefacts.NamedColor;
import colors.artefacts.generators.CopycatArtefactGenerator;
import colors.artefacts.generators.GroupedArtefactGenerator;
import colors.artefacts.generators.SamplingArtefactGenerator;
import colors.artefacts.genplans.RandomGenerationPlanner;
import colors.artefacts.initers.RandomAgentArtefactInitializer;
import colors.interfaces.AffinityUpdater;
import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.ArtefactGenerator;
import colors.interfaces.ArtefactInitializer;
import colors.interfaces.GenerationPlanner;
import colors.interfaces.PreferenceUpdater;
import colors.interfaces.RatingGenerator;
import colors.prefs.IndependentKDEPreferenceUpdater;
import colors.ratings.EgocentricRatingGenerator;
import colors.util.Util;

public class MultiAgentSystem implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(MultiAgentSystem.class.getName());
	
	private static MultiAgentSystem system;
	public static MultiAgentSystem instance() {
		if(system==null)
			system = new MultiAgentSystem();
		return system;
	}
	
	
	private int round = 0;
	private MultiAgentSystem() {
	}
	
	public int round() {
		return round;
	}
	
	private final Set<Agent> agents = new HashSet<Agent>();
	public Set<Agent> agents() {
		return Collections.unmodifiableSet(agents);
	}
	
	public void addAgent(final Agent agent) {
		agents.add(agent);
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
			round++;
		}
		
		logger.info("Beginning takedown");
		for(Agent agent : agents) {
			agent.takeDown();
		}
	}
	
	public void step() {
		run(1);
	}
	
	public void dumpResult() {
		for(Agent agent : agents) {
			System.out.println("Agent: " + agent);
			for(int i = 0; i < round; i++) {
				for(Artefact a : agent.publishedArtefacts(i)) {
					System.out.println("\t" + i + "\t" + a);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		Util.initLogging();
		final ColorDB db = new ColorDB("/home/jjfresh/Courses/cs673/colors2.db");
		
		final MultiAgentSystem sys = new MultiAgentSystem();
		final int agentCount = 4;
		final int iterations = 10;
		final int topAgentsToPickFrom = 100;
		final int maxInitialArtefactsPerAgent = 40;
		final boolean orderArtefactsRandomly = true;
		
		final ModularAgentFactory agentFact = new ModularAgentFactory(sys);
		agentFact.setPreferenceUpdaterFactory(Util.staticFactory( (PreferenceUpdater) new IndependentKDEPreferenceUpdater()));
		agentFact.setArtefactInitializerFactory(Util.staticFactory( (ArtefactInitializer) new RandomAgentArtefactInitializer(db, topAgentsToPickFrom, maxInitialArtefactsPerAgent, orderArtefactsRandomly)));
		agentFact.setGenerationPlannerFactory(Util.staticFactory( (GenerationPlanner) new RandomGenerationPlanner(10)));
		agentFact.setRatingGeneratorFactory(Util.staticFactory( (RatingGenerator) new EgocentricRatingGenerator()));
		agentFact.setAffinityUpdaterFactory(Util.staticFactory( (AffinityUpdater) new AverageRatingAffinityUpdater()));
		
		final GroupedArtefactGenerator gag = new GroupedArtefactGenerator();
		gag.addGenerator(new SamplingArtefactGenerator(), 0.9);
		gag.addGenerator(new CopycatArtefactGenerator(), 0.1);
		agentFact.setArtefactGeneratorFactory(Util.staticFactory( (ArtefactGenerator) gag));
		
		for(int i = 0; i < agentCount-1; i++) {
			sys.addAgent(agentFact.instantiate());
		}
		sys.addAgent(new RandomAgent(sys));
		
		sys.run(iterations);
		sys.dumpResult();
	}
}
