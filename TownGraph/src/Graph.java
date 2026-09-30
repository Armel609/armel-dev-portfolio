/**
 * Class: CMSC204 
 * Instructor: Gary Thai
 * Description: 
 * This project implements a graph-based application that models a network
 * of towns and roads. The system uses a Graph data structure to represent
 * towns as vertices and roads as edges connecting them.
 * 
 * The Town class represents each location in the network and implements
 * Comparable to allow sorting and comparison based on town names.
 * 
 * The Road class represents connections between towns, storing the
 * distance and name of each road. It also implements Comparable and
 * treats roads as undirected edges.
 * 
 * The Graph class implements the GraphInterface and manages the overall
 * structure using vertices and edges. It supports operations such as
 * adding towns and roads, checking connections, and computing the
 * shortest path between towns using Dijkstra’s Shortest Path algorithm.
 * 
 * The TownGraphManager class serves as a higher-level interface to the
 * graph, allowing users to add towns and roads, read data from files,
 * and find the shortest path between two towns.
 * 
 * The application also includes JUnit test classes to verify the
 * functionality of all components and ensure correctness of the
 * implementation.
 * 
 * Due: 5/3/2026
 * Platform/compiler: Eclipse / javac
 * 
 * I pledge that I have completed the programming assignment 
 * independently. I have not copied code from any student or 
 * external source, nor have I shared my code with others.
 *
 * Name: Armel Daryl Kelodjoue Nguetchouang
 */
import java.util.*;

public class Graph implements GraphInterface<Town, Road>
{
	private Map<Town, List<Road>> adjacencyList;
	private Map<Town, Integer> distances;
	private Map<Town, Town> predecessors;
	
	/**
	 * Constructor
	 */
	public Graph()
	{
		adjacencyList = new HashMap<>();
	}

	
	@Override
	public Road getEdge(Town sourceVertex, Town destinationVertex) 
	{
		for (Road road : adjacencyList.get(sourceVertex))
		{
			if (road.getSource().equals(destinationVertex) || road.getDestination().equals(destinationVertex))
			{
				return road;
			}
		}
		return null;
	}

	/**
	 * Creates a new edge in this graph, going from the source vertex to the
     * target vertex, and returns the created edge. 
     * The source and target vertices must already be contained in this
     * graph. If they are not found in graph IllegalArgumentException is
     * thrown.
     * @param sourceVertex source vertex of the edge.
     * @param destinationVertex target vertex of the edge.
     * @param weight weight of the edge
     * @param description description for edge
     * @return The newly created edge if added to the graph, otherwise null.
     * @throws IllegalArgumentException if source or target vertices are not
     * found in the graph.
     * @throws NullPointerException if any of the specified vertices is null.
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
		adjacencyList.get(destinationVertex).add(road);
		return road;
	}

	/**
     * Adds the specified vertex to this graph if not already present. More
     * formally, adds the specified vertex, v, to this graph if
     * this graph contains no vertex u such that
     * u.equals(v). If this graph already contains such vertex, the call
     * leaves this graph unchanged and returns false. In combination
     * with the restriction on constructors, this ensures that graphs never
     * contain duplicate vertices.
     * @param v vertex to be added to this graph.
     * @return true if this graph did not already contain the specified
     * vertex.
     * @throws NullPointerException if the specified vertex is null.
     */
	@Override
	public boolean addVertex(Town v) 
	{
		if (v == null)
			throw new NullPointerException("Vertex cannot be null");
		if (adjacencyList.containsKey(v))
			return false;
		adjacencyList.put(v, new ArrayList<>());
		
		return true;
	}

	/**
     * Returns true if and only if this graph contains an edge going
     * from the source vertex to the target vertex. In undirected graphs the
     * same result is obtained when source and target are inverted. If any of
     * the specified vertices does not exist in the graph, or if is
     * null, returns false.
     * @param sourceVertex source vertex of the edge.
     * @param destinationVertex target vertex of the edge.
     * @return true if this graph contains the specified edge.
     */
	@Override
	public boolean containsEdge(Town sourceVertex, Town destinationVertex) 
	{
		return getEdge(sourceVertex, destinationVertex) != null;
	}

	
	@Override
	public boolean containsVertex(Town v) 
	{
		return adjacencyList.containsKey(v);
	}

	
	@Override
	public Set<Road> edgeSet() 
	{
		Set<Road> edges = new HashSet<>();
		for (List<Road> roads : adjacencyList.values())
		{
			edges.addAll(roads);
		}
		return edges;
	}

	
	@Override
	public Set<Road> edgesOf(Town vertex) 
	{
		if (vertex == null)
		{
			throw new NullPointerException("Vertex is null");
		}
		else if (!adjacencyList.containsKey(vertex))
		{
			throw new IllegalArgumentException("Vertex not found");
		}
		
		return new HashSet<>(adjacencyList.get(vertex));
	}

	@Override
	public Road removeEdge(Town sourceVertex, Town destinationVertex, int weight, String description) 
	{
		Road removeRoad = new Road(sourceVertex, destinationVertex, weight, description);
		List<Road> sourceEdges = adjacencyList.get(sourceVertex);
		List<Road> destinationEdges = adjacencyList.get(destinationVertex);
		
		if (sourceEdges != null && sourceEdges.remove(removeRoad))
		{
			destinationEdges.remove(removeRoad);
			return removeRoad;
		}
		return null;
	}

	
	@Override
	public boolean removeVertex(Town v) 
	{
		if (!adjacencyList.containsKey(v))
			return false;
		
		for (Road road : new ArrayList<>(adjacencyList.get(v)))
		{
			removeEdge(road.getSource(), road.getDestination(), road.getWeight(), road.getName());
		}
		adjacencyList.remove(v);
		return true;
	}

	
	@Override
	public Set<Town> vertexSet() 
	{
		return adjacencyList.keySet();
	}

	
	@Override
	public ArrayList<String> shortestPath(Town sourceVertex, Town destinationVertex) 
	{
		dijkstraShortestPath(sourceVertex);
		ArrayList<String> path = new ArrayList<>();
		Town step = destinationVertex;
		
		if (distances.get(step) == Integer.MAX_VALUE)
		{
			return path;
		}
		
		while (step != null && predecessors.get(step) != null)
		{
			Town prev = predecessors.get(step);
			Road road = getEdge(prev, step);
			
			if (road != null)
			{
				path.add(0, prev.getName() + " via " + road.getName() + " to " + step.getName() + " " + road.getWeight() + " mi");
			}
			step = prev;
		}
		return path;
	}

	
	@Override
	public void dijkstraShortestPath(Town sourceVertex) 
	{
		distances = new HashMap<>();
		predecessors = new HashMap<>();
		PriorityQueue<Town> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));
		
		for (Town town : adjacencyList.keySet())
		{
			distances.put(town, Integer.MAX_VALUE);
			predecessors.put(town, null);
		}
		distances.put(sourceVertex, 0);
		pq.add(sourceVertex);
		
		while(!pq.isEmpty())
		{
			Town current = pq.poll();
			
			for (Road road : adjacencyList.get(current))
			{
				Town neighbor = road.getSource().equals(current) ? road.getDestination() : road.getSource();

				int newDist = distances.get(current) + road.getWeight();				
				if (newDist < distances.get(neighbor))
				{
					distances.put(neighbor, newDist);
					predecessors.put(neighbor, current);
					pq.remove(neighbor);
					pq.add(neighbor);
				}
			}
		}
	}
}