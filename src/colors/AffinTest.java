package colors;

import java.util.Map.Entry;

import colors.util.Counter;
import colors.util.CounterMap;

public class AffinTest {
	public static void main(String[] args) {
		final double close = 1;
		final double far = 3;
		
		final CounterMap<String,String> clusters = new CounterMap<String,String>();
		clusters.setCount("1", "2", close);
		clusters.setCount("1", "3", far);
		clusters.setCount("1", "4", far);
		clusters.setCount("2", "1", close);
		clusters.setCount("2", "3", far);
		clusters.setCount("2", "4", far);
		clusters.setCount("3", "1", far);
		clusters.setCount("3", "2", far);
		clusters.setCount("3", "4", close);
		clusters.setCount("4", "1", far);
		clusters.setCount("4", "2", far);
		clusters.setCount("4", "3", close);
		
		final CounterMap<String,String> smallUniform = new CounterMap<String,String>();
		smallUniform.setCount("1", "2", close);
		smallUniform.setCount("1", "3", close);
		smallUniform.setCount("1", "4", close);
		smallUniform.setCount("2", "1", close);
		smallUniform.setCount("2", "3", close);
		smallUniform.setCount("2", "4", close);
		smallUniform.setCount("3", "1", close);
		smallUniform.setCount("3", "2", close);
		smallUniform.setCount("3", "4", close);
		smallUniform.setCount("4", "1", close);
		smallUniform.setCount("4", "2", close);
		smallUniform.setCount("4", "3", close);
		
		final CounterMap<String,String> largeUniform = new CounterMap<String,String>();
		largeUniform.setCount("1", "2", far);
		largeUniform.setCount("1", "3", far);
		largeUniform.setCount("1", "4", far);
		largeUniform.setCount("2", "1", far);
		largeUniform.setCount("2", "3", far);
		largeUniform.setCount("2", "4", far);
		largeUniform.setCount("3", "1", far);
		largeUniform.setCount("3", "2", far);
		largeUniform.setCount("3", "4", far);
		largeUniform.setCount("4", "1", far);
		largeUniform.setCount("4", "2", far);
		largeUniform.setCount("4", "3", far);
		
		printHeader();
		printMetrics("smallUniform", smallUniform);
		printMetrics("largeUniform", largeUniform);
		printMetrics("clusters", clusters);
	}
	
	private static void printHeader() {
		System.out.println("name,totalAffins,avgAffins,totalEntropy,avgEntropy,totalNormalizedEntropy,entropyPerAgent");
	}
	
	private static <A> void printMetrics(final String name, final CounterMap<A,A> affins) {
		final StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append(',');
		sb.append(total(affins));
		sb.append(',');
		sb.append(averageAffinity(affins));
		sb.append(',');
		sb.append(totalEntropy(affins));
		sb.append(',');
		sb.append(averageEntropy(affins));
		sb.append(',');
		sb.append(totalNormalizedEntropy(affins));
		sb.append(',');
		sb.append(entropyPerAgent(affins));
		System.out.println(sb.toString());
	}
	
	private static <A> double averageEntropy(final CounterMap<A,A> affins) {
		final int numAgents = affins.keySet().size();
		return totalEntropy(affins) / (double) ( numAgents * (numAgents-1));
	}
	
	private static <A> double entropyPerAgent(final CounterMap<A,A> affins) {
		return totalEntropy(affins) / (double) affins.keySet().size();
	}
	
	private static <A> double totalEntropy(final CounterMap<A,A> affins) {
		double sum = 0.0;
		for(Entry<A,Counter<A>> entry : affins.entrySet()) {
			sum += entropy(entry.getValue());
		}
		return sum;
	}
	
	private static <A> double entropy(final Counter<A> dist) {
		double sum = 0.0;
		for(Entry<A,Double> entry : dist.entrySet()) {
			sum += (entry.getValue().doubleValue() * Math.log(entry.getValue().doubleValue()));
		}
		return sum;
	}
	
	private static <A> double totalNormalizedEntropy(final CounterMap<A,A> affins) {
		double sum = 0.0;
		for(Entry<A,Counter<A>> entry : affins.entrySet()) {
			sum += normalizedEntropy(entry.getValue());
		}
		return sum;
	}
	
	private static <A> double normalizedEntropy(final Counter<A> dist) {
		return entropy(dist) / total(dist);
	}
	
	private static <A> double averageAffinity(final CounterMap<A,A> affins) {
		final int numAgents = affins.keySet().size();
		return total(affins) / (double) ( numAgents * (numAgents-1));
	}
	
	private static <A> double total(final CounterMap<A,A> affins) {
		return affins.totalCount();
	}
	
	private static <A> double total(final Counter<A> dist) {
		return dist.totalCount();
	}
}
