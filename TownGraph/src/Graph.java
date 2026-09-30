import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Graph.java
 *
 * An undirected, weighted graph of {@link Town} vertices connected by
 * {@link Road} edges, stored as an adjacency list.
 *
 * Each town maps to the list of roads that touch it. Because the graph is
 * undirected, a road between A and B is stored in both A's list and B's list.
 *
 * Shortest paths are computed with Dijkstra's algorithm using a binary-heap
 * priority queue, which runs in O((V + E) log V) time.
 *
 * @author Armel Daryl Kelodjoue Nguetchouang
 */
public class Graph implements GraphInterface<Town, Road>
{
	/** Every town in the graph, mapped to the roads that touch it. */
	private final Map<Town, List<Road>> adjacencyList = new HashMap<>();

	/** Shortest known distance from the last Dijkstra source to each town. */
	private Map<Town, Integer> distances = new HashMap<>();

	/**
	 * The road used to reach each town on its shortest path from the last
	 * Dijkstra source. Storing the road (not just the previous town) means the
	 * path always reports the exact road Dijkstra chose, even when two towns
	 * are joined by more than one road.
	 */
	private Map<Town, Road> previousRoad = new HashMap<>();

	/** The source town used by the most recent Dijkstra run. */
	private Town lastSource;

	/** Creates an empty graph. */
	public Graph()
	{
	}

	// ------------------------------------------------------------------
	// Vertices
	// ------------------------------------------------------------------

	/**
	 * Adds a town to the graph if it is not already present.
	 *
	 * @param v the town to add
	 * @return true if the town was added, false if it was already in the graph
	 * @throws NullPointerException if the town is null
	 */
	@Override
	public boolean addVertex(Town v)
	{
		if (v == null)
			throw new NullPointerException("Vertex cannot be null");
		if (adjacencyList.containsKey(v))
			return false;

		adjacencyList.put(v, new ArrayList<>());
		invalidatePaths();
		return true;
	}

	/**
	 * @param v the town to look for
	 * @return true if the town is in the graph; false if not or if v is null
	 */
	@Override
	public boolean containsVertex(Town v)
	{
		return v != null && adjacencyList.containsKey(v);
	}

	/**
	 * Removes a town and every road that touches it.
	 *
	 * @param v the town to remove
	 * @return true if the town was in the graph, false otherwise (or if null)
	 */
	@Override
	public boolean removeVertex(Town v)
	{
		if (!containsVertex(v))
			return false;

		// Remove each touching road from the neighbor's list as well.
		for (Road road : adjacencyList.get(v))
		{
			Town neighbor = road.getOtherTown(v);
			if (!neighbor.equals(v))
				adjacencyList.get(neighbor).remove(road);
		}
		adjacencyList.remove(v);
		invalidatePaths();
		return true;
	}

	/**
	 * @return a set view of all towns in the graph (backed by the graph)
	 */
	@Override
	public Set<Town> vertexSet()
	{
		return adjacencyList.keySet();
	}

	// ------------------------------------------------------------------
	// Edges
	// ------------------------------------------------------------------

	/**
	 * Creates a road between two towns that are already in the graph.
	 *
	 * @param sourceVertex      one end of the road
	 * @param destinationVertex the other end of the road
	 * @param weight            length of the road in miles
	 * @param description       name of the road
	 * @return the newly created road
	 * @throws NullPointerException     if either town is null
	 * @throws IllegalArgumentException if either town is not in the graph
	 */
	@Override
	public Road addEdge(Town sourceVertex, Town destinationVertex, int weight, String description)
	{
		if (sourceVertex == null || destinationVertex == null)
			throw new NullPointerException("Vertices cannot be null");
		if (!adjacencyList.containsKey(sourceVertex) || !adjacencyList.containsKey(destinationVertex))
			throw new IllegalArgumentException("Both vertices must be in the graph");

		Road road = new Road(sourceVertex, destinationVertex, weight, description);
		adjacencyList.get(sourceVertex).add(road);
		if (!sourceVertex.equals(destinationVertex))  // don't store a self-loop twice
			adjacencyList.get(destinationVertex).add(road);
		invalidatePaths();
		return road;
	}

	/**
	 * Returns a road connecting the two towns (in either direction).
	 *
	 * @param sourceVertex      one end of the road
	 * @param destinationVertex the other end of the road
	 * @return the connecting road, or null if there is none or a town is
	 *         null / not in the graph
	 */
	@Override
	public Road getEdge(Town sourceVertex, Town destinationVertex)
	{
		if (!containsVertex(sourceVertex) || destinationVertex == null)
			return null;

		for (Road road : adjacencyList.get(sourceVertex))
		{
			if (road.contains(destinationVertex))
				return road;
		}
		return null;
	}

	/**
	 * @return true if a road connects the two towns (in either direction)
	 */
	@Override
	public boolean containsEdge(Town sourceVertex, Town destinationVertex)
	{
		return getEdge(sourceVertex, destinationVertex) != null;
	}

	/**
	 * @return every road in the graph (each road appears once)
	 */
	@Override
	public Set<Road> edgeSet()
	{
		Set<Road> edges = new HashSet<>();
		for (List<Road> roads : adjacencyList.values())
			edges.addAll(roads);
		return edges;
	}

	/**
	 * @param vertex the town whose roads are wanted
	 * @return every road touching the town (empty if it has none)
	 * @throws NullPointerException     if the town is null
	 * @throws IllegalArgumentException if the town is not in the graph
	 */
	@Override
	public Set<Road> edgesOf(Town vertex)
	{
		if (vertex == null)
			throw new NullPointerException("Vertex is null");
		if (!adjacencyList.containsKey(vertex))
			throw new IllegalArgumentException("Vertex not found");

		return new HashSet<>(adjacencyList.get(vertex));
	}

	/**
	 * Removes a road between two towns.
	 * The weight is only checked if it is greater than -1, and the name is
	 * only checked if it is not null.
	 *
	 * @param sourceVertex      one end of the road
	 * @param destinationVertex the other end of the road
	 * @param weight            road length to match, or -1 to match any
	 * @param description       road name to match, or null to match any
	 * @return the removed road, or null if no matching road was found
	 */
	@Override
	public Road removeEdge(Town sourceVertex, Town destinationVertex, int weight, String description)
	{
		if (!containsVertex(sourceVertex) || !containsVertex(destinationVertex))
			return null;

		for (Road road : adjacencyList.get(sourceVertex))
		{
			boolean sameTowns  = road.contains(destinationVertex);
			boolean sameWeight = weight <= -1 || road.getWeight() == weight;
			boolean sameName   = description == null || road.getName().equals(description);

			if (sameTowns && sameWeight && sameName)
			{
				adjacencyList.get(sourceVertex).remove(road);
				adjacencyList.get(destinationVertex).remove(road);
				invalidatePaths();
				return road;
			}
		}
		return null;
	}

	// ------------------------------------------------------------------
	// Shortest path (Dijkstra)
	// ------------------------------------------------------------------

	/**
	 * Finds the shortest route between two towns.
	 * Each entry has the form "Town_A via Road_X to Town_B 5 mi".
	 *
	 * @param sourceVertex      starting town
	 * @param destinationVertex ending town
	 * @return the steps of the route in order; an empty list if there is no
	 *         route, a town is not in the graph, or both towns are the same
	 */
	@Override
	public ArrayList<String> shortestPath(Town sourceVertex, Town destinationVertex)
	{
		ArrayList<String> path = new ArrayList<>();
		if (!containsVertex(sourceVertex) || !containsVertex(destinationVertex))
			return path;

		// Reuse the previous Dijkstra run if the graph and source haven't changed.
		if (!sourceVertex.equals(lastSource))
			dijkstraShortestPath(sourceVertex);

		if (distances.get(destinationVertex) == Integer.MAX_VALUE)
			return path;   // unreachable

		// Walk backwards from the destination, following the road used to
		// reach each town, then prepend so the list reads start -> finish.
		Town step = destinationVertex;
		while (!step.equals(sourceVertex))
		{
			Road road = previousRoad.get(step);
			Town prev = road.getOtherTown(step);
			path.add(prev.getName() + " via " + road.getName() + " to "
					+ step.getName() + " " + road.getWeight() + " mi");
			step = prev;
		}
		Collections.reverse(path);
		return path;
	}

	/**
	 * Runs Dijkstra's algorithm from the given town, filling in the shortest
	 * distance to every town and the road used to reach it.
	 *
	 * Uses "lazy deletion": instead of removing and re-adding a town in the
	 * priority queue when its distance improves (O(n) per update), a new
	 * entry is added and stale entries are skipped when polled.
	 *
	 * @param sourceVertex the town to measure distances from
	 */
	@Override
	public void dijkstraShortestPath(Town sourceVertex)
	{
		distances = new HashMap<>();
		previousRoad = new HashMap<>();
		lastSource = null;
		if (!containsVertex(sourceVertex))
			return;

		for (Town town : adjacencyList.keySet())
			distances.put(town, Integer.MAX_VALUE);
		distances.put(sourceVertex, 0);

		// Queue entries are {town, distance when queued}, ordered by distance.
		PriorityQueue<QueueEntry> queue = new PriorityQueue<>();
		queue.add(new QueueEntry(sourceVertex, 0));
		Set<Town> visited = new HashSet<>();

		while (!queue.isEmpty())
		{
			QueueEntry entry = queue.poll();
			Town current = entry.town;
			if (!visited.add(current))
				continue;   // stale entry: a shorter distance was already processed

			for (Road road : adjacencyList.get(current))
			{
				Town neighbor = road.getOtherTown(current);
				int newDist = entry.distance + road.getWeight();

				if (newDist < distances.get(neighbor))
				{
					distances.put(neighbor, newDist);
					previousRoad.put(neighbor, road);
					queue.add(new QueueEntry(neighbor, newDist));
				}
			}
		}
		lastSource = sourceVertex;
	}

	/**
	 * Returns the total length in miles of the shortest route between two
	 * towns.
	 *
	 * @param sourceVertex      starting town
	 * @param destinationVertex ending town
	 * @return total miles, or -1 if there is no route
	 */
	public int shortestDistance(Town sourceVertex, Town destinationVertex)
	{
		if (!containsVertex(sourceVertex) || !containsVertex(destinationVertex))
			return -1;
		if (!sourceVertex.equals(lastSource))
			dijkstraShortestPath(sourceVertex);

		int dist = distances.get(destinationVertex);
		return dist == Integer.MAX_VALUE ? -1 : dist;
	}

	/** Forgets the cached Dijkstra results; called whenever the graph changes. */
	private void invalidatePaths()
	{
		lastSource = null;
	}

	/** A town waiting in Dijkstra's priority queue, with its distance at the time. */
	private static class QueueEntry implements Comparable<QueueEntry>
	{
		final Town town;
		final int distance;

		QueueEntry(Town town, int distance)
		{
			this.town = town;
			this.distance = distance;
		}

		@Override
		public int compareTo(QueueEntry other)
		{
			return Integer.compare(distance, other.distance);
		}
	}
}
