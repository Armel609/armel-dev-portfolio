/**
 * Road.java
 *
 * A road (edge) connecting two towns in the undirected road-network graph.
 * A road from A to B is the same as a road from B to A. Roads sort by name,
 * then by distance, then by the towns they connect.
 *
 * @author Armel Daryl Kelodjoue Nguetchouang
 */
public class Road implements Comparable<Road> {

    /** One end of the road */
    private Town source;

    /** The other end of the road */
    private Town destination;

    /** Distance of the road in miles */
    private int weight;

    /** Name of the road */
    private String name;

    /**
     * Constructs a new Road between two towns with a given distance and name.
     *
     * @param source      the starting town
     * @param destination the ending town
     * @param weight      distance in miles
     * @param name        name of the road
     */
    public Road(Town source, Town destination, int weight, String name) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
        this.name = name;
    }

    /**
     * Copy constructor creates a new Road based on another road.
     *
     * @param templateRoad the road to copy
     */
    public Road(Road templateRoad) {
        this.source = templateRoad.source;
        this.destination = templateRoad.destination;
        this.weight = templateRoad.weight;
        this.name = templateRoad.name;
    }

    // --- Getters and Setters ---

    /** @return one end of the road */
    public Town getSource() {
        return source;
    }

    /** @param source the new first end of the road */
    public void setSource(Town source) {
        this.source = source;
    }

    /** @return the other end of the road */
    public Town getDestination() {
        return destination;
    }

    /** @param destination the new second end of the road */
    public void setDestination(Town destination) {
        this.destination = destination;
    }

    /** @return the road's distance in miles */
    public int getWeight() {
        return weight;
    }

    /**
     * Sets the weight (distance) of the road.
     *
     * @param weight the distance in miles
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Returns the name of the road.
     *
     * @return road name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the road.
     *
     * @param name new road name
     */
    public void setName(String name) {
        this.name = name;
    }

    // --- Utility Methods ---

    /**
     * Checks if this road connects to a specific town.
     *
     * @param town the town to check
     * @return true if the road connects to the town, false otherwise
     */
    public boolean contains(Town town) {
        return source.equals(town) || destination.equals(town);
    }

    /**
     * Returns the town on the other end of the road given one town.
     *
     * @param town one end of the road
     * @return the other town, or null if the given town is not connected
     */
    public Town getOtherTown(Town town) {
        if (source.equals(town)) {
            return destination;
        } else if (destination.equals(town)) {
            return source;
        }
        return null;
    }

    // --- Comparable Implementation ---

    /**
     * Compares this road to another road for sorting.
     * First compares by name, then by weight, and finally by connected towns.
     *
     * @param o the road to compare to
     * @return negative if less, positive if greater, 0 if equal
     */
    @Override
    public int compareTo(Road o) {
        if (o == null) return 1;

        // Compare by name
        int nameCompare = this.name.compareTo(o.name);
        if (nameCompare != 0) return nameCompare;

        // Compare by weight (distance)
        int weightCompare = Integer.compare(this.weight, o.weight);
        if (weightCompare != 0) return weightCompare;

        // Check if roads connect same towns (A-B equals B-A)
        boolean sameTowns = (this.source.equals(o.source) && this.destination.equals(o.destination)) ||
                            (this.source.equals(o.destination) && this.destination.equals(o.source));
        if (sameTowns) return 0;

        // Otherwise, compare by source then destination
        int sourceCompare = this.source.compareTo(o.source);
        if (sourceCompare != 0) return sourceCompare;

        return this.destination.compareTo(o.destination);
    }

    // --- Equals and HashCode ---

    /**
     * Checks equality of two roads.
     * Roads are equal if they connect the same towns (in either order), and have same weight and name.
     *
     * @param obj the object to compare
     * @return true if roads are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Road road = (Road) obj;

        boolean sameDirection = source.equals(road.source) && destination.equals(road.destination);
        boolean oppositeDirection = source.equals(road.destination) && destination.equals(road.source);

        return (sameDirection || oppositeDirection) &&
               weight == road.weight &&
               name.equals(road.name);
    }

    /**
     * Generates a hash code for the road.
     * Uses a commutative hash so A-B and B-A produce the same value.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        int sourceHash = source != null ? source.hashCode() : 0;
        int destHash = destination != null ? destination.hashCode() : 0;
        int minHash = Math.min(sourceHash, destHash);
        int maxHash = Math.max(sourceHash, destHash);
        return 31 * (31 * (31 * minHash + maxHash) + weight) + name.hashCode();
    }

    /**
     * Returns a string representation of the road.
     *
     * @return formatted string with road name, distance, and connected towns
     */
    @Override
    public String toString() {
        return name + " (" + weight + " miles): " + source + " to " + destination;
    }
}
