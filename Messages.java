/**
 * @author Brennan Ward
 * 
 */
public class Messages {

	public static int[] solution(int[] l, int t) {
		for (int i = 0; i < l.length; i++) {
			int sum = 0;
			for (int j = i; j < l.length; j++) {
				sum += l[j];
				if (sum == t) return new int[] { i, j };
				else if (sum > t) break;
			}
		}
		return new int[] { -1, -1 };
	}

}
