


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/**
 * FXMainPane.java
 *
 * The JavaFX window for the TownGraph app. It lets the user add towns and
 * roads, list them, load a road network from a file, and find the shortest
 * route between two towns.
 *
 * The screen layout was provided as starter code by the CMSC 204 course.
 * Armel Daryl Kelodjoue Nguetchouang fixed its bugs (a crash when no towns
 * were selected, drop-downs not refreshing), added input validation and
 * error messages, and added the total trip distance to the route output.
 */
public class FXMainPane extends VBox {
	Label addTownLabel, townNameLabel, addRoadLabel, roadNameLabel, selectTownsForRoadLabel, findConnectionLabel, findConnectionFromLabel, toLabel, distLabel;
	VBox addTownVBox, addRoadVBox, findConnectionVBox, bottomVBox;
	HBox addTownHBox, addRoadNameHBox, addRoadHBox, addRoadTownsHBox, findConnectionHBox, bottomHBox;
	Button addTownButton, addRoadButton, findConnectionButton, readFileButton, exitButton;
	Button displayTownsButton, displayRoadsButton;
	TextField addTownTextField, addRoadTextField, specifyDistanceTextField;
	TextArea findConnectionTextArea, displayTowns, displayRoads;
	ComboBox<String> addSourceTownComboBox, addDestTownComboBox, sourceConnectionComboBox, destConnectionComboBox; 
	Insets inset, inset2, inset3;

