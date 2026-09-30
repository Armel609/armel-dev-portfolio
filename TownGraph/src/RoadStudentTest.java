import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * RoadStudentTest.java
 *
 * Tests for the {@link Road} class, plus graph behavior that depends on
 * roads (parallel roads and invalid input).
 *
 * @author Armel Daryl Kelodjoue Nguetchouang
 */
class RoadStudentTest {

	private Town a, b, c;
	private Road ab;

	@BeforeEach
	void setUp() {
		a = new Town("A");
		b = new Town("B");
		c = new Town("C");
		ab = new Road(a, b, 5, "Main St");
	}

	@Test
	void roadIsUndirected() {
		Road ba = new Road(b, a, 5, "Main St");
		assertEquals(ab, ba);
		assertEquals(ab.hashCode(), ba.hashCode());
		assertEquals(0, ab.compareTo(ba));
	}

	@Test
	void differentNameOrWeightMeansDifferentRoad() {
		assertNotEquals(ab, new Road(a, b, 6, "Main St"));
		assertNotEquals(ab, new Road(a, b, 5, "Oak Ave"));
		assertNotEquals(ab, new Road(a, c, 5, "Main St"));
	}

	@Test
	void containsAndGetOtherTown() {
		assertTrue(ab.contains(a));
		assertTrue(ab.contains(b));
		assertFalse(ab.contains(c));
		assertEquals(b, ab.getOtherTown(a));
		assertEquals(a, ab.getOtherTown(b));
		assertNull(ab.getOtherTown(c));
	}

	@Test
	void copyConstructorMakesEqualRoad() {
		assertEquals(ab, new Road(ab));
	}

	@Test
	void shortestPathUsesTheShorterOfTwoParallelRoads() {
		Graph g = new Graph();
		g.addVertex(a);
		g.addVertex(b);
		g.addEdge(a, b, 10, "Long Way");
		g.addEdge(a, b, 3, "Short Cut");

		assertEquals("A via Short Cut to B 3 mi", g.shortestPath(a, b).get(0));
		assertEquals(3, g.shortestDistance(a, b));
	}

	@Test
	void graphHandlesUnknownOrNullTownsSafely() {
		Graph g = new Graph();
		g.addVertex(a);
		assertNull(g.getEdge(a, c));
		assertNull(g.getEdge(null, a));
		assertFalse(g.containsEdge(c, a));
		assertTrue(g.shortestPath(a, c).isEmpty());
		assertNull(g.removeEdge(a, c, 1, "X"));
		assertFalse(g.removeVertex(null));
	}

	@Test
	void pathUpdatesAfterRoadIsRemoved() {
		Graph g = new Graph();
		g.addVertex(a);
		g.addVertex(b);
		g.addVertex(c);
		g.addEdge(a, b, 1, "AB");
		g.addEdge(b, c, 1, "BC");
		g.addEdge(a, c, 5, "AC");

		assertEquals(2, g.shortestPath(a, c).size());
		g.removeEdge(a, b, 1, "AB");
		assertEquals("A via AC to C 5 mi", g.shortestPath(a, c).get(0));
	}

	@Test
	void managerRejectsBadRoads() {
		TownGraphManager m = new TownGraphManager();
		m.addTown("A");
		m.addTown("B");
		assertFalse(m.addRoad("A", "Nowhere", 3, "R"));
		assertFalse(m.addRoad("A", "B", -1, "R"));
		assertFalse(m.addRoad("A", "B", 3, " "));
		assertTrue(m.addRoad("A", "B", 3, "R"));
		assertNull(m.getRoad("A", "Nowhere"));
		assertFalse(m.deleteRoadConnection("A", "B", "Wrong Name"));
		assertTrue(m.deleteRoadConnection("A", "B", "R"));
	}
}
