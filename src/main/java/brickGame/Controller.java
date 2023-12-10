package brickGame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

/**
 * The {@code Controller} class serves as the main controller for the game.
 * It handles user input, game logic, and interactions between the model,view and game engine.
 * This class implements the EventHandler for KeyEvents and the GameEngine's OnAction interface.
 *
 * @author Chai Ze Xuan
 */
public class Controller  implements EventHandler<KeyEvent>, GameEngine.OnAction  {

    private double xBreak = 0.0f;
    private double centerBreakX;
    private double yBreak = 640.0f;

    private int breakWidth     = 130;
    private int sceneWidth = 500;
    private int sceneHeigt = 700;

    private static int LEFT  = 1;
    private static int RIGHT = 2;
    private double xBall;
    private double yBall;

    private boolean isGoldStauts      = false;
    private boolean isExistHeartBlock = false;
    private int       ballRadius = 10;

    private int destroyedBlockCount = 0;
    private int  heart    = 3;
    private int  score    = 0;
    private long time     = 0;
    private long goldTime = 0;
    private GameEngine engine;
    public static String savePath    = "C:/save/save.mdds";
    public static String savePathDir =  "C:/save/";

    private ArrayList<Block> blocks = new ArrayList<Block>();
    private ArrayList<Bonus> chocos = new ArrayList<Bonus>();
    private ArrayList<Bonus> snows = new ArrayList<Bonus>();
    private boolean loadFromSave = false;
    private boolean isPaused = false;
    private boolean isGameRunning = false;

    Stage primaryStage;
    private Ball playball;
    private Break paddle;
    private Model model = new Model();
    private View view = new View();
    private Sound sound = new Sound();
    private long savedTime;

    /**
     * The start method of the JavaFx application.
     * It initializes the game components, sets up the primary stage and starts the game engine.
     *
     * @param primaryStage The primary stage for the JavaFX application
     * @throws Exception If an exception occurs during initialization
     */
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        model.setBall(view.createBall());
        model.setRect(view.createRect());

        if (loadFromSave == false) {
            model.setLevel(model.getLevel()+1);
            if (model.getLevel() >1){
                view.showLevelUpMsg();
            }
            if (model.getLevel()==18){
                System.out.println("Bonus level!");
                view.showBonusLevelMsg();
            }
            if (model.getLevel() == 19) {
                blocks.clear();
                view.showWinMsg();
                return;
            }
            playball=model.initBall();
            paddle=model.initBreak();
            model.initBoard(blocks,model.getLevel(),isExistHeartBlock);
            view.loadButton();

        }
        view.setScene(model,blocks,primaryStage,score,model.getLevel());
        Scene scene = primaryStage.getScene();
        scene.setOnKeyPressed(this);

