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
	private static Pattern tokenRegex = Pattern.compile("[a-z]+");
	
	private static List<String> tokenize(final String s) {
		List<String> parts = new ArrayList<String>();
		final Matcher m = tokenRegex.matcher(s.toLowerCase());
		while(m.find()) {
			parts.add(m.group());
		}
		return parts;
	}
	
	private static final class KDEColorModel implements PreferenceModel, Sampleable<NamedColor> {
		private final SampleableKernelEstimator r;
		private final SampleableKernelEstimator g;
		private final SampleableKernelEstimator b;
		private final Counter<String> words;

		private KDEColorModel(SampleableKernelEstimator r, SampleableKernelEstimator g,
				SampleableKernelEstimator b, Counter<String> words) {
			this.b = b;
			this.r = r;
			this.words = words;
			this.g = g;
		}

		@Override
		public double preference(Artefact artefact) {
			if(artefact instanceof NamedColor) {
				NamedColor nc = (NamedColor) artefact;
				final double prob_r = r.getProbability(nc.getColor().getRed());
				final double prob_g = g.getProbability(nc.getColor().getGreen());
				final double prob_b = b.getProbability(nc.getColor().getBlue());
				
				double prob_w = 1.0;
				for(final String w_n : tokenize(nc.getName())) {
					prob_w *= words.getCount(w_n);
				}
				return prob_r*prob_g*prob_b*prob_w;
			} else throw new IllegalArgumentException();
		}

		@Override
		public NamedColor sample() {
			final int r_ = r.sample(0, 255, 1).intValue();
			final int g_ = g.sample(0, 255, 1).intValue();
			final int b_ = b.sample(0, 255, 1).intValue();
			final String word = words.sample();
			return new NamedColor(r_, g_, b_, word);
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
		for(Artefact a : agentB.publishedArtefacts()) {
			if(a instanceof NamedColor) {
				NamedColor nc = (NamedColor) a;
				Color color = nc.getColor();
				r.addValue(color.getRed(), 1);
				g.addValue(color.getGreen(), 1);
				b.addValue(color.getBlue(), 1);
				
				final String name = nc.getName();
				words.incrementAll(tokenize(name));
			}
		}
		words.normalize();
		
		final KDEColorModel model = new KDEColorModel(r, g, b, words);
//		System.out.println("sample: " + model.sample());
		return model;
	}
}
