import java.math.BigInteger;

/**
 * @author Brennan Ward
 * 
 * The Bomb Baby problem asks how many operations are necessary given
 * that we can start with one M bomb and one F bomb.
 * Each operation, we can either increase the M bombs by the amount of
 * F bombs, or increase the F bombs by the amount of M bombs.
 * We are given the target amount of bombs, and asked to solve for
 * the number of operations.
 * 
 * To solve this question, we have to work backwards, starting at the end
 * and computing the number of steps to (1,1).  We must employ modular
 * arithmetic to consider the case where we have a subtantially large
 * number of one type of bombs, and a low number of the other to avoid
 * repeated subtractions, as these numbers can go up to 10^50.
 */
public class BombBaby {

	static BigInteger N_ONE = BigInteger.valueOf(-1);

	static String solution(String x, String y) {
		BigInteger targetMach = new BigInteger(x);
		BigInteger targetFac = new BigInteger(y);
		BombNode initial = new BombNode(BigInteger.ZERO, targetMach, targetFac);
		BigInteger steps = traverse(initial);
		return steps == N_ONE ? "impossible" : steps.toString();
	}

	/**
	 * We can only solve a node if the two numbers are different.
	 * So we short-circuit on nodes that are invalid.
	 */
	static boolean isInvalid(BombNode node) {
		return (node.fac.equals(BigInteger.ZERO) || node.mach.equals(BigInteger.ZERO)) || !node.fac.equals(BigInteger.ONE) && node.fac.equals(node.mach);
	}

	static BigInteger traverse(BombNode root) {
		while (true) {
			BigInteger m = root.mach;
			BigInteger f = root.fac;
			if (m.equals(f) && m.equals(BigInteger.ONE)) return root.steps;
			if (m.compareTo(f) > 0) {
				root = new BombNode(root.steps.add(m.divide(f)), m.mod(f), f);
			} else {
				root = new BombNode(root.steps.add(f.divide(m)), m, f.mod(m));
			}
			if (root.mach.equals(BigInteger.ZERO) && root.fac.equals(BigInteger.ONE)) {
				root.mach = BigInteger.ONE;
				root.steps = root.steps.subtract(BigInteger.ONE);
			} else if (root.fac.equals(BigInteger.ZERO) && root.mach.equals(BigInteger.ONE)) {
				root.fac = BigInteger.ONE;
				root.steps = root.steps.subtract(BigInteger.ONE);
			}
			if (isInvalid(root)) break;
		}
		return N_ONE;
	}

	static class BombNode {

		BigInteger mach, fac;
		BigInteger steps;

		public BombNode(BigInteger steps, BigInteger mach, BigInteger fac) {
			this.mach = mach;
			this.fac = fac;
			this.steps = steps;
		}

	}

}
