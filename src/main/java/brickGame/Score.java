package brickGame;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.function.IntUnaryOperator;

public class Score {
    public void show(final double x, final double y, int score, final View view) {
        String sign = (score >= 0) ? "+" : "";
        final Label label = createLabel(sign + score, x, y);

        Platform.runLater(() -> view.root.getChildren().add(label));
        animateLabel(label, 21, i -> i, 20);
    }

    public void showMessage(String message, final View view) {
        final Label label = createLabel(message, 220, 340);

        Platform.runLater(() -> view.root.getChildren().add(label));
        animateLabel(label, 21, i -> Math.abs(i - 10), 20);
    }

    public void showGameOver(final View view, Controller controller) {
        Platform.runLater(() -> {
            Label label = createLabel("Game Over :(", 200, 250);
            label.setScaleX(2);
            label.setScaleY(2);

            Button restart = createRestartButton(view, controller);

            view.root.getChildren().addAll(label, restart);
        });
    }

    public void showWin(final View view) {
        Platform.runLater(() -> {
            Label label = createLabel("You Win :)", 200, 250);
            label.setScaleX(2);
            label.setScaleY(2);

            view.root.getChildren().addAll(label);
        });
    }

    private Label createLabel(String text, double x, double y) {
        Label label = new Label(text);
        label.setTranslateX(x);
        label.setTranslateY(y);
        return label;
    }

    private Button createRestartButton(View view, Controller controller) {
        Button restart = new Button("Restart");
        restart.setTranslateX(220);
        restart.setTranslateY(300);
        restart.setOnAction(event -> controller.restartGame());
        return restart;
    }

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