	TownGraphManager graph;
	private Alert alert = new Alert(AlertType.ERROR);
	
	
	FXMainPane() {
		//TownGraphManager object
		graph = new TownGraphManager();
		//set up margins
		inset = new Insets(10);
		
		
		//add-town components
		addTownLabel = new Label("Add Town");
		addTownLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold");
		townNameLabel = new Label("Town Name: ");
		
		addTownTextField = new TextField();
		addTownTextField.setPrefColumnCount(10);
		
		displayTowns = new TextArea();
		
		addTownButton = new Button("Add Town");
		displayTownsButton = new Button("Display Towns");
		
		//HBox and VBox for add town area
		addTownHBox = new HBox();
		addTownHBox.getChildren().addAll(townNameLabel, addTownTextField, addTownButton);
		addTownVBox = new VBox();

		VBox.setMargin(addTownLabel, inset);
	    HBox.setMargin(townNameLabel, inset);
	    VBox.setMargin(addTownHBox, inset);
	    HBox.setMargin(addTownLabel, inset);
	    HBox.setMargin(addTownButton, inset);
	    
	    addTownHBox.setAlignment(Pos.CENTER);
	    addTownVBox.setAlignment(Pos.CENTER);
	    setAlignment(Pos.CENTER);
		
		addTownVBox.getChildren().addAll(addTownLabel, addTownHBox);
		addTownVBox.setStyle("-fx-border-color: gray;");
		addTownVBox.setPrefWidth(400);

		//VBox for the display Towns area
		VBox displayTownVBox = new VBox();
		displayTownVBox.setAlignment(Pos.CENTER);
		displayTownVBox.setStyle("-fx-border-color: gray;");
		displayTownVBox.setPrefWidth(200);
		displayTownVBox.getChildren().addAll(displayTowns, displayTownsButton);
		VBox.setMargin(displayTownsButton, inset);
		VBox.setMargin(displayTowns, inset);
		
		HBox addTown = new HBox();
		addTown.setAlignment(Pos.CENTER);
		addTown.getChildren().addAll(addTownVBox, displayTownVBox);

		//add-road area components
		addRoadLabel = new Label("Add Road");
		addRoadLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold");
		roadNameLabel = new Label("Road Name: ");
		selectTownsForRoadLabel = new Label("Select Towns the Road Connects");
		distLabel = new Label("Distance");
		
		displayRoads = new TextArea();

		//ComboBoxes of all towns
		addSourceTownComboBox = new ComboBox<String>();
		addDestTownComboBox = new ComboBox<String>();

		displayRoadsButton = new Button("Display Roads");
		addRoadButton = new Button("Add Road");

		addRoadTextField = new TextField();
		addRoadTextField.setPrefColumnCount(10);
		specifyDistanceTextField = new TextField();
		specifyDistanceTextField.setPrefColumnCount(10);

		//HBoxes and VBoxes to put Add Road area together
		addRoadHBox = new HBox();
		addRoadHBox.getChildren().addAll(roadNameLabel, addRoadTextField);
		addRoadHBox.setAlignment(Pos.CENTER);
		
		addRoadTownsHBox = new HBox();
	    HBox.setMargin(addSourceTownComboBox, inset);
	    HBox.setMargin(addDestTownComboBox, inset);
	    HBox.setMargin(distLabel, inset);
	    HBox.setMargin(specifyDistanceTextField, inset);
	    HBox.setMargin(addRoadButton, inset);

	    HBox addRoadTownsHBox2 = new HBox();
	    addRoadTownsHBox2.getChildren().addAll(distLabel, specifyDistanceTextField);
	    addRoadTownsHBox2.setAlignment(Pos.CENTER);

		addRoadTownsHBox.getChildren().addAll(addSourceTownComboBox, addDestTownComboBox);
		addRoadTownsHBox.setAlignment(Pos.CENTER);
		
		addRoadVBox = new VBox();
		addRoadVBox.setAlignment(Pos.CENTER);
		addRoadVBox.getChildren().addAll(addRoadLabel, addRoadHBox, selectTownsForRoadLabel,addRoadTownsHBox,addRoadTownsHBox2,addRoadButton);
		addRoadVBox.setAlignment(Pos.CENTER);
		addRoadVBox.setPrefWidth(400);
		VBox.setMargin(addRoadButton, inset);

		addRoadVBox.setStyle("-fx-border-color: gray;");
		
		//HBoxes and VBoxes for displaying all roads
		VBox displayRoadVBox = new VBox();
		displayRoadVBox.setAlignment(Pos.CENTER);
		displayRoadVBox.setStyle("-fx-border-color: gray;");
		displayRoadVBox.setPrefWidth(200);
		displayRoadVBox.getChildren().addAll(displayRoads, displayRoadsButton);
		VBox.setMargin(displayRoadsButton, inset);
		VBox.setMargin(displayRoads, inset);

		HBox addRoad = new HBox();
		addRoad.setAlignment(Pos.CENTER);
		addRoad.getChildren().addAll(addRoadVBox, displayRoadVBox);

		VBox.setMargin(addRoadLabel, inset);
		VBox.setMargin(addRoadHBox, inset);
		VBox.setMargin(selectTownsForRoadLabel, inset);
		HBox.setMargin(roadNameLabel, inset);
		HBox.setMargin(addRoadTextField, inset);
		HBox.setMargin(addRoadTownsHBox, inset);
		HBox.setMargin(addRoadTownsHBox2, inset);

	    
		//find connection area components
	    sourceConnectionComboBox = new ComboBox<String>();
	    destConnectionComboBox = new ComboBox<String>();

	    findConnectionLabel = new Label("Find Connection");
	    findConnectionLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold");
	    findConnectionFromLabel = new Label("Find connection from ");
		toLabel = new Label("to");

		findConnectionTextArea = new TextArea();
		
		findConnectionButton = new Button("Find Connection");
		
		//HBoxes and VBoxes for the Find Connection area
		findConnectionVBox = new VBox();
		findConnectionHBox = new HBox();
		findConnectionHBox.getChildren().addAll(findConnectionFromLabel, sourceConnectionComboBox, toLabel, destConnectionComboBox, findConnectionButton);
		findConnectionVBox.getChildren().addAll(findConnectionLabel, findConnectionHBox, findConnectionTextArea);
		findConnectionVBox.setStyle("-fx-border-color: gray;");
		VBox.setMargin(findConnectionTextArea, inset);

		VBox.setMargin(findConnectionHBox, inset);
	    VBox.setMargin(findConnectionLabel, inset);
	    HBox.setMargin(findConnectionFromLabel, inset);
	    HBox.setMargin(sourceConnectionComboBox, inset);
	    HBox.setMargin(toLabel, inset);
	    HBox.setMargin(destConnectionComboBox, inset);
	    HBox.setMargin(findConnectionButton, inset);

	    findConnectionHBox.setAlignment(Pos.CENTER);
	    findConnectionVBox.setAlignment(Pos.CENTER);
		
	    //bottom button area components
	    bottomHBox = new HBox();
		readFileButton = new Button("Read File");
		exitButton =new Button("Exit");

		bottomVBox = new VBox();
		bottomVBox.getChildren().addAll(bottomHBox);
		bottomVBox.setStyle("-fx-border-color: gray;");

		bottomHBox.getChildren().addAll(readFileButton, exitButton);
		
		bottomHBox.setAlignment(Pos.CENTER);

		VBox.setMargin(bottomHBox,inset);
	    HBox.setMargin(readFileButton, inset);
	    HBox.setMargin(exitButton, inset);

		getChildren().addAll(addTown, addRoad, findConnectionVBox, bottomHBox);
		
		// ---------------- Button actions ----------------

		displayTownsButton.setOnAction(event -> refreshTownList());
		displayRoadsButton.setOnAction(event -> refreshRoadList());

		addTownButton.setOnAction(event -> {
			String townName = addTownTextField.getText().trim();
			if (townName.isEmpty()) {
				showError("Town name cannot be empty");
			}
			else if (graph.containsTown(townName)) {
				showError("Town \"" + townName + "\" already exists");
			}
			else {
				graph.addTown(townName);
				addTownTextField.clear();
				updateComboBoxes();
				refreshTownList();
			}
		});

		addRoadButton.setOnAction(event -> {
			String town1 = addSourceTownComboBox.getValue();
			String town2 = addDestTownComboBox.getValue();
			String name = addRoadTextField.getText().trim();
			String distanceText = specifyDistanceTextField.getText().trim();

			int weight;
			try {
				weight = Integer.parseInt(distanceText);
			}
			catch (NumberFormatException e) {
				weight = -1;
			}

			if (town1 == null || town2 == null) {
				showError("Select the two towns the road connects");
			}
			else if (town1.equals(town2)) {
				showError("A road must connect two different towns");
			}
			else if (name.isEmpty()) {
				showError("Road name cannot be blank");
			}
			else if (weight < 0) {
				showError("Distance must be a whole number of miles (0 or more)");
			}
			else {
				graph.addRoad(town1, town2, weight, name);
				addSourceTownComboBox.setValue(null);
				addDestTownComboBox.setValue(null);
				addRoadTextField.clear();
				specifyDistanceTextField.clear();
				refreshRoadList();
			}
		});

		findConnectionButton.setOnAction(event -> {
			String town1 = sourceConnectionComboBox.getValue();
			String town2 = destConnectionComboBox.getValue();
			findConnectionTextArea.clear();

			if (town1 == null || town2 == null) {
				findConnectionTextArea.setText("Select a starting town and a destination town");
				return;
			}
			if (town1.equals(town2)) {
				findConnectionTextArea.setText("Select two different towns");
				return;
			}

			ArrayList<String> path = graph.getPath(town1, town2);
			if (path.isEmpty()) {
				findConnectionTextArea.setText("You can't get there from here");
			}
			else {
				findConnectionTextArea.setText(String.join("\n", path)
						+ "\n\nTotal distance: " + graph.getPathDistance(town1, town2) + " mi");
			}
		});

		readFileButton.setOnAction(event -> readFile());

		exitButton.setOnAction(event -> {
			Platform.exit();
			System.exit(0);
		});
	}

