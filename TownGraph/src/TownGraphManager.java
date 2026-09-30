import java.util.*;
import java.io.*;

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

public class TownGraphManager implements TownGraphManagerInterface
{
	private Graph graph;
	
	/**
	 * Default no-arg constructor initializes graph to represent town map
	 */
	public TownGraphManager()
	{
		graph = new Graph();
	}
	
	/**
	 * Adds a road with 2 towns and a road name
	 * @param town1 name of town 1 (lastname, firstname)
	 * @param town2 name of town 2 (lastname, firstname)
	 * @param roadName name of road
	 * @return true if the road was added successfully
	 */
	@Override
	public boolean addRoad(String town1, String town2, int weight, String roadName) 
	{
		Town t1a = getTown(town1);
		Town t2a = getTown(town2);
		graph.addEdge(t1a, t2a, weight, roadName);
		return true;
	}

	/**
	 * Returns the name of the road that both towns are connected through
	 * @param town1 name of town 1 (lastname, firstname)
	 * @param town2 name of town 2 (lastname, firstname)
	 * @return name of road if town 1 and town2 are in the same road, returns null if not
	 */
	@Override
	public String getRoad(String town1, String town2) 
	{
		Town t1a = getTown(town1);
		Town t2a = getTown(town2);
		Road road = graph.getEdge(t1a, t2a);
		for (Road r : graph.edgesOf(t1a))
		{
			if (r.getDestination().equals(t2a) || r.getSource().equals(t2a))
				return road.getName();
		}
		return null;
	}

	/**
	 * Adds a town to the graph
	 * @param v the town's name  (lastname, firstname)
	 * @return true if the town was successfully added, false if not
	 */
	@Override
	public boolean addTown(String v) 
	{
		Town t1a = new Town(v);
		return graph.addVertex(t1a);
	}

	/**
	 * Gets a town with a given name
	 * @param name the town's name 
	 * @return the Town specified by the name, or null if town does not exist
	 */
	
	public Town getTown(String name) 
	{
		Town t1a = new Town(name);
		for (Town town : graph.vertexSet())
		{
			if (t1a.equals(town))
			{
				return town;
			}
		}
		return null;
	}

	
	/**
	 * Determines if a town is already in the graph
	 * @param v the town's name 
	 * @return true if the town is in the graph, false if not
	 */
	@Override
	public boolean containsTown(String v) 
	{
		Town t1a = new Town(v);
		return graph.containsVertex(t1a);
	}

	/**
	 * Determines if a road is in the graph
	 * @param town1 name of town 1 (lastname, firstname)
	 * @param town2 name of town 2 (lastname, firstname)
	 * @return true if the road is in the graph, false if not
	 */
	@Override
	public boolean containsRoadConnection(String town1, String town2) 
	{
		Town t1a = getTown(town1);
		Town t2a = getTown(town2);
		return graph.containsEdge(t1a, t2a);
	}

	/**
	 * Creates an arraylist of all road titles in sorted order by road name
	 * @return an arraylist of all road titles in sorted order by road name
	 */
	@Override
	public ArrayList<String> allRoads() 
	{
		ArrayList<String> roads = new ArrayList<>();
		for (Road road : graph.edgeSet())
		{
			roads.add(road.getName());
		}
		Collections.sort(roads);
		return roads;
	}

	/**
	 * Deletes a road from the graph
	 * @param town1 name of town 1 (lastname, firstname)
	 * @param town2 name of town 2 (lastname, firstname)
	 * @param roadName the road name
	 * @return true if the road was successfully deleted, false if not
	 */
	@Override
	public boolean deleteRoadConnection(String town1, String town2, String road) 
	{
		Town t1a = getTown(town1);
		Town t2a = getTown(town2);
		
		if (graph.containsEdge(t1a, t2a))
		{
			Road road1 = graph.getEdge(t1a, t2a);
			Road road2 = graph.removeEdge(t1a, t2a, road1.getWeight(), road);
			if (road2.equals(road1))
				return true;
		}
		return false;
	}

	/**
	 * Deletes a town from the graph
	 * @param v name of town (lastname, firstname)
	 * @return true if the town was successfully deleted, false if not
	 */
	@Override
	public boolean deleteTown(String v) 
	{
		Town town1a = new Town(v);
		return graph.removeVertex(town1a);
	}

	/**
	 * Creates an arraylist of all towns in alphabetical order (last name, first name)
	 * @return an arraylist of all towns in alphabetical order (last name, first name)
	 */
	@Override
	public ArrayList<String> allTowns() 
	{
		ArrayList<String> towns = new ArrayList<>();
		for (Town town : graph.vertexSet())
		{
			towns.add(town.getName());
		}
		Collections.sort(towns);
		return towns;
	}

	/**
	 * Returns the shortest path from town 1 to town 2
	 * @param town1 name of town 1 (lastname, firstname)
	 * @param town2 name of town 2 (lastname, firstname)
	 * @return an Arraylist of roads connecting the two towns together, null if the
	 * towns have no path to connect them.
	 */
	@Override
	public ArrayList<String> getPath(String town1, String town2) 
	{
		Town t1a = getTown(town1);
		Town t2a = getTown(town2);
		ArrayList<String> path = graph.shortestPath(t1a, t2a);
		return path;
	}
	
	/**
	 * Method to populate the town graph
	 * @param input - File that contains the towns
	 * @throws FileNotFoundException if the file is NOT found
	 */
	public void populateTownGraph(File input) throws FileNotFoundException
	{
		ArrayList<String> record = new ArrayList<>();
		if (input == null || !input.exists())
		{
			throw new FileNotFoundException();
		}
		
		Scanner scanner = new Scanner(input);
		while (scanner.hasNextLine())
		{
			record.add(scanner.nextLine());
		}
		for (String line : record)
		{
			String[] split = line.split(";");
			int deliminator = split[0].indexOf(",");
			String rName = split[0].substring(0, deliminator);
			String weight = split[0].substring(deliminator+1,split[0].length());
			String source = split[1];
			String destination = split[2];
			
			addTown(source);
			addTown(destination);
			addRoad(source, destination, Integer.parseInt(weight), rName);
		}
		scanner.close();
	}
}