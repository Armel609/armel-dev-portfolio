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
import java.util.Objects;

/**
 * Represents a town in a graph.
 * Implements Comparable<Town> so towns can be sorted alphabetically by name.
 */
public class Town implements Comparable<Town> {

    /** Name of the town */
    private String name;

    /**
     * Constructs a Town with the specified name.
     *
     * @param name the name of the town
     */
    public Town(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the town.
     *
     * @return town name
     */
    public String getName() {
        return name;
    }

    /**
     * Checks equality of this town with another object.
     * Towns are considered equal if their names are equal.
     *
     * @param o the object to compare
     * @return true if the object is a Town with the same name, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // same reference
        if (!(o instanceof Town)) return false; // not a Town
        Town town = (Town) o;
        return name.equals(town.name); // compare names
    }

    /**
     * Generates a hash code for this town.
     * Based solely on the town's name.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Compares this town to another town alphabetically by name.
     *
     * @param o the town to compare to
     * @return negative if this town comes before, positive if after, 0 if equal
     */
    @Override
    public int compareTo(Town o) {
        return this.name.compareTo(o.name);
    }

    /**
     * Returns a string representation of the town.
     *
     * @return the town's name
     */
    @Override
    public String toString() {
        return name;
    }
}