	/** Shows every town, one per line, in the town list box. */
	private void refreshTownList() {
		displayTowns.setText(String.join("\n", graph.allTowns()));
	}

	/** Shows every road, one per line, in the road list box. */
	private void refreshRoadList() {
		displayRoads.setText(String.join("\n", graph.allRoads()));
	}

	/** Pops up an error dialog with the given message. */
	private void showError(String message) {
		alert.setTitle("Error");
		alert.setHeaderText(message);
		alert.showAndWait();
	}

	/** Reloads all four town drop-downs so they list every town in the graph. */
	public void updateComboBoxes() {
		ArrayList<String> townList = graph.allTowns();
		for (ComboBox<String> box : java.util.List.of(addSourceTownComboBox, addDestTownComboBox,
				sourceConnectionComboBox, destConnectionComboBox)) {
			box.getItems().setAll(townList);
		}
	}

	/**
	 * Lets the user pick a road file, loads it into the graph, and refreshes
	 * the drop-downs and lists. Shows an error dialog if the file is missing
	 * or badly formatted.
	 */
	public void readFile() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Choose a road file (e.g. MD Towns.txt)");
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files", "*.txt"));
		File startDir = new File(System.getProperty("user.dir"));
		if (startDir.isDirectory())
			chooser.setInitialDirectory(startDir);

		File selectedFile = chooser.showOpenDialog(getScene() == null ? null : getScene().getWindow());
		if (selectedFile == null)
			return;   // user cancelled

		try {
			graph.populateTownGraph(selectedFile);
		}
		catch (FileNotFoundException e) {
			showError("File not found: " + selectedFile.getName());
		}
		catch (IllegalArgumentException e) {
			showError("Could not read file. " + e.getMessage());
		}
		updateComboBoxes();
		refreshTownList();
		refreshRoadList();
	}
}
