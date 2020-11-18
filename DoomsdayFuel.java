import java.util.ArrayList;
import java.util.List;

/**
 * @author Brennan Ward
 * 
 * The doomsday fuel problem is an example of an Absorbing Markov Chain problem.
 * The inputs are given as a state matrix, in which each row in the matrix
 * represents the chance of a piece of fuel in that state to jump to the
 * state as indicated by it's position in the row.
 * Initially, these are weights, relative to the sum of the entire row.
 * 
 * The expected output is an integer array of the chances to end up in every state.
 * However, the output is not expected as decimals, but rather numerators to a series
 * of common fractions.  The final integer in the output array is the denominator.
 * 
 * Note: This solution does not actually run on the Google Foobar emulator.
 * It fails all test cases, even ones that I can verify it produces the right outputs for.
 * I was never able to figure out why, and instead used a different solution that passed.
 * This solution is mine, and it works for all test cases (as found online).
 */
public class DoomsdayFuel {

	public static int[] solution(int[][] m) {
		List<Integer> terminals = new ArrayList<>();
		Rational[][] matrix = toRational(m, terminals);
		int k = 0;
		for (int i : terminals) {
			swapRowOrdered(matrix, k++, i);
		}
		k = 0;
		for (int i : terminals) {
			swapColOrdered(matrix, k++, i);
		}
		Rational[][] q = submatrix2(matrix, terminals.size(), terminals.size());
		Rational[][] identity = identity(q.length);
		Rational[][] iMinusQ = matrixSub(identity, q);
		Rational[][] f = inverse(iMinusQ);
		Rational[][] r = submatrix3(matrix, terminals.size(), terminals.size());
		Rational[][] fr = matrixMul(f, r);
		Foobar.printMatrix(fr);
		return fr.length == 0 ? new int[] { 1, 1 } : formatToOutputSpec(fr[0]);
	}

	public static Rational[][] toRational(int[][] m, List<Integer> terminalHolder) {
		Rational[][] matrix = new Rational[m.length][m.length];
		for (int i = 0; i < m.length; i++) {
			int sum = 0;
			for (int j = 0; j < m.length; j++) {
				sum += m[i][j];
			}
			if (sum == 0) terminalHolder.add(i);
			for (int j = 0; j < m.length; j++) {
				if (sum == 0) matrix[i][j] = new Rational(i == j ? 1 : 0, 1);
				else matrix[i][j] = new Rational(m[i][j], sum);
			}
		}
		return matrix;
	}

	public static int[] formatToOutputSpec(Rational[] arr) {
		long[] out = new long[arr.length + 1];
		long denom = 0;
		for (int i = 0; i < arr.length; i++) {
			Rational r = arr[i];
			int factor = 1;
			for (int j = 0; j < arr.length; j++) {
				if (j != i) factor *= arr[j].denom;
			}
			r = r.rebase(factor);
			out[i] = r.num;
			denom = r.denom;
		}
		long gcd = gcd(out, arr.length);
		out[arr.length] = (int) (denom / gcd);
		for (int i = 0; i < arr.length; i++) {
			out[i] = out[i] / gcd;
		}
		int[] rOut = new int[arr.length + 1];
		for (int i = 0; i < out.length; i++) {
			rOut[i] = (int) out[i];
		}
		return rOut;
	}

	public static Rational[][] matrixSub(Rational[][] m1, Rational[][] m2) {
		Rational[][] out = new Rational[m1.length][m1.length];
		for (int i = 0; i < out.length; i++) {
			for (int j = 0; j < out.length; j++) {
				out[i][j] = m1[i][j].sub(m2[i][j]);
			}
		}
		return out;
	}

	/**
	 * To multiply an m×n matrix by an n×p matrix, the ns must be the same,
	 *	and the result is an m×p matrix.
	 */
	public static Rational[][] matrixMul(Rational[][] m1, Rational[][] m2) {
		if (m1.length == 0) return m1;
		Rational[][] out = new Rational[m1.length][m2[0].length];
		for (int i = 0; i < out.length; i++) {
			for (int j = 0; j < out[0].length; j++) {
				Rational[] row = m1[i];
				Rational dot = new Rational(0, 1);
				for (int k = 0; k < row.length; k++) {
					dot = dot.add(row[k].mul(m2[k][j]));
				}
				out[i][j] = dot;
			}
		}
		return out;
	}

	public static class Rational {
		public long num, denom;

		public Rational(long num, long denom) {
			this.num = num;
			this.denom = denom;
		}

		Rational mul(Rational other) {
			Rational r = new Rational(this.num * other.num, this.denom * other.denom);
			r.reduce();
			return r;
		}

		Rational rebase(int factor) {
			Rational r = new Rational(this.num * factor, this.denom * factor);
			return r;
		}

		Rational add(Rational other) {
			if (this.denom == other.denom) return new Rational(this.num + other.num, this.denom);
			Rational r = new Rational(this.num * other.denom + other.num * this.denom, this.denom * other.denom);
			r.reduce();
			return r;
		}

		Rational sub(Rational other) {
			if (this.denom == other.denom) return new Rational(this.num - other.num, this.denom);
			Rational r = new Rational(this.num * other.denom - other.num * this.denom, this.denom * other.denom);
			r.reduce();
			return r;
		}

		Rational reciprocol() {
			return new Rational(this.denom, this.num);
		}

		void reduce() {
			if (this.num == 0) this.denom = 1;
			else {
				long gcd = gcd(num, denom);
				if (gcd != 1) {
					this.num /= gcd;
					this.denom /= gcd;
				}
			}
		}

