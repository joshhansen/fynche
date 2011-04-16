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

import java.util.Random;

public class Rand {
	private static final Random rand = new Random();

	public static boolean nextBoolean() {
		return rand.nextBoolean();
	}

	public static void nextBytes(byte[] bytes) {
		rand.nextBytes(bytes);
	}

	public static double nextDouble() {
		return rand.nextDouble();
	}

	public static float nextFloat() {
		return rand.nextFloat();
	}

	public static double nextGaussian() {
		return rand.nextGaussian();
	}

	public static int nextInt() {
		return rand.nextInt();
	}

	public static int nextInt(int n) {
		return rand.nextInt(n);
	}

	public static long nextLong() {
		return rand.nextLong();
	}
}
