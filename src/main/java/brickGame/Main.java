package brickGame;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The Main class is the entry point for the Brick Game application.
 */
public class Main extends Application {

    /**
     * The main method to launch the JavaFX application.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Overrides the start method from the Application class.
     * Initializes the application by creating a new Controller and starting it with the given stage.
     *
     * @param stage The primary stage for the JavaFX application.
     * @throws Exception If an exception occurs during the initialization or startup of the application.
     */
    @Override
    public void start(Stage stage) throws Exception {
        Controller controller = new Controller();
        controller.start(stage);

    }
}