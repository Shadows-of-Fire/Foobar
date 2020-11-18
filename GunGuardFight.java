import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Brennan Ward
 * 
 * The "Bringing a Gun to a Guard Fight" problem asks,
 * given a rectangular room, your location, the guard's location,
 * and how far the laser can travel before becoming harmless,
 * how many directions can you shoot the laser in and hit the guard.
 * 
 * The laser immediately terminates upon hitting the guard 
 * or yourself, and you aren't allowed to hit yourself anyway.
 * 
 * There is an infinite amount of angles we can check,
 * so we need another approach rather than checking all angles.
 */
public class GunGuardFight {

	/**
	 * Calculates how many different shots we can take at the guard.
	 * @param dimensions The width/length of the room.
	 * @param ourPos Our coordinates.
	 * @param guardPos The guard's coordinates.
	 * @param distance Maximum laser distance.
	 * @return How many shots can be made at the guard.
	 */
	public static int solution(int[] dimensions, int[] ourPos, int[] guardPos, int distance) {
		List<int[]> positions = mirrorRoom(dimensions, ourPos, guardPos, distance);
		positions = positions.stream().filter(pos -> distSq(ourPos[0], ourPos[1], pos[0], pos[1]) <= distance * distance).collect(Collectors.toList());

		//So now we have all positions within range, but we need to keep only the closest entity per angle.

		Map<Double, int[]> uniques = new HashMap<>();

		for (int[] pos : positions) {
			if (pos[0] == ourPos[0] && pos[1] == ourPos[1]) continue;
			double angle = Math.atan2(pos[0] - ourPos[0], pos[1] - ourPos[1]);
			if (!uniques.containsKey(angle)) {
				uniques.put(angle, pos);
			} else {
				int[] existing = uniques.get(angle);
				int distSqEx = distSq(existing[0], existing[1], ourPos[0], ourPos[1]);
				int distSqNew = distSq(pos[0], pos[1], ourPos[0], ourPos[1]);
				if (distSqEx > distSqNew) {
					uniques.put(angle, pos);
				}
			}
		}

		return (int) uniques.values().stream().filter(a -> a[2] == 1).count();
	}

	/**
	 * We can't check infinite shots against the walls, but we can determine where in space
	 * the guard might be.  By creating mirrors of the room, we can emulate the laser
	 * boucing off the wall, which allows us to compute the possible shots in a reasonable time.
	 * 
	 * Returns a list of all positions of mirrored versions of us and the guard.
	 * The int arrays are 3-length, the 3rd index is an id.  We have id=0.
	 */
	public static List<int[]> mirrorRoom(int[] dim, int[] us, int[] guard, int dist) {
		int xMax = us[0] + dist;
		int yMax = us[1] + dist;
		//We need to create this many copies of the room along these axis.
		int xExpansions = (int) Math.ceil(Math.log10((double) xMax / dim[0]) / Math.log10(2));
		int yExpansions = (int) Math.ceil(Math.log10((double) yMax / dim[1]) / Math.log10(2));
		List<int[]> firstQuad = new ArrayList<>();
		//We need to calculate in all 4 directions, but mirrors along the first quadrant
		//are easiest, and we can then do a single mirror of the entire list for the rest.
		firstQuad.add(new int[] { us[0], us[1], 0 }); //We have id=0
		firstQuad.add(new int[] { guard[0], guard[1], 1 }); //Guards have id=1
		int height = dim[1];
		for (int i = 0; i < yExpansions; i++) {
			List<int[]> temp = new LinkedList<>();
			for (int[] pos : firstQuad) {
				temp.add(new int[] { pos[0], -(pos[1] - height) + height, pos[2] });
			}
			firstQuad.addAll(temp);
			height *= 2;
		}

		int width = dim[0];
		for (int i = 0; i < xExpansions; i++) {
			List<int[]> temp = new LinkedList<>();
			for (int[] pos : firstQuad) {
				temp.add(new int[] { -(pos[0] - width) + width, pos[1], pos[2] });
			}
			firstQuad.addAll(temp);
			width *= 2;
		}

		List<int[]> positions = new ArrayList<>();
		for (int[] pos : firstQuad) {
			positions.add(pos);
			positions.add(new int[] { pos[0] * -1, pos[1], pos[2] });
			positions.add(new int[] { pos[0] * -1, pos[1] * -1, pos[2] });
			positions.add(new int[] { pos[0], pos[1] * -1, pos[2] });
		}
		return positions;
	}

	public static int distSq(int x1, int y1, int x2, int y2) {
		return (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
	}

}