		@Override
		public String toString() {
			return num + "/" + denom;
		}
	}

	public static long gcd(long x, long y) {
		int gcd = 1;
		for (int i = 1; i <= x && i <= y; i++) {
			if (x % i == 0 && y % i == 0) gcd = i;
		}
		return gcd;
	}

	public static long zeroGcd(long a, long b) {
		if (a == 0) return b;
		return zeroGcd(b % a, a);
	}

	public static long gcd(long[] a, int n) {
		long gcd = a[0];

		for (int i = 1; i < n; i++) {
			gcd = zeroGcd(gcd, a[i]);
		}

		return gcd;
	}

	public static int getSum(int[] row) {
		int sum = 0;
		for (int i : row)
			sum += i;
		return sum;
	}

	public static boolean isBaseTerminal(int[] arr) {
		for (int i : arr)
			if (i != 0) return false;
		return true;
	}

	/**
	 * Swaps two rows in the matrix.
	 */
	public static void swapRow(Rational[][] matrix, int a, int b) {
		Rational[] rowA = matrix[a];
		matrix[a] = matrix[b];
		matrix[b] = rowA;
	}

	/*
	 * Swaps two rows incrementally as to retain the original order of the others.
	 */
	public static void swapRowOrdered(Rational[][] matrix, int a, int b) {
		for (int i = b; i > a; i--) {
			swapRow(matrix, i, i - 1);
		}
	}

	/**
	 * Swaps two columns in the matrix.
	 */
	public static void swapCol(Rational[][] matrix, int a, int b) {
		Rational[] colA = new Rational[matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			colA[i] = matrix[i][a];
		}
		for (int i = 0; i < matrix.length; i++) {
			matrix[i][a] = matrix[i][b];
			matrix[i][b] = colA[i];
		}
	}

	/*
	 * Swaps two columns incrementally as to retain the original order of the others.
	 */
	public static void swapColOrdered(Rational[][] matrix, int a, int b) {
		for (int i = b; i > a; i--) {
			swapCol(matrix, i, i - 1);
		}
	}

	public static Rational det(Rational[][] matrix) {
		if (matrix.length == 1) return matrix[0][0];
		Rational det = new Rational(0, 1);
		for (int col = 0; col < matrix.length; col++) {
			Rational subDet = matrix[0][col].mul(det(cofactor(matrix, col)));
			det = det.add(col % 2 == 0 ? subDet : subDet.mul(new Rational(-1, 1)));
		}
		return det;
	}

	/**
	 * Calculates the cofactor of a matrix relative to the given column, 0th row.
	 * The cofactor is M-1 X M-1 relative to the MXM input matrix.
	 */
	public static Rational[][] cofactor(Rational[][] matrix, int col) {
		return submatrix(matrix, 0, col);
	}

	/**
	 * Creates a submatrix by removing the row-th row and col-th column from the matrix.
	 */
	public static Rational[][] submatrix(Rational[][] matrix, int row, int col) {
		Rational[][] submatrix = new Rational[matrix.length - 1][matrix.length - 1];
		for (int i = 0; i < matrix.length; i++) {
			if (i == row) continue;
			for (int j = 0; j < matrix.length; j++) {
				if (j == col) continue;
				submatrix[i > row ? i - 1 : i][j > col ? j - 1 : j] = matrix[i][j];
			}
		}
		return submatrix;
	}

	/**
	 * Creates a submatrix by removing all rows and columns before the selected ones.
	 */
	public static Rational[][] submatrix2(Rational[][] matrix, int row, int col) {
		Rational[][] submatrix = new Rational[matrix.length - row][matrix.length - col];
		for (int i = 0; i < matrix.length; i++) {
			if (i < row) continue;
			for (int j = 0; j < matrix.length; j++) {
				if (j < col) continue;
				submatrix[i - row][j - col] = matrix[i][j];
			}
		}
		return submatrix;
	}

	/**
	 * Creates a submatrix by removing all rows after the selected
	 * and retaining all columns before the selected.
	 */
	public static Rational[][] submatrix3(Rational[][] matrix, int row, int col) {
		Rational[][] submatrix = new Rational[matrix.length - row][col];
		for (int i = 0; i < matrix.length; i++) {
			if (i < row) continue;
			for (int j = 0; j < col; j++) {
				submatrix[i - row][j] = matrix[i][j];
			}
		}
		return submatrix;
	}

	/**
	 * Creates an identity matrix of size n.
	 */
	public static Rational[][] identity(int n) {
		Rational[][] matrix = new Rational[n][n];
		Rational zero = new Rational(0, 1);
		Rational one = new Rational(1, 1);
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (i == j) matrix[i][j] = one;
				else matrix[i][j] = zero;
			}
		}
		return matrix;
	}

	/**
	 * Calculates the adjugate matrix for the given matrix.
	 */
	public static Rational[][] adjugate(Rational[][] matrix) {
		Rational[][] adjugate = new Rational[matrix.length][matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				Rational det = det(submatrix(matrix, j, i));
				adjugate[i][j] = (i + j) % 2 == 0 ? det : det.mul(new Rational(-1, 1));
			}
		}
		return adjugate;
	}

	public static Rational[][] inverse(Rational[][] matrix) {
		Rational[][] adj = adjugate(matrix);
		Rational detInv = det(matrix).reciprocol();
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				adj[i][j] = detInv.mul(adj[i][j]);
			}
		}
		return adj;
	}
}