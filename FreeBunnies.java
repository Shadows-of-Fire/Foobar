import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Brennan Ward
 * 
 * Google FooBar Level 4: Free the Bunny Prisoners.
 * Every door requires N keys, and we have B bunnies.
 * We need to know the combinations such that we can distribute keys so that any Z of the B bunnies can open the door.
 */
public class FreeBunnies {

	/**
	 * Input guarantees: 
	 * bunnies >= required
	 * bunnies is within [1-9]
	 * required is within [1-9]
	 * 
	 * In the case where bunnies == required, we return an array of single-element arrays of increasing values.
	 * In the case where required == 1, we return an array of single-element arrays of zeroes.
	 * What's complicated is the case where neither of these is true.
	 * In this final case, we have (bunnies, required - 1) (as a binomial coefficient) keys.
	 * Then, there are bunnies - required + 1 copies of each key.
	 * Each bunny holds (keys * keyCopies / bunnies) keys.
	 */
	public static int[][] solution(int bunnies, int required) {
		int keys = binomCoeff(bunnies, required - 1);
		int keyCopies = bunnies - required + 1;
		Bunny[] bunList = new Bunny[bunnies];
		for (int i = 0; i < bunnies; i++)
			bunList[i] = new Bunny();

		List<Bunny[]> combos = new ArrayList<>();

		combinations(bunList, keyCopies, 0, new Bunny[keyCopies], combos);

		for (int i = 0; i < keys; i++) {
			for (Bunny b : combos.get(i))
				b.keys.add(i);
		}
		return toOutput(bunList);
	}

	static int[][] toOutput(Bunny[] bunnies) {
		int[][] out = new int[bunnies.length][bunnies[0].keys.size()];
		for (int i = 0; i < bunnies.length; i++) {
			List<Integer> keys = bunnies[i].keys;
			for (int j = 0; j < keys.size(); j++) {
				out[i][j] = keys.get(j);
			}
		}
		return out;
	}

	private static class Bunny {
		List<Integer> keys = new ArrayList<>();

		@Override
		public String toString() {
			return "Bunny: " + Arrays.toString(keys.toArray());
		}
	}

	public static int binomCoeff(int m, int n) {
		return factorial(m) / (factorial(n) * factorial(m - n));
	}

	public static int factorial(int n) {
		int fac = 1;
		for (int i = 2; i <= n; i++) {
			fac *= i;
		}
		return fac;
	}

	static void combinations(Bunny[] arr, int len, int startPosition, Bunny[] result, List<Bunny[]> combos) {
		if (len == 0) {
			combos.add(Arrays.copyOf(result, result.length));
			return;
		}
		for (int i = startPosition; i <= arr.length - len; i++) {
			result[result.length - len] = arr[i];
			combinations(arr, len - 1, i + 1, result, combos);
		}
	}

}
