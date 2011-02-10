package colors.prefs;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import colors.artefacts.NamedColor;
import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.PreferenceModel;
import colors.interfaces.PreferenceUpdater;
import colors.util.Counter;
import colors.util.SampleableKernelEstimator;

public class IndependentKDEPreferenceUpdater implements PreferenceUpdater {
	private static Pattern tokenRegex = Pattern.compile("(\\p{Alpha}+)|([0-9.]+)|(\\p{Punct}+)");
	
	private static final String STOP_TOKEN = "<<<STOP>>>";
	private static List<String> tokenize(final String s) {
		List<String> parts = new ArrayList<String>();
		final Matcher m = tokenRegex.matcher(s);
		while(m.find()) {
			parts.add(m.group());
		}
		parts.add(STOP_TOKEN);
		return parts;
	}
	
	private static final class KDEColorModel implements PreferenceModel, Sampleable<NamedColor> {
		private final SampleableKernelEstimator r;
		private final SampleableKernelEstimator g;
		private final SampleableKernelEstimator b;
		private final Counter<String> words;
		private final Counter<Integer> tokenCount;

		private KDEColorModel(SampleableKernelEstimator r, SampleableKernelEstimator g,
				SampleableKernelEstimator b, Counter<String> words, Counter<Integer> tokenCount) {
			this.r = r;
			this.g = g;
			this.b = b;
			this.words = words;
			this.tokenCount = tokenCount;
		}

		@Override
		public double preference(Artefact artefact) {
			if(artefact instanceof NamedColor) {
				NamedColor nc = (NamedColor) artefact;
				final double log_prob_r = r.getLogProbability(nc.getColor().getRed());
				final double log_prob_g = g.getLogProbability(nc.getColor().getGreen());
				final double log_prob_b = b.getLogProbability(nc.getColor().getBlue());
				
				double log_prob_w = 0;
				for(final String w_n : tokenize(nc.getName())) {
					log_prob_w += words.getLogCount(w_n);
				}
				return Math.exp(log_prob_r+log_prob_g+log_prob_b+log_prob_w);
			} else throw new IllegalArgumentException();
		}

		@Override
		public NamedColor sample() {
			final int r_ = r.sample(0, 255, 1).intValue();
			final int g_ = g.sample(0, 255, 1).intValue();
			final int b_ = b.sample(0, 255, 1).intValue();
			StringBuilder name = new StringBuilder();
			
			final int length = tokenCount.sample();
			for(int i = 0; i < length; i++) {
				name.append(words.sample());
				name.append(' ');
			}
			return new NamedColor(r_, g_, b_, name.toString().trim());
		}
	}

	public interface Sampleable<S> {
		public S sample();
	}
	
	@Override
	public PreferenceModel newPreferences(Agent agentA, Agent agentB) {
		final SampleableKernelEstimator r = new SampleableKernelEstimator(1);
		final SampleableKernelEstimator g = new SampleableKernelEstimator(1);
		final SampleableKernelEstimator b = new SampleableKernelEstimator(1);
		final Counter<String> words = new Counter<String>();
		final Counter<Integer> tokenCounts = new Counter<Integer>();
		for(Artefact a : agentB.publishedArtefacts()) {
			if(a instanceof NamedColor) {
				NamedColor nc = (NamedColor) a;
				Color color = nc.getColor();
				r.addValue(color.getRed(), 1);
				g.addValue(color.getGreen(), 1);
				b.addValue(color.getBlue(), 1);
				
				final String name = nc.getName();
				final List<String> tokens = tokenize(name);
				words.incrementAll(tokens);
				tokenCounts.increment(tokens.size());
			}
		}
		words.normalize();
		
		final KDEColorModel model = new KDEColorModel(r, g, b, words, tokenCounts);
//		System.out.println("sample: " + model.sample());
		return model;
	}
}
