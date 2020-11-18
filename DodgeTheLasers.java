import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Brennan Ward
 * 
 * Dodge The Lasers asks, given a number n in the range [10, 10^100],
 * that you calculate sum from i = 1..n of floor(i * sqrt(2)).
 * 
 * This is an application of a Beatty Sequence.  Specifically https://oeis.org/A001951
 * 
 * b = 1/(1 - 1/a) = 2 + sqrt(2)
 * n' = (a-1)n
 * S(a,n) = (n + n')(n + n' + 1)/2 - S(b, n')
 * if a = sqrt(2), b = 2 + sqrt(2)
 */
public class DodgeTheLasers {

	static final BigDecimal SQRT_DIG = new BigDecimal(150);
	static final BigDecimal SQRT_PRE = new BigDecimal(10).pow(150);
	static final BigDecimal SQRT_2 = new BigDecimal("1.4142135623730950488016887242096980785696718753769480731766797379907324784621070388503875343276415727350138462309122970249248360558507372126441214971");
	static final BigDecimal PRIME_FACTOR = SQRT_2.subtract(BigDecimal.ONE);
	static final BigInteger TWO = BigInteger.valueOf(2);

	public static String solution(String input) {
		BigInteger n = new BigInteger(input);
		return beattyA001951(n).toString();
	}

	public static BigInteger beattyA001951(BigInteger n) {
		if (n.equals(BigInteger.ZERO)) return BigInteger.ZERO;
		BigInteger nPrime = PRIME_FACTOR.multiply(new BigDecimal(n)).toBigInteger();
		BigInteger term1 = n.multiply(nPrime);
		BigInteger term2 = n.multiply(n.add(BigInteger.ONE)).divide(TWO);
		BigInteger term3 = nPrime.multiply(nPrime.add(BigInteger.ONE)).divide(TWO);
		return term1.add(term2).subtract(term3).subtract(beattyA001951(nPrime));
	}

}
