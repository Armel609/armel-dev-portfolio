import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

/**
 * ConsoleDriver.java
 *
 * A text-only version of the TownGraph app that runs with plain Java (no
 * JavaFX needed). It loads a road file, lists the towns, and prints the
 * shortest route between any two towns the user types.
 *
 * Usage:
 * <pre>
 *     java ConsoleDriver                    (loads "MD Towns.txt")
 *     java ConsoleDriver "US Towns.txt"     (loads another road file)
 * </pre>
 *
 * @author Armel Daryl Kelodjoue Nguetchouang
 */
public class ConsoleDriver
{
	/**
	 * Runs the interactive route finder.
	 * @param args optional path to a road file
	 */
	public static void main(String[] args)
	{
		String fileName = args.length > 0 ? args[0] : "MD Towns.txt";
		TownGraphManager manager = new TownGraphManager();

		try
		{
			manager.populateTownGraph(new File(fileName));
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Could not find \"" + fileName + "\". Run this from the src folder,"
					+ " or pass the path to a road file.");
			return;
		}
		catch (IllegalArgumentException e)
		{
			System.out.println("Could not read \"" + fileName + "\": " + e.getMessage());
			return;
		}

		List<String> towns = manager.allTowns();
		System.out.println("Loaded " + towns.size() + " towns and "
				+ manager.allRoads().size() + " roads from " + fileName);
		System.out.println("Towns: " + String.join(", ", towns));

		try (Scanner in = new Scanner(System.in))
		{
			while (true)
			{
				System.out.print("\nStarting town (or press Enter to quit): ");
				if (!in.hasNextLine()) break;
				String from = in.nextLine().trim();
				if (from.isEmpty()) break;

				System.out.print("Destination town: ");
				if (!in.hasNextLine()) break;
				String to = in.nextLine().trim();

				printRoute(manager, from, to);
			}
		}
		System.out.println("Goodbye!");
	}

	/** Prints the shortest route between two towns, or why there isn't one. */
	private static void printRoute(TownGraphManager manager, String from, String to)
	{
		if (!manager.containsTown(from))
		{
			System.out.println("Unknown town: " + from);
			return;
		}
		if (!manager.containsTown(to))
		{
			System.out.println("Unknown town: " + to);
			return;
		}
		if (from.equals(to))
		{
			System.out.println("You are already there!");
			return;
		}

		List<String> path = manager.getPath(from, to);
		if (path.isEmpty())
		{
			System.out.println("You can't get there from here.");
			return;
		}

		System.out.println("Shortest route:");
		for (int i = 0; i < path.size(); i++)
			System.out.println("  " + (i + 1) + ". " + path.get(i));
		System.out.println("Total distance: " + manager.getPathDistance(from, to) + " mi");
	}
}
