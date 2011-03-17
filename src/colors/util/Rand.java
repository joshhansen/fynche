package colors.util;

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
