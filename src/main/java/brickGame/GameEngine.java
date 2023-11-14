package brickGame;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameEngine {

    private OnAction onAction;
    private int fps = 15;
    private Timeline updateTimeline;
    private Timeline physicsTimeline;
    private Timeline timeTimeline;
    public boolean isStopped = true;
    private long time = 0;

    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * @param fps set fps and we convert it to millisecond
     */
    public void setFps(int fps) {
        this.fps = (int) 1000 / fps;
    }

    private void initialize() {
        onAction.onInit();
    }

    public void start() {
        time = 0;
        initialize();
        createUpdateTimeline();
        createPhysicsTimeline();
        createTimeTimeline();
        isStopped = false;
    }

    public void stop() {
        if (!isStopped) {
            isStopped = true;
            updateTimeline.stop();
            physicsTimeline.stop();
            timeTimeline.stop();
        }
    }

    private void createUpdateTimeline() {
        updateTimeline = new Timeline(new KeyFrame(Duration.millis(fps), event -> {
            onAction.onUpdate();
        }));
        updateTimeline.setCycleCount(Animation.INDEFINITE);
        updateTimeline.play();
    }

    private void createPhysicsTimeline() {
        physicsTimeline = new Timeline(new KeyFrame(Duration.millis(fps), event -> {
            onAction.onPhysicsUpdate();
        }));
        physicsTimeline.setCycleCount(Animation.INDEFINITE);
        physicsTimeline.play();
    }

    private void createTimeTimeline() {
        timeTimeline = new Timeline(new KeyFrame(Duration.millis(1), event -> {
            time++;
            onAction.onTime(time);
        }));
        timeTimeline.setCycleCount(Animation.INDEFINITE);
        timeTimeline.play();
    }

    public interface OnAction {
        void onUpdate();

        void onInit();

        void onPhysicsUpdate();

        void onTime(long time);
    }
}
