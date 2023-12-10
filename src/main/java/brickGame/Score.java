package brickGame;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.function.IntUnaryOperator;

/**
 * The Score class manages the display and animation of score-related messages in the game.
 */
public class Score {

    /**
     * Displays a score message at the specified coordinates on the game view.
     *
     * @param x     The x-coordinate for the message.
     * @param y     The y-coordinate for the message.
     * @param score The score value to be displayed.
     * @param view  The game view where the message should be displayed.
     */
    public void show(final double x, final double y, int score, final View view) {
        String sign = (score >= 0) ? "+" : "";
        final Label label = createLabel(sign + score, x, y);

        Platform.runLater(() -> view.root.getChildren().add(label));
        animateLabel(label, 21, i -> i, 20);
    }

    /**
     * Displays a general message on the game view with animation.
     *
     * @param message The message to be displayed.
     * @param view    The game view where the message should be displayed.
     */
    public void showMessage(String message, final View view) {
        final Label label = createLabel(message, 220, 340);
        Platform.runLater(() -> view.root.getChildren().add(label));
        animateLabel(label, 21, i -> Math.abs(i - 10), 20);
    }

    /**
     * Displays a game over message and restart button on the game view.
     *
     * @param view       The game view where the message and button should be displayed.
     * @param controller The game controller to handle the restart action.
     */
    public void showGameOver(final View view, Controller controller) {
        Platform.runLater(() -> {
            Label label = createLabel("Game Over :(", 200, 250);
            label.setScaleX(2);
            label.setScaleY(2);

            Button restart = createRestartButton(view, controller);

            view.root.getChildren().addAll(label, restart);
        });
    }

    /**
     * Displays a "You Win" message on the game view.
     *
     * @param view The game view where the message should be displayed.
     */
    public void showWin(final View view) {
        Platform.runLater(() -> {
            Label label = createLabel("You Win :)", 200, 250);
            label.setScaleX(2);
            label.setScaleY(2);

            view.root.getChildren().addAll(label);
        });
    }

    /**
     * Creates a JavaFX Label with the specified text and position coordinates.
     *
     * @param text The text to be displayed on the label.
     * @param x    The x-coordinate of the label's position.
     * @param y    The y-coordinate of the label's position.
     * @return The created Label with the specified text and position.
     */
    private Label createLabel(String text, double x, double y) {
        Label label = new Label(text);
        label.setTranslateX(x);
        label.setTranslateY(y);
        return label;
    }

    /**
     * Creates a JavaFX Button labeled "Restart" and sets its position on the game view.
     * Associates the button with the specified controller to handle the restart action.
     *
     * @param view       The game view where the button should be displayed.
     * @param controller The game controller responsible for handling the restart action.
     * @return The created Button for restarting the game.
     */
    private Button createRestartButton(View view, Controller controller) {
        Button restart = new Button("Restart");
        restart.setTranslateX(220);
        restart.setTranslateY(300);
        restart.setOnAction(event -> controller.restartGame());
        return restart;
    }

    /**
     * Animates a JavaFX Label with a scaling and fading effect over a specified duration.
     *
     * @param label           The Label to be animated.
     * @param iterations      The number of animation iterations.
     * @param scaleXFunction  A function to determine the scaling factor for the x-axis during animation.
     * @param opacityBase     The initial opacity of the label during animation.
     */
    private void animateLabel(Label label, int iterations, IntUnaryOperator scaleXFunction, int opacityBase) {
        Timeline timeline = new Timeline();

        for (int i = 0; i < iterations; i++) {

            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis(i * 15),
                    new KeyValue(label.scaleXProperty(), scaleXFunction.applyAsInt(i)),
                    new KeyValue(label.scaleYProperty(), scaleXFunction.applyAsInt(i)),
                    new KeyValue(label.opacityProperty(), (opacityBase - i) / (double) opacityBase)
            );
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.play();
    }

}