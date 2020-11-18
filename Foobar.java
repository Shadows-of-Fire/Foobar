/**
 * Debug/test file used to run solutions in dev
 */
public class Foobar {

	public static void main(String[] args) {
		printf("%s", DodgeTheLasers.solution("1111111110"));
		long sum = 0;
		for (long i = 1; i <= 1111111110L; i++) {
			sum += (long) Math.floor(Math.sqrt(2) * i);
		}
		printf("%s", sum);
	}

	static void printf(String s, Object... args) {
		System.out.printf(s + '\n', args);
	}

	static void printMatrix(int[][] matrix) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < matrix.length; i++) {
			b.append("[");
			for (int j = 0; j < matrix[i].length; j++) {
				b.append(matrix[i][j]).append(j == matrix[i].length - 1 ? "" : ", ");
			}
			b.append("]\n");
		}
		printf(b.toString());
	}

	static <T> void printMatrix(T[][] matrix) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < matrix.length; i++) {
			b.append("[");
			for (int j = 0; j < matrix[i].length; j++) {
				b.append(matrix[i][j]).append(j == matrix[i].length - 1 ? "" : ", ");
			}
			b.append("]\n");
		}
		printf(b.toString());
	}

}
