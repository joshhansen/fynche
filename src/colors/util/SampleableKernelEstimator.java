package colors.util;

import java.util.Random;

import weka.estimators.KernelEstimator;

public class SampleableKernelEstimator extends KernelEstimator {
	private static final long serialVersionUID = 1L;

	public SampleableKernelEstimator(double precision) {
		super(precision);
	}

	private static final Random rand = new Random();
	public Double sample(final double min, final double max, final double stepSize) {
		final int binCount = (int) ((max-min)/stepSize);
		double totalProb = 0.0;
		double[] vals = new double[binCount];
		double[] probs = new double[binCount];
		int i = 0;
		for(double x = min; x < max; x += stepSize) {
			vals[i] = x;
			probs[i] = getProbability(x);
			totalProb += probs[i];
			i++;
		}
		
		final double position = rand.nextDouble();
		double sum = 0.0;
		for(int j = 0; j < binCount; j++) {
			final double normedProb = probs[j] / totalProb;
			if(sum >= position || sum+normedProb>=0.999999999)
				return vals[j];
			sum += normedProb;
		}
		return null;
	}
	
	public double getLogProbability(double data) {
		final double prob = super.getProbability(data);
		if(prob == 0) return 0.0;
		return Math.log(prob);
	}

}