        if (loadFromSave == false) {
            if (model.getLevel() > 1 && model.getLevel() < 19) {
                view.hideButtons();
                engine = new GameEngine();
                engine.setOnAction(this);
                engine.setFps(120);
                engine.start();
            }

            view.getLoad().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    loadGame();
                    view.hideButtons();
                }
            });

            view.getNewGame().setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    engine = new GameEngine();
                    engine.setOnAction(Controller.this);
                    engine.setFps(120);
                    engine.start();
                    view.hideButtons();
                }
            });
        } else {
            engine = new GameEngine();
            engine.setOnAction(this);
            engine.setFps(120);
            engine.start();
            loadFromSave = false;
        }


    }

    /**
     * Handles the KeyEvents for controlling the game, including paddle movement, saving the game, toggling pause, and quitting the game.
     *
     * @param event The KeyEvent triggering the method.
     */
    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                move(LEFT);
                break;
            case RIGHT:

                move(RIGHT);
                break;
            case DOWN:
                //setPhysicsToBall();
                break;
            case S:
                saveGame();
                break;
            case SPACE:
                togglePause();
                break;
            case ESCAPE:
                showQuitConfirmation();
                break;
        }
    }

    /**
     * This method shows a confirmation window for quitting the game.
     * Stops the game engine, displays a confirmation window and handle user responses.
     * If the user chooses to quit, it exits the application;otherwise, resumes the game.
     */
    private void showQuitConfirmation() {
        engine.stop();
        isGameRunning = false;
        view.showPauseMsg();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit Game");
        alert.setHeaderText("Are you sure you want to quit?");
        alert.setContentText("Any unsaved progress will be lost.");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            // User clicked Yes, close the game
            Platform.exit();
        }
        if (result.isPresent() && result.get() == noButton) {
            engine = new GameEngine();
            engine.setOnAction(this);
            engine.setFps(120);
            engine.start();
            isGameRunning = true;
            view.showContinueMsg();
        }
    }

    /**
     * This method pause and resume the game.
     * Stops the game engine when the game is running, if the game is paused, resume the game.
     */
    private void togglePause() {
        isPaused = !isPaused;

        if (isPaused) {
            // Game is paused, stop the game engine and save the time state
            engine.stop();
            isGameRunning = false;
            savedTime = engine.getCurrentTime(); // Save the current time state
            view.showPauseMsg();
        } else {
            // Game is resumed, start or resume the game engine and restore the time state
            if (!isGameRunning) {
                engine = new GameEngine();
                engine.setOnAction(this);
                engine.setFps(120);
                engine.start();  // Note: You may need to initialize the engine before restoring the time state
                engine.restoreTimeState(savedTime); // Restore the saved time state
                isGameRunning = true;
                view.showContinueMsg();
            }
        }
    }

    /**
     * Moves the paddle in the specified direction using a separate thread.
     * The method creates a new thread to gradually move the paddle by incrementing or decrementing its x-coordinate.
     * The movement is achieved by changing the x-coordinate of the paddle in small steps for a set duration.
     * The thread pauses briefly between steps to control the speed of the movement.
     *
     * @param direction The direction in which the paddle should move. Use constants LEFT or RIGHT.
     */
    private void move(final int direction) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int sleepTime = 4;
                for (int i = 0; i < 30; i++) {
                    if (paddle.getxbreak() == (sceneWidth - breakWidth) && direction == RIGHT) {
                        return;
                    }
                    if (paddle.getxbreak() == 0 && direction == LEFT) {
                        return;
                    }
                    if (direction == RIGHT) {
                        paddle.setxbreak(paddle.getxbreak()+1);
                    } else {
                        paddle.setxbreak(paddle.getxbreak()-1);
                    }
                    paddle.setcenterbreakx(paddle.getxbreak()+ paddle.gethalfbreakwidth());
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i >= 20) {
                        sleepTime = i;
                    }
                }
            }
        }).start();
    }

    private boolean goDownBall                  = true;
    private boolean goRightBall                 = true;
    private boolean colideToBreak               = false;
    private boolean colideToBreakAndMoveToRight = true;
    private boolean colideToRightWall           = false;
    private boolean colideToLeftWall            = false;
    private boolean colideToRightBlock          = false;
    private boolean colideToBottomBlock         = false;
    private boolean colideToLeftBlock           = false;
    private boolean colideToTopBlock            = false;

    private double vX = 1.000;
    private double vY = 1.000;

    /**
     * handles the physics of ball
     * handles the movement of the ball after collision with wall and pedal
     */
    private void setPhysicsToBall() {
        playball.updatePosition();
        model.handleWallCollisions(playball,sceneWidth);
        model.handlePaddleCollision(playball,paddle);
        handleGameOver();
        model.handleBallMovement(playball);
    }

    /**
     * handles the deduction of heart when ball collide with bottom of the scene.
     */
    private void handleGameOver() {
        if (!isGoldStauts && playball.gety() >= sceneHeigt) {
            heart--;
            view.minusHeart();
            sound.playMinusHeartSound();
            if (heart == 0) {
                sound.playGameOverSound();
                view.showGameOverMsg(this);
                engine.stop();
            }
        }
    }

    /**
     * This method will let the game proceeds to next level when the destroyed block count is same as the blocks the current level has.
     */
    private void checkDestroyedCount() {
        if (destroyedBlockCount == blocks.size()) {
            //TODO win level todo...
            //System.out.println("You Win");

            nextLevel();
        }
    }

    /**
     * Saves the current game state to a file.
     */
    private void saveGame() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new File(savePathDir).mkdirs();
                File file = new File(savePath);
                ObjectOutputStream outputStream = null;
                try {
                    outputStream = new ObjectOutputStream(new FileOutputStream(file));

                    outputStream.writeInt(model.getLevel());
                    outputStream.writeInt(score);
                    outputStream.writeInt(heart);
                    outputStream.writeInt(destroyedBlockCount);


                    outputStream.writeDouble(xBall);
                    outputStream.writeDouble(yBall);
                    outputStream.writeDouble(xBreak);
                    outputStream.writeDouble(yBreak);
                    outputStream.writeDouble(centerBreakX);
                    outputStream.writeLong(time);
                    outputStream.writeLong(goldTime);
//                    outputStream.writeLong(snowTime);
                    outputStream.writeDouble(vX);


                    outputStream.writeBoolean(isExistHeartBlock);
                    outputStream.writeBoolean(isGoldStauts);
//                    outputStream.writeBoolean(isSnowStauts);
                    outputStream.writeBoolean(goDownBall);
                    outputStream.writeBoolean(goRightBall);
                    outputStream.writeBoolean(colideToBreak);
                    outputStream.writeBoolean(colideToBreakAndMoveToRight);
                    outputStream.writeBoolean(colideToRightWall);
                    outputStream.writeBoolean(colideToLeftWall);
                    outputStream.writeBoolean(colideToRightBlock);
                    outputStream.writeBoolean(colideToBottomBlock);
                    outputStream.writeBoolean(colideToLeftBlock);
                    outputStream.writeBoolean(colideToTopBlock);

                    ArrayList<BlockSerializable> blockSerializables = new ArrayList<BlockSerializable>();
                    for (Block block : blocks) {
                        if (block.isDestroyed) {
                            continue;
                        }
                        blockSerializables.add(new BlockSerializable(block.row, block.column, block.type));
                    }

                    outputStream.writeObject(blockSerializables);

                    view.showSavedMsg();


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    /**
     * Loads a previously saved game state from a file.
     */
    private void loadGame() {

        LoadSave loadSave = new LoadSave();
        loadSave.read();
        model.setGameState(loadSave,playball,paddle);
        model.setLevel(loadSave.level);
        score= model.getScore();
        heart=model.getHeart();
        model.restoreBlocks(loadSave,blocks);

        try {
            loadFromSave = true;
            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Reset or initialize variables related to game state
        nextLevelInProgress = false;
        destroyedBlockCount = 0;

        // Start or restart timelines
        if (engine != null) {
            engine.stop();
        }
        engine = new GameEngine();
        engine.setOnAction(this);
        engine.setFps(120);
        engine.start();

    }

    /**
     * Handles the logic for the next game level, including resetting parameters and initializing the next level.
     */
    private boolean nextLevelInProgress = false;
    private void nextLevel() {
        sound.playNextLevelSound();
        resetSnowBonus(playball);
        if (nextLevelInProgress) {
            return;
        }
        nextLevelInProgress = true;

        Platform.runLater(() -> {
            try {
                model.resetGameParameters(playball);
                resetEngineAndBall();
                model.clearGameElements();
                initializeGame();
                nextLevelInProgress = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void resetEngineAndBall() {
        engine.stop();
    }
    private void initializeGame() throws Exception {
        start(primaryStage);
    }

    /**
     * reset all the parameters in the game
     */
    public void restartGame() {
        try {
            model.clearPenalty(playball);
            model.resetGame(playball);
            model.setLevel(0);
            model.setScore(0);
            model.setHeart(3);
            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Implements the onUpdate method from the GameEngine's OnAction interface.
     * Updates the game state, checks for collisions, and handles bonuses.
     */
    @Override
    public void onUpdate() {
        Platform.runLater(() -> {
            view.showLabel(score,heart);
            model.setBallPaddle(playball, paddle);

            for (Bonus choco : chocos) {
                choco.choco.setY(choco.y);
            }
            for (Bonus snow : snows) {
                snow.snow.setY(snow.y);
            }
        });

        if (playball.gety() >= Block.getPaddingTop() && playball.gety() <= (Block.getHeight() * (model.getLevel() + 1)) + Block.getPaddingTop()) {
            for (final Block block : blocks) {
                double hitCode = block.checkHitToBlock(playball.getx(), playball.gety(), ballRadius);
                if (hitCode != Block.NO_HIT) {
                    //sound.playHitSound();
                    score += 1;

                    view.addScore(block);

                    block.rect.setVisible(false);
                    block.isDestroyed = true;
                    destroyedBlockCount++;
                    //System.out.println("size is " + blocks.size());
                    model.resetColideFlags();

                    if (block.type == Block.BLOCK_CHOCO) {
                        sound.playHitSound();
                        final Bonus choco = new Bonus(block.row, block.column);
                        choco.timeCreated = time;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                view.getRoot().getChildren().add(choco.choco);
                            }
                        });
                        chocos.add(choco);
                    }

                    if (block.type == Block.BLOCK_STAR) {
                        sound.playGoldBallSound();
                        goldTime = time;
                        view.GoldState();
                        isGoldStauts = true;
                    }
                    if (block.type == Block.BLOCK_SNOW) {
                        sound.playHitSound();
                        final Bonus snow = new Bonus(block.row, block.column);
                        snow.timeCreated = time;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                view.getRoot().getChildren().add(snow.snow);
                            }
                        });
                        snows.add(snow);

                    }

                    if (block.type == Block.BLOCK_HEART) {
                        sound.playExtraHeartSound();
                        heart++;
                    }
                    sound.playHitSound();
                    model.setCollision(hitCode);
                }

                //TODO hit to break and some work here....
                //System.out.println("Break in row:" + block.row + " and column:" + block.column + " hit");
            }
        }
    }

    /**
     * Implements the onInit method from the GameEngine's OnAction interface.
     * Handles initialization tasks when the game engine is started.
     */
    @Override
    public void onInit() {

    }
    private static final double TARGET_SLOW_SPEED = 0.3;
    private static final double SLOWDOWN_FACTOR = 0.98;
    private static final long SNOW_BONUS_DURATION = 10000;

    private boolean isSnowBonusActive = false;
    private long snowBonusStartTime = 0;

    /**
     * Implements the onPhysicsUpdate method from the GameEngine's OnAction interface.
     * Handle physics updates, such as ball movement and collision detection.
     */
    @Override
    public void onPhysicsUpdate() {
        Platform.runLater(() -> {
            checkDestroyedCount();
            setPhysicsToBall();
            if (isPaused) {
                return; // Skip physics update if the game is paused
            }
        });

        if (time - goldTime > 5000) {
            view.RemoveGoldState();
            isGoldStauts = false;
            goldTime = 0;
        }
        for (Bonus choco : chocos) {
            if (model.shouldSkipChoco(choco)) {
                continue;
            }

            if (model.isChocoCaught(choco, paddle)) {
                score=view.handleCaughtChoco(choco,score);
            }
            model.updateChocoPosition(choco, time);
        }
        for (Bonus snow : snows) {
            model.handleSnowBonusCollision(snow, playball, paddle, time);
            model.updateSnowPosition(snow, time);
        }

        if (model.getisSnowBonusActive()) {
            model.applySnowBonus(playball, time);
        }
    }

    /**
     * Resets the snow bonus, restoring the ball's speed to its original values and deactivating the snow bonus.
     * Additionally, it sets the ball's fill to the default image pattern.
     *
     * @param playball The Ball object for which the snow bonus is being reset.
     */
    private void resetSnowBonus(Ball playball) {
        playball.setvx(1.0); // Reset to the original speed
        playball.setvy(1.0);
        isSnowBonusActive = false;
        model.getBall().setFill(new ImagePattern(new Image("ball.png")));
    }

    /**
     * Overrides the onTime method to update the current time variable in the controller.
     *
     * @param time The current time value provided by the game engine.
     */
    @Override
    public void onTime(long time) {
        this.time = time;
    }
}