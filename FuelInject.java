import java.math.BigInteger;

/**
 * @author Brennan Ward
 * 
 * The Fuel Injectors problem asks "how fast can we get to one"
 * Given that the only operations are to add one, subtract one
 * and divide by two.
 */
public class FuelInject {

	static BigInteger three = BigInteger.valueOf(3);
	static BigInteger two = BigInteger.valueOf(2);
	static BigInteger one = BigInteger.ONE;

	public static int solution(String x) {
		BigInteger pellets = new BigInteger(x);
		int steps = 0;
		while (!pellets.equals(one)) {
			steps++;
			if (pellets.mod(two).intValue() == 0) {
				pellets = pellets.divide(two);
			} else {
				BigInteger up = pellets.add(one);
				BigInteger down = pellets.subtract(one);
				int ac = 0, bc = 0;
				while (up.mod(two).intValue() == 0) {
					up = up.divide(two);
					ac += 1;
				}
				while (down.mod(two).intValue() == 0) {
					down = down.divide(two);
					bc += 1;
				}
				if (ac > bc && pellets.compareTo(three) != 0) pellets = pellets.add(one);
				else pellets = pellets.subtract(one);
			}
			if (pellets.equals(one)) break;
		}
		return steps;
	}

}
