package brickGame;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * The GameEngine class manages the game loop, timelines, and time-related operations.
 */
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
     * @param fps set fps and we convert it to milliseconds
     */
    public void setFps(int fps) {
        this.fps = (int) 1000 / fps;
    }

    /**
     * Initializes the game engine by calling the onInit method in the callback interface.
     */
    private void initialize() {
        onAction.onInit();
    }

    /**
     * Starts the game engine by initializing, creating timelines, and setting the stopped flag to false.
     */
    public void start() {
        initialize();
        createUpdateTimeline();
        createPhysicsTimeline();
        createTimeTimeline();
        isStopped = false;
    }

    /**
     * Stops the game engine by stopping the update, physics, and time timelines.
     */
    public void stop() {
        if (!isStopped) {
            isStopped = true;
            updateTimeline.stop();
            physicsTimeline.stop();
            timeTimeline.stop();
        }
    }


    // New method to restore the time state
    /**
     * Restores the time state to a specified saved time.
     *
     * @param savedTime The time state to be restored.
     */
    public void restoreTimeState(long savedTime) {
        time = savedTime;
    }

    /**
     * Gets the current time value in the game.
     *
     * @return The current time value.
     */
    public long getCurrentTime() {
        return time;
    }

    /**
     * Creates the update timeline responsible for calling the onUpdate method in the callback interface.
     */
    private void createUpdateTimeline() {
        updateTimeline = new Timeline(new KeyFrame(Duration.millis(fps), event -> {
            onAction.onUpdate();
        }));
        updateTimeline.setCycleCount(Animation.INDEFINITE);
        updateTimeline.play();
    }

    /**
     * Creates the physics timeline responsible for calling the onPhysicsUpdate method in the callback interface.
     */
    private void createPhysicsTimeline() {
        physicsTimeline = new Timeline(new KeyFrame(Duration.millis(fps), event -> {
            onAction.onPhysicsUpdate();
        }));
        physicsTimeline.setCycleCount(Animation.INDEFINITE);
        physicsTimeline.play();
    }

    /**
     * Creates the time timeline responsible for tracking time and calling the onTime method in the callback interface.
     */
    private void createTimeTimeline() {
        timeTimeline = new Timeline(new KeyFrame(Duration.millis(1), event -> {
            time++;
            onAction.onTime(time);
        }));
        timeTimeline.setCycleCount(Animation.INDEFINITE);
        timeTimeline.play();
    }

    /**
     * Callback interface for game actions.
     */
    public interface OnAction {
        void onUpdate();

        void onInit();

        void onPhysicsUpdate();

        void onTime(long time);
    }
}
