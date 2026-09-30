import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * TownStudentTest.java
 *
 * JUnit tests for the Town class and basic Graph operations on towns.
 *
 * @author Armel Daryl Kelodjoue Nguetchouang
 */
class TownStudentTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	 @Test
	    public void testAddVertex() {
	        Graph g = new Graph();
	        Town t = new Town("Douala");

	        assertTrue(g.addVertex(t));
	    }

	    @Test
	    public void testAddEdge() {
	        Graph g = new Graph();

	        Town t1 = new Town("Douala");
	        Town t2 = new Town("Yaounde");

	        g.addVertex(t1);
	        g.addVertex(t2);

	        assertNotNull(g.addEdge(t1, t2, 5, "Road1"));
	    }

	    @Test
	    public void testContainsVertex() {
	        Graph g = new Graph();
	        Town t = new Town("Douala");

	        g.addVertex(t);
	        assertTrue(g.containsVertex(t));
	    }

}
