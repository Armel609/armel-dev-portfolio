import java.util.Objects;

/**
 * Town.java
 *
 * A town (vertex) in the road-network graph. Two towns are equal when they
 * have the same name, and towns sort alphabetically by name.
 *
 * @author Armel Daryl Kelodjoue Nguetchouang
 */
public class Town implements Comparable<Town> {

    /** Name of the town */
    private final String name;

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
