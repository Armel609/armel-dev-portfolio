import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * DriverFX.java
 *
 * Starts the JavaFX (graphical) version of the TownGraph app.
 * For a version that needs no JavaFX, run {@link ConsoleDriver} instead.
 *
 * Provided as starter code by the CMSC 204 course; window size and title
 * updated by Armel Daryl Kelodjoue Nguetchouang.
 */
public class DriverFX extends Application {

	/**
	 * Launches the JavaFX application.
	 * @param args not used
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		FXMainPane root = new FXMainPane();
		stage.setScene(new Scene(root, 700, 750));
		stage.setTitle("TownGraph - Shortest Route Finder");
		stage.show();
	}
}
