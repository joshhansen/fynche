/*
 * Fynche - a Framework for Multiagent Computational Creativity
 * Copyright 2011 Josh Hansen
 * 
 * This file is part of the Fynche <https://github.com/joshhansen/fynche>.
 * 
 * Fynche is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Fynche is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with Fynche.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * If you have inquiries regarding any further use of Fynche, please
 * contact Josh Hansen <http://joshhansen.net/>
 */
package fynche.util;

import weka.estimators.KernelEstimator;

public class SampleableKernelEstimator extends KernelEstimator {
	private static final long serialVersionUID = 1L;

	public SampleableKernelEstimator(double precision) {
		super(precision);
	}
	
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
		
		final double position = Rand.nextDouble();
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
