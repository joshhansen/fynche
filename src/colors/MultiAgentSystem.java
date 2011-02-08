package colors;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import colors.agents.ModularAgentFactory;
import colors.artefacts.RandomAgentArtefactInitializer;
import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.ArtefactInitializer;
import colors.interfaces.Factory;
import colors.interfaces.PreferenceUpdater;
import colors.prefs.IndependentKDEPreferenceUpdater;

public class MultiAgentSystem implements Serializable {
	private static final long serialVersionUID = 1L;
	
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
		final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		log.setLevel(Level.FINE);
		log.info("Going to run for " + iterations + " iterations");
		for(Agent agent : agents) {
			log.info("Setting up agent " + agent);
			agent.setUp();
		}
		for(int i = 0; i < iterations; i++) {
			for(Agent agent : agents) {
				agent.roundStart();
			}
			for(Agent agent : agents) {
				agent.create();
			}
			for(Agent agent : agents) {
				agent.rate();
			}
			for(Agent agent : agents) {
				agent.roundFinish();
			}
			round++;
		}
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
	
	private static <T> Factory<T> staticFactory(final T object) {
		return new Factory<T>(){
			@Override
			public T instantiate() {
				return object;
			}
		};
	}
	
	public static void main(String[] args) {
		final ColorDB db = new ColorDB("/home/jjfresh/Courses/cs673/colors2.db");
		
		final MultiAgentSystem sys = new MultiAgentSystem();
		final int agentCount = 10;
		
		final ModularAgentFactory agentFact = new ModularAgentFactory(sys);
		agentFact.setPreferenceUpdaterFactory(staticFactory( (PreferenceUpdater) new IndependentKDEPreferenceUpdater()));
		agentFact.setArtefactInitializerFactory(staticFactory( (ArtefactInitializer) new RandomAgentArtefactInitializer(db, 100)));
		for(int i = 0; i < agentCount; i++) {
			sys.addAgent(agentFact.instantiate());
		}
		
		sys.run(10);
		sys.dumpResult();
	}
}
