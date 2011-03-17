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
import colors.interfaces.Sampleable;
import colors.util.Counter;
import colors.util.CounterMap;
import colors.util.SampleableKernelEstimator;

public class DependentWordsPreferenceUpdater implements PreferenceUpdater {
	private static Pattern tokenRegex = Pattern.compile("(\\p{XDigit}\\p{XDigit}\\p{XDigit}\\p{XDigit}\\p{XDigit}\\p{XDigit})|(\\p{Alpha}+)|([0-9.]+)|(\\p{Punct}+)");
	
	private final double subdivSize;
	
	public DependentWordsPreferenceUpdater(final int axisSubdivisionCount) {
		this.subdivSize = 255.0 / (double)axisSubdivisionCount;
	}
	
	private static List<String> tokenize(final String s) {
		List<String> parts = new ArrayList<String>();
		final Matcher m = tokenRegex.matcher(s);
		while(m.find()) {
			parts.add(m.group());
		}
//		System.out.println(s + " => " + parts);
		return parts;
	}
	
	private String key(final int r, final int g, final int b) {
		final int r_div = (int) ((double)r / subdivSize);
		final int g_div = (int) ((double)g / subdivSize);
		final int b_div = (int) ((double)b / subdivSize);
		final StringBuilder key = new StringBuilder();
		key.append(r_div);
		key.append("_");
		key.append(g_div);
		key.append("_");
		key.append(b_div);
		return key.toString();
	}
	
	public class DependentWordsPreferenceModel implements PreferenceModel, Sampleable<NamedColor> {
		private final SampleableKernelEstimator r;
		private final SampleableKernelEstimator g;
		private final SampleableKernelEstimator b;
		private final CounterMap<String,String> words;
		private final CounterMap<String,Integer> tokenCount;

		private DependentWordsPreferenceModel(SampleableKernelEstimator r, SampleableKernelEstimator g,
				SampleableKernelEstimator b, CounterMap<String,String> words, CounterMap<String,Integer> tokenCount) {
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
				final String subdivKey = key(nc.getColor().getRed(), nc.getColor().getGreen(), nc.getColor().getBlue());
				final Counter<String> subdivWords = words.getCounter(subdivKey);
				final double log_prob_r = r.getLogProbability(nc.getColor().getRed());
				final double log_prob_g = g.getLogProbability(nc.getColor().getGreen());
				final double log_prob_b = b.getLogProbability(nc.getColor().getBlue());
				
				double log_prob_w = 0;
				for(final String w_n : tokenize(nc.getName())) {
					log_prob_w += subdivWords.getLogCount(w_n);
				}
				return Math.exp(log_prob_r+log_prob_g+log_prob_b+log_prob_w);
			} else throw new IllegalArgumentException();
		}
		
		@Override
		public NamedColor sample() {
			while(true) {
				final int r_ = r.sample(0, 255, 1).intValue();
				final int g_ = g.sample(0, 255, 1).intValue();
				final int b_ = b.sample(0, 255, 1).intValue();
				
				final String subdivKey = key(r_, g_, b_);
				final Counter<String> subdivWords = words.getCounter(subdivKey);
				if(!subdivWords.isEmpty()) {
					StringBuilder name = new StringBuilder();
					
					final int length = tokenCount.getCounter(subdivKey).sample();
					for(int i = 0; i < length; i++) {
						name.append(subdivWords.sample());
						name.append(' ');
					}
					return new NamedColor(r_, g_, b_, name.toString().trim());
				}
			}
		}
	}
	
	@Override
	public PreferenceModel newPreferences(Agent agentA, Agent agentB) {
		final SampleableKernelEstimator r = new SampleableKernelEstimator(1);
		final SampleableKernelEstimator g = new SampleableKernelEstimator(1);
		final SampleableKernelEstimator b = new SampleableKernelEstimator(1);
		final CounterMap<String,String> words = new CounterMap<String,String>();
		final CounterMap<String,Integer> tokenCounts = new CounterMap<String,Integer>();
		for(Artefact a : agentB.artefacts()) {
			if(a instanceof NamedColor) {
				NamedColor nc = (NamedColor) a;
				Color color = nc.getColor();
				final String subdivKey = key(color.getRed(), color.getGreen(), color.getBlue());
				r.addValue(color.getRed(), 1);
				g.addValue(color.getGreen(), 1);
				b.addValue(color.getBlue(), 1);
				
				final String name = nc.getName();
				final List<String> tokens = tokenize(name);
				words.getCounter(subdivKey).incrementAll(tokens);
				tokenCounts.getCounter(subdivKey).increment(tokens.size());
			}
		}
		words.normalize();
		
		final DependentWordsPreferenceModel model = new DependentWordsPreferenceModel(r, g, b, words, tokenCounts);
//		System.out.println("sample: " + model.sample());
		return model;
	}
}
