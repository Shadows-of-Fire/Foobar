import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * @author Brennan Ward
 * 
 * The escape pods problem states that given a set of entrances and exits, and the path, we need to find the max throughput.
 * This is an example of a Maximum Flow Problem.  However, the sources and the sink are broken up, so they need to be merged.
 * We also need to actually make the path into a graph for use.
 * 
 * Once we have the graph, we can use the Ford-Fulkerson method via the Edmonds-Karp algorithm.
 * We also have the option of Dinic's algorithm, but that is harder to implement.
 */
public class EscapePods {

	public static int solution(int[] entrances, int[] exits, int[][] path) {
		Room[] graph = buildGraph(toList(entrances), toList(exits), path);
		Connection[] augPath;
		int flow = 0;
		while ((augPath = findPath(graph)) != null) {
			Connection c = augPath[graph.length - 1];
			int flowConsumed = 1000000;
			while (c != null) {
				flowConsumed = Math.min(c.capacity - c.flow, flowConsumed);
				c = augPath[c.src];
			}
			c = augPath[graph.length - 1];
			while (c != null) {
				c.addFlow(flowConsumed);
				c = augPath[c.src];
			}
			flow += flowConsumed;
		}
		return flow;
	}

	/**
	 * Creates the undirected graph given the input data.
	 * Creates a supersource and supersink that each have infinite (max int) flow to the true entrances/exits.
	 * That is, the supersource has infinite flow to each entrance,
	 * and the supersink has infinite flow from each exit.
	 */
	public static Room[] buildGraph(List<Integer> entrances, List<Integer> exits, int[][] path) {
		Room[] rooms = new Room[path.length + 2];
		for (int i = 0; i < rooms.length; i++) {
			rooms[i] = new Room(i);
		}

		for (int i : entrances) { //Connect from Supersource to "real" entrances.
			makeConnection(rooms, rooms[0], i + 1, 1000000);
		}

		for (int i = 0; i < path.length; i++) {
			Room room = rooms[i + 1];
			int[] connections = path[i];
			for (int k = 0; k < connections.length; k++) {
				int dest = k + 1;
				int capacity = connections[k];
				if (capacity <= 0) continue;
				makeConnection(rooms, room, dest, capacity);
			}
		}

		for (int i : exits) { //Connect from "real" exits to Supersink.
			makeConnection(rooms, rooms[i + 1], rooms.length - 1, 1000000);
		}

		return rooms;
	}

	/**
	 * Creates a connection between src and dest.
	 * Also handles creation of the reverse connection (with capacity zero),
	 * and the link of the reverse and forward connections.
	 */
	public static void makeConnection(Room[] rooms, Room src, int dest, int capacity) {
		Connection forward = new Connection(src.id, dest, capacity);
		Connection rev = new Connection(dest, src.id, 0);
		forward.reverse = rev;
		rev.reverse = forward;
		src.connections.add(forward);
		rooms[dest].connections.add(rev);
	}

	/**
	 * BFS method to find an augmenting path in the residual graph.
	 * The main graph is actually the same thing as the residual graph,
	 * as all connections hold their current flow.
	 * @return The augmenting path, as an array of edges.
	 */
	public static Connection[] findPath(Room[] graph) {
		Queue<Room> queue = new LinkedList<>();
		Connection[] path = new Connection[graph.length]; //Path array, which holds the backtrack of the paths.
		queue.add(graph[0]);

		while (!queue.isEmpty()) {
			Room room = queue.poll();
			for (Connection c : room.connections) {
				Room n = graph[c.dest];
				if (n.id != 0 && path[n.id] == null && c.capacity - c.flow > 0) {
					path[n.id] = c;
					queue.add(n);
					if (n.id == graph.length - 1) return path; //We've found the sink, exit.
				}
			}
		}
		return null;
	}

	static List<Integer> toList(int[] arr) {
		return Arrays.stream(arr).boxed().collect(Collectors.toList());
	}

	public static class Room {
		final int id;
		int level = Integer.MAX_VALUE;
		List<Connection> connections = new ArrayList<>();

		public Room(int id) {
			this.id = id;
		}
	}

	public static class Connection {
		final int src, dest, capacity;
		int flow = 0;
		Connection reverse;

		public Connection(int src, int dest, int capacity) {
			this.src = src;
			this.dest = dest;
			this.capacity = capacity;
		}

		public void addFlow(int flow) {
			if (this.flow + flow > capacity) throw new RuntimeException("Connection Flow Exceeded!");
			this.flow += flow;
			this.reverse.flow -= flow;
		}
	}

}
