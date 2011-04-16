package colors.affinities;

import java.util.Map.Entry;

import colors.interfaces.AffinityUpdater;
import colors.interfaces.Agent;
import colors.util.Counter;
import colors.util.Rand;

public class ModerateNormalizedEntropyAffinityUpdater implements AffinityUpdater {
	private final double l;
	private final double m;
	private final double stepSize;
	
	public ModerateNormalizedEntropyAffinityUpdater(final double l, final double m, final double stepSize) {
		this.l = l;
		this.m = m;
		this.stepSize = stepSize;
	}

	@Override
	public Counter<Agent> newAffinities(Agent agent) {
		return followPartialDerivatives(agent);
	}
	
	private Counter<Agent> followPartialDerivatives(final Agent me) {
		final Counter<Agent> currentAffins = new Counter<Agent>(me.affinities());
		final Agent[] otherAgents = currentAffins.keySet().toArray(new Agent[0]);
		final Agent other = otherAgents[Rand.nextInt(otherAgents.length)];
		final double originalCount = currentAffins.getCount(other);
		assert(other != me);
//			final double originalCount = entry.getValue().doubleValue();
		final double partialDerivative = partialDerivative(currentAffins, other);
		final double delta = partialDerivative * stepSize;
		
		final double newCount = Math.max(0.0, originalCount+delta);
		assert(!Double.isNaN(newCount));
		currentAffins.setCount(other, newCount);
		
		currentAffins.normalize();
			
		return currentAffins;
	}
	
	private double partialDerivative(final Counter<Agent> affins, final Agent withRespectToThisAgent) {
		final double entropy = entropy(affins);
		final double sum = sum(affins);
		final double log = smartLog(affins.getCount(withRespectToThisAgent));
		final double firstTerm = 2.0*l*entropy/sum + m;
		final double secondTermNum = (-log-1.0)*sum - entropy;
		final double secondTermDenom = Math.pow(sum, 2.0);
		final double partialDerivative = secondTermDenom <= 0.0 ? 0.0 : firstTerm * secondTermNum / secondTermDenom;
		assert(!Double.isNaN(partialDerivative));
		return partialDerivative;
	}

	private static <A> double entropy(final Counter<A> dist) {
		double sum = 0.0;
		for(Entry<A,Double> entry : dist.entrySet()) {
			final double value = entry.getValue().doubleValue();
			assert(!Double.isNaN(value));
			final double log = smartLog(value);
			assert(!Double.isNaN(log));
			sum += value * log;
		}
		assert(!Double.isNaN(sum));
		return sum;
	}
	
	private static <A> double sum(final Counter<A> dist) {
		double sum = 0.0;
		for(Entry<A,Double> entry : dist.entrySet()) {
			final double value = entry.getValue().doubleValue();
			assert(!Double.isNaN(value));
			sum += value;
		}
		return sum;
	}
	
	private static double smartLog(final double x) {
		return x == 0.0 ? 0.0 : Math.log(x);
	}
	
//	private static double maxEntropy(final int domainSize) {
//		final double massAtEachPoint = 1.0 / (double) domainSize;
//		return (double) domainSize * massAtEachPoint * Math.log(massAtEachPoint);
//	}
}
