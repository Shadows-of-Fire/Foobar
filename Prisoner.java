public class Prisoner {
	public static String solution(int x, int y) {
		long n = 0;
		for (int i = 1; i <= x; i++) {
			n += i;
		}
		for (int i = 1; i < y; i++) {
			n += (x + i - 1);
		}
		return String.valueOf(n);
	}
}
