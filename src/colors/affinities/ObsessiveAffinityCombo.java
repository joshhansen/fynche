package colors.affinities;

import colors.interfaces.AffinityCombo;
import colors.interfaces.Agent;
import colors.interfaces.Factory;
import colors.util.Counter;
import colors.util.CounterMap;
import colors.util.Rand;
import colors.util.Util.SmartStaticFactory;

public class ObsessiveAffinityCombo implements AffinityCombo {
	public static final Factory<ObsessiveAffinityCombo> factory = new SmartStaticFactory<ObsessiveAffinityCombo>() {
		@Override
		protected ObsessiveAffinityCombo instantiate_() {
			return new ObsessiveAffinityCombo();
		}
	};
	
	private final double changeProb;
	private CounterMap<Agent,Agent> affins = new CounterMap<Agent,Agent>();
	public ObsessiveAffinityCombo() {
		this(0.0);
	}
	
	public ObsessiveAffinityCombo(final double changeProb) {
		this.changeProb = changeProb;
	}
	
	private Counter<Agent> chooseNewObsession(final Agent obsesser) {
		final Agent[] agents = obsesser.system().agents().toArray(new Agent[0]);
		Agent objectOfObsession = null;
		do {
			final int chosen = Rand.nextInt(agents.length);
			objectOfObsession = agents[chosen];
		} while(objectOfObsession==obsesser);
		
		final Counter<Agent> agentAffins = affins.getCounter(obsesser);
		agentAffins.setCount(objectOfObsession, 1.0);
		
		for(Agent other : obsesser.system().agents()) {
			if(other != obsesser && other != objectOfObsession)
				agentAffins.setCount(other, 1.0/(double)(agents.length-2));
		}
		agentAffins.normalize();
		return agentAffins;
	}
	
	@Override
	public Counter<Agent> newAffinities(Agent agent) {
		if(!affins.containsKey(agent) || Rand.nextDouble() < changeProb)
			return chooseNewObsession(agent);
		return affins.getCounter(agent);
	}

	@Override
	public Counter<Agent> initialAffinities(Agent agent) {
		return newAffinities(agent);
	}

}
