import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * TownGraphManager.java
 *
 * A simple, string-based front end for the {@link Graph}. The GUI and other
 * callers work with plain town and road names; this class turns those names
 * into {@link Town} and {@link Road} objects and forwards the work to the
 * graph.
 *
 * It can also load a whole road network from a text file where each line has
 * the form:
 * <pre>
 *     RoadName,Miles;TownA;TownB
 *     e.g.  I270-N,14;Frederick;Clarksburg
 * </pre>
 *
 * @author Armel Daryl Kelodjoue Nguetchouang
 */
public class TownGraphManager implements TownGraphManagerInterface
{
	/** The underlying graph of towns and roads. */
	private final Graph graph;

	/** Creates a manager with an empty road network. */
	public TownGraphManager()
	{
		graph = new Graph();
	}

	// ------------------------------------------------------------------
	// Towns
	// ------------------------------------------------------------------

	/**
	 * Adds a town to the network.
	 *
	 * @param v the town's name
	 * @return true if the town was added, false if it already exists or the
	 *         name is blank
	 */
	@Override
	public boolean addTown(String v)
	{
		if (v == null || v.isBlank())
			return false;
		return graph.addVertex(new Town(v.trim()));
	}

	/**
	 * Looks up a town by name.
	 *
	 * @param name the town's name
	 * @return the matching Town, or null if no town has that name
	 */
	@Override
	public Town getTown(String name)
	{
		if (name == null)
			return null;
		Town town = new Town(name.trim());
		return graph.containsVertex(town) ? town : null;
	}

	/**
	 * @param v the town's name
	 * @return true if a town with that name exists
	 */
	@Override
	public boolean containsTown(String v)
	{
		return getTown(v) != null;
	}

	/**
	 * Removes a town and every road connected to it.
	 *
	 * @param v the town's name
	 * @return true if the town existed and was removed
	 */
	@Override
	public boolean deleteTown(String v)
	{
		return graph.removeVertex(getTown(v));
	}

	/**
	 * @return the names of all towns, sorted alphabetically
	 */
	@Override
	public ArrayList<String> allTowns()
	{
		ArrayList<String> towns = new ArrayList<>();
		for (Town town : graph.vertexSet())
			towns.add(town.getName());
		Collections.sort(towns);
		return towns;
	}

	// ------------------------------------------------------------------
	// Roads
	// ------------------------------------------------------------------

	/**
	 * Adds a road between two existing towns.
	 *
	 * @param town1    name of one town
	 * @param town2    name of the other town
	 * @param weight   length of the road in miles (must not be negative)
	 * @param roadName name of the road
	 * @return true if the road was added, false if a town doesn't exist,
	 *         the distance is negative, or the name is blank
	 */
	@Override
	public boolean addRoad(String town1, String town2, int weight, String roadName)
	{
		Town t1 = getTown(town1);
		Town t2 = getTown(town2);
		if (t1 == null || t2 == null || weight < 0 || roadName == null || roadName.isBlank())
			return false;

		graph.addEdge(t1, t2, weight, roadName.trim());
		return true;
	}

	/**
	 * Returns the name of a road that connects two towns.
	 *
	 * @param town1 name of one town
	 * @param town2 name of the other town
	 * @return the road's name, or null if the towns are not directly connected
	 */
	@Override
	public String getRoad(String town1, String town2)
	{
		Road road = graph.getEdge(getTown(town1), getTown(town2));
		return road == null ? null : road.getName();
	}

	/**
	 * @param town1 name of one town
	 * @param town2 name of the other town
	 * @return true if a road directly connects the two towns
	 */
	@Override
	public boolean containsRoadConnection(String town1, String town2)
	{
		return graph.containsEdge(getTown(town1), getTown(town2));
	}

	/**
	 * Removes the named road between two towns.
	 *
	 * @param town1 name of one town
	 * @param town2 name of the other town
	 * @param road  name of the road to remove
	 * @return true if the road existed and was removed
	 */
	@Override
	public boolean deleteRoadConnection(String town1, String town2, String road)
	{
		// weight -1 means "any distance"; only the towns and road name must match
		return graph.removeEdge(getTown(town1), getTown(town2), -1, road) != null;
	}

	/**
	 * @return the names of all roads, sorted alphabetically
	 */
	@Override
	public ArrayList<String> allRoads()
	{
		ArrayList<String> roads = new ArrayList<>();
		for (Road road : graph.edgeSet())
			roads.add(road.getName());
		Collections.sort(roads);
		return roads;
	}

	// ------------------------------------------------------------------
	// Shortest path
	// ------------------------------------------------------------------

	/**
	 * Finds the shortest route between two towns.
	 * Each step reads like "Rockville via MD189 to Potomac 6 mi".
	 *
	 * @param town1 name of the starting town
	 * @param town2 name of the destination town
	 * @return the steps of the route, or an empty list if there is no route
	 */
	@Override
	public ArrayList<String> getPath(String town1, String town2)
	{
		return graph.shortestPath(getTown(town1), getTown(town2));
	}

	/**
	 * Returns the total length of the shortest route between two towns.
	 *
	 * @param town1 name of the starting town
	 * @param town2 name of the destination town
	 * @return total miles, or -1 if there is no route
	 */
	public int getPathDistance(String town1, String town2)
	{
		return graph.shortestDistance(getTown(town1), getTown(town2));
	}

	// ------------------------------------------------------------------
	// File loading
	// ------------------------------------------------------------------

	/**
	 * Loads towns and roads from a text file. Each non-blank line must have
	 * the form {@code RoadName,Miles;TownA;TownB}. Towns are created
	 * automatically the first time they appear.
	 *
	 * @param input the file to read
	 * @throws FileNotFoundException    if the file does not exist
	 * @throws IllegalArgumentException if a line is not in the expected format
	 *                                  (the message gives the line number)
	 */
	public void populateTownGraph(File input) throws FileNotFoundException
	{
		if (input == null || !input.exists())
			throw new FileNotFoundException(input == null ? "No file given" : input.getPath());

		// try-with-resources closes the file even if a line is bad
		try (Scanner scanner = new Scanner(input))
		{
			int lineNumber = 0;
			while (scanner.hasNextLine())
			{
				String line = scanner.nextLine().trim();
				lineNumber++;
				if (line.isEmpty())
					continue;   // skip blank lines

				parseLine(line, lineNumber);
			}
		}
	}

	/**
	 * Parses one line of a road file and adds its towns and road.
	 *
	 * @param line       the text of the line, e.g. "MD97,8;Rockville;Olney"
	 * @param lineNumber the line's position in the file (for error messages)
	 */
	private void parseLine(String line, int lineNumber)
	{
		String[] parts = line.split(";");
		int comma = parts[0].lastIndexOf(',');
		if (parts.length != 3 || comma < 0)
			throw new IllegalArgumentException("Line " + lineNumber
					+ ": expected RoadName,Miles;TownA;TownB but got \"" + line + "\"");

		String roadName = parts[0].substring(0, comma).trim();
		String source = parts[1].trim();
		String destination = parts[2].trim();
		int miles;
		try
		{
			miles = Integer.parseInt(parts[0].substring(comma + 1).trim());
		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException("Line " + lineNumber
					+ ": distance must be a whole number in \"" + line + "\"");
		}

		addTown(source);
		addTown(destination);
		addRoad(source, destination, miles, roadName);
	}
}
