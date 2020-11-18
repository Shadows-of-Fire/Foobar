/**
 * @author Brennan Ward
 * 
 * Translates messages into braille.
 * Only handles [A-Z] + [a-z]
 */
public class Braille {

	static String[] braille = { "100000", "110000", "100100", "100110", "100010", "110100", "110110", "110010", "010100", "010110", "101000", "111000", "101100", "101110", "101010", "111100", "111110", "111010", "011100", "011110", "101001", "111001", "010111", "101101", "101111", "101011" };

	public static String solution(String s) {
		String out = "";
		for (char c : s.toCharArray()) {
			if (c == ' ') {
				out += "000000";
				continue;
			}
			if (Character.isUpperCase(c)) {
				out += "000001";
				c = Character.toLowerCase(c);
			}
			out += braille[c - 97];
		}
		return out;
	}

}
