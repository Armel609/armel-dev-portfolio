# TownGraph: Shortest Route Finder

**Author:** Armel Daryl Kelodjoue Nguetchouang
**Language:** Java 17+ (JavaFX for the graphical version)
**Topics:** graphs, adjacency lists, Dijkstra's shortest-path algorithm, priority queues, JUnit testing

TownGraph models a road network as a **weighted, undirected graph**. Towns are the
vertices and roads are the edges, and each road's weight is its length in miles.
You can load a map from a text file or build one by hand, then ask for the
**shortest route** between any two towns. The app prints every road you'd take and
the total distance.

```
Shortest route:
  1. Frederick via I270-N to Clarksburg 14 mi
  2. Clarksburg via I270-NC to Germantown 5 mi
  3. Germantown via MD118W to Darnestown 6 mi
  4. Darnestown via MD190W to Potomac 8 mi
Total distance: 33 mi
```

It has two front ends:

| Version | Start class | Needs JavaFX? |
|---|---|---|
| **Graphical app** (add towns/roads, pick towns from drop-downs, find routes) | `DriverFX` | Yes |
| **Console app** (load a map file, type two towns, get the route) | `ConsoleDriver` | No |

---

## How to run it

### 1. Download the project

- **With Git:** `git clone https://github.com/Armel609/armel-dev-portfolio.git`, then go into `armel-dev-portfolio/TownGraph`.
- **Without Git:** on the [repository page](https://github.com/Armel609/armel-dev-portfolio), click **Code → Download ZIP**, unzip it, and open the `TownGraph` folder.

### 2. Install Java

You need a **JDK 17 or newer**, for example from [Adoptium](https://adoptium.net/).
To check that it's installed, open a terminal and run:

```
java -version
javac -version
```

### 3a. Run the console version (quickest, no JavaFX needed)

From the `TownGraph/src` folder:

```
javac Town.java Road.java GraphInterface.java Graph.java TownGraphManagerInterface.java TownGraphManager.java ConsoleDriver.java
java ConsoleDriver
```

This loads `MD Towns.txt` (towns in Montgomery County, Maryland). To load the
US cities map instead:

```
java ConsoleDriver "US Towns.txt"
```

Type a starting town and a destination town. Names are case-sensitive, so type them
as they appear in the list. Press **Enter** on an empty line to quit.

### 3b. Run the graphical (JavaFX) version

Since Java 11, JavaFX isn't included with the JDK, so you have to download it separately.

1. Download the **JavaFX SDK** for your operating system from [gluonhq.com/products/javafx](https://gluonhq.com/products/javafx/) and unzip it.
   Note the path to its `lib` folder, for example `C:\javafx-sdk-23\lib` or `/Users/you/javafx-sdk-23/lib`.
2. From the `TownGraph/src` folder, compile and run the app. Replace `PATH_TO_FX` with your `lib` path.

**Windows (Command Prompt):**
```
set FX=C:\javafx-sdk-23\lib
javac --module-path "%FX%" --add-modules javafx.controls -d ..\out Town.java Road.java GraphInterface.java Graph.java TownGraphManagerInterface.java TownGraphManager.java FXMainPane.java DriverFX.java
java --module-path "%FX%" --add-modules javafx.controls -cp ..\out DriverFX
```

**macOS / Linux:**
```
FX=/path/to/javafx-sdk-23/lib
javac --module-path "$FX" --add-modules javafx.controls -d ../out Town.java Road.java GraphInterface.java Graph.java TownGraphManagerInterface.java TownGraphManager.java FXMainPane.java DriverFX.java
java --module-path "$FX" --add-modules javafx.controls -cp ../out DriverFX
```

**Using the app:**
1. Click **Read File** and choose `MD Towns.txt` or `US Towns.txt` from the `src` folder. You can also type a town name and click **Add Town**.
2. To add a road, enter a road name, pick the two towns, enter a distance, and click **Add Road**.
3. Under **Find Connection**, pick a starting town and a destination, then click **Find Connection**. The route and total distance appear below.
4. **Display Towns** and **Display Roads** list everything in the map.

### 3c. Run it in Eclipse

1. **File → Import → General → Existing Projects into Workspace** and select the `TownGraph` folder.
2. Create a user library named **`javaFX`** that contains the JARs from the JavaFX SDK `lib` folder:
   **Window → Preferences → Java → Build Path → User Libraries**.
3. Right-click `ConsoleDriver.java` or `DriverFX.java` and choose **Run As → Java Application**.
   For `DriverFX`, add these VM arguments in the run configuration:
   `--module-path "PATH_TO_FX" --add-modules javafx.controls`

### 4. Run the tests (optional)

The project has 78 JUnit tests, and all of them pass. In Eclipse, right-click the `src` folder and choose
**Run As → JUnit Test**. From the command line, download
[`junit-platform-console-standalone-1.11.4.jar`](https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar),
[`junit-4.13.2.jar`](https://repo1.maven.org/maven2/junit/junit/4.13.2/junit-4.13.2.jar) and
[`hamcrest-core-1.3.jar`](https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar)
into `TownGraph/lib`. Then run these from `TownGraph/src` (on Windows, use `;` instead of `:` between the classpath entries):

```
javac -d ../out -cp "../lib/*" Town.java Road.java GraphInterface.java Graph.java TownGraphManagerInterface.java TownGraphManager.java *Test.java
java -cp "../out:../lib/*" org.junit.platform.console.ConsoleLauncher execute --scan-classpath ../out
```

---

## How it works

### Project structure

```
TownGraph/
├── src/
│   ├── Town.java                    Vertex: a town, identified by its name
│   ├── Road.java                    Edge: a road between two towns, with a name and a distance
│   ├── GraphInterface.java          The graph ADT (operations a graph must support)
│   ├── Graph.java                   The graph: adjacency list + Dijkstra's algorithm
│   ├── TownGraphManagerInterface.java
│   ├── TownGraphManager.java        Works with town/road names; loads map files
│   ├── ConsoleDriver.java           Text-based app (no JavaFX)
│   ├── DriverFX.java                Starts the JavaFX app
│   ├── FXMainPane.java              The JavaFX window and its buttons
│   ├── MD Towns.txt, US Towns.txt   Sample maps
│   └── *Test.java                   JUnit tests
└── doc/                             Javadoc (open doc/index.html in a browser)
```

The classes are layered. The user interface (`ConsoleDriver` or `FXMainPane`) only works
with plain strings like `"Rockville"`. `TownGraphManager` turns those names into `Town`
and `Road` objects and calls `Graph`, which holds the data and runs the algorithm.

```
ConsoleDriver / FXMainPane  →  TownGraphManager  →  Graph  →  Town, Road
      (user interface)           (names ↔ objects)    (data + algorithm)
```

### Town and Road

- A **`Town`** is just a name. Two `Town` objects with the same name are *equal*, and
  their `hashCode()` depends only on the name, so they work as keys in a `HashMap`.
- A **`Road`** connects two towns and has a name and a distance in miles. Roads are
  **undirected**, so a road from A to B *equals* the same road from B to A. `hashCode()` is
  built so both directions produce the same value. `getOtherTown(t)` returns the
  town at the other end of the road, and the path-finding code uses it a lot.

### The graph: adjacency list

`Graph` stores a `Map<Town, List<Road>>` that maps every town to the roads touching it.
Because roads are two-way, each road is stored in *both* of its towns' lists.

| Operation | How it works | Time |
|---|---|---|
| Add a town | Put a new empty list in the map | O(1) |
| Add a road | Append it to both towns' lists | O(1) |
| Find a road between A and B | Scan A's list | O(roads at A) |
| Remove a road | Remove it from both towns' lists | O(roads at A + roads at B) |
| Remove a town | Remove each of its roads from its neighbors' lists, then drop the town | O(roads at that town) |

An adjacency list fits road maps well because each town only connects to a few
others. Storage is O(V + E), where V is the number of towns and E is the number of roads.

### Finding the shortest route: Dijkstra's algorithm

`dijkstraShortestPath(source)` finds the shortest distance from one town to *every* other town:

1. Set every town's distance to ∞, except the source, which is 0.
2. Put the source in a **priority queue** ordered by distance.
3. Repeatedly remove the closest unvisited town. For each road leaving it, check whether
   going through this town gives the neighbor a shorter distance. If it does, record the new
   distance and **the road used to get there**, then add the neighbor to the queue.
4. Stop when the queue is empty.

To rebuild the route, `shortestPath(source, destination)` starts at the destination
and follows the saved road back to the previous town, repeating until it reaches the source.
Then it reverses the list so the route reads from start to finish.

Implementation details:
- **Lazy deletion:** when a town's distance improves, a new entry is added to the priority queue
  instead of searching the queue to update the old one. Old entries are skipped when they come
  out. This keeps each queue operation at O(log V), so the whole algorithm runs in
  **O((V + E) log V)**.
- **The exact road is saved**, not just the previous town. If two towns are joined by
  more than one road, the route reports the shorter road Dijkstra actually chose.
- **Results are cached:** asking for several routes from the same starting town reuses one
  Dijkstra run. Adding or removing towns or roads clears the cache.

### Map file format

Each line of a map file describes one road:

```
RoadName,Miles;TownA;TownB
I270-N,14;Frederick;Clarksburg
```

`TownGraphManager.populateTownGraph` reads the file line by line, skips blank lines,
creates towns the first time they appear, and adds the road. If a line is badly
formatted, the error message gives the line number. You can create your own maps in
this format and load them in either version of the app.

---

## Credits

Designed and implemented by **Armel Daryl Kelodjoue Nguetchouang**. This includes `Town`, `Road`,
`Graph` (with Dijkstra's algorithm), `TownGraphManager`, `ConsoleDriver`, and the student
JUnit tests.

This started as a data-structures project for CMSC 204 at Montgomery College. The graph interfaces
(`GraphInterface`, `TownGraphManagerInterface`), the instructor test classes, and the original
JavaFX screen layout (`FXMainPane`, `DriverFX`) were provided as starter code. I extended
`FXMainPane` with bug fixes, input validation, and total trip distance.
