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

import static javafx.application.Application.launch;

public class Controller  implements EventHandler<KeyEvent>, GameEngine.OnAction  {

    private int level = 0;

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

//    private Rectangle rect;
    private int       ballRadius = 10;

    private int destroyedBlockCount = 0;

//    private double v = 1.000;

    private int  heart    = 3;
    private int  score    = 0;
    private long time     = 0;
    private long hitTime  = 0;
    private long goldTime = 0;
//    private long snowTime = 0;

    private GameEngine engine;
    public static String savePath    = "C:/save/save.mdds";
    public static String savePathDir =  "C:/save/";

    private ArrayList<Block> blocks = new ArrayList<Block>();
    private ArrayList<Bonus> chocos = new ArrayList<Bonus>();
    private ArrayList<Bonus> snows = new ArrayList<Bonus>();
    private Color[]          colors = new Color[]{
            Color.MAGENTA,
            Color.RED,
            Color.GOLD,
            Color.CORAL,
            Color.AQUA,
            Color.VIOLET,
            Color.GREENYELLOW,
            Color.ORANGE,
            Color.PINK,
            Color.SLATEGREY,
            Color.YELLOW,
            Color.TOMATO,
            Color.TAN,
    };
    public Pane root;
    private Label scoreLabel;
    private Label            heartLabel;
    private Label            levelLabel;

    private boolean loadFromSave = false;
    private boolean isPaused = false;
    private boolean isGameRunning = false;

    Stage primaryStage;
    Button load    = null;
    Button newGame = null;
    private Ball playball;
    private Break paddle;
    private Model model = new Model();
    private View view = new View();
    private long savedTime;


    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        model.setBall(view.createBall());
        model.setRect(view.createRect());

        if (loadFromSave == false) {
            level++;
            if (level >1){
                view.showLevelUpMsg();
            }
            if (level == 18) {
                view.showWinMsg();
                return;
            }

            playball=model.initBall();
            paddle=model.initBreak();
            model.initBoard(blocks,level,isExistHeartBlock);

            view.loadButton();
        }
        view.setScene(model,blocks,primaryStage,score,level);
        Scene scene = primaryStage.getScene();
        scene.setOnKeyPressed(this);

        if (loadFromSave == false) {
            if (level > 1 && level < 18) {
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

    private void setPhysicsToBall() {
        playball.updatePosition();
        model.handleWallCollisions(playball,sceneWidth);
        model.handlePaddleCollision(playball,paddle);
        handleGameOver();
        model.handleBallMovement(playball);
    }


    private void handleGameOver() {
        if (!isGoldStauts && playball.gety() >= sceneHeigt) {
            heart--;
            view.minusHeart();

            if (heart == 0) {
                view.showGameOverMsg(this);
                engine.stop();
            }
        }
    }

    private void checkDestroyedCount() {
        if (destroyedBlockCount == blocks.size()) {
            //TODO win level todo...
            //System.out.println("You Win");

            nextLevel();
        }
    }

    private void saveGame() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new File(savePathDir).mkdirs();
                File file = new File(savePath);
                ObjectOutputStream outputStream = null;
                try {
                    outputStream = new ObjectOutputStream(new FileOutputStream(file));

                    outputStream.writeInt(level);
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

    private void loadGame() {

        LoadSave loadSave = new LoadSave();
        loadSave.read();
        model.setGameState(loadSave,playball,paddle);
        level= model.getLevel();
        score= model.getScore();
        heart= model.getHeart();
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

    private boolean nextLevelInProgress = false;
    private void nextLevel() {
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

    public void restartGame() {

        try {
            resetSnowBonus(playball);
            model.resetGame(playball);
            level= model.getLevel();
            score= model.getScore();
            heart= model.getHeart();
            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

        if (playball.gety() >= Block.getPaddingTop() && playball.gety() <= (Block.getHeight() * (level + 1)) + Block.getPaddingTop()) {
            for (final Block block : blocks) {
                double hitCode = block.checkHitToBlock(playball.getx(), playball.gety(), ballRadius);
                if (hitCode != Block.NO_HIT) {
                    score += 1;

                    view.addScore(block);

                    block.rect.setVisible(false);
                    block.isDestroyed = true;
                    destroyedBlockCount++;
                    //System.out.println("size is " + blocks.size());
                    model.resetColideFlags();

                    if (block.type == Block.BLOCK_CHOCO) {
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
                        goldTime = time;
                        view.GoldState();
                        isGoldStauts = true;
                    }
                    if (block.type == Block.BLOCK_SNOW) {
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
                        heart++;
                    }
                    model.setCollision(hitCode);
                }

                //TODO hit to break and some work here....
                //System.out.println("Break in row:" + block.row + " and column:" + block.column + " hit");
            }
        }
    }

    @Override
    public void onInit() {

    }
    private static final double TARGET_SLOW_SPEED = 0.3;
    private static final double SLOWDOWN_FACTOR = 0.98;
    private static final long SNOW_BONUS_DURATION = 10000;

    private boolean isSnowBonusActive = false;
    private long snowBonusStartTime = 0;

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


    // Check for active snow bonus and update the ball speed
    private void handleSnowBonusCollision(Bonus snow, Ball playball, Break paddle, long time) {
        if (snow.y > sceneHeigt || snow.taken) {
            return;
        }

        if (isSnowCaught(snow, paddle)) {
            System.out.println("You Got a Penalty! (Ball will slow down for 10 seconds)");
            snow.taken = true;
            snow.snow.setVisible(false);
            activateSnowBonus(); // Activate snow bonus on collision
        }
    }

    private boolean isSnowCaught(Bonus snow, Break paddle) {
        return snow.y >= paddle.getybreak() && snow.y <= paddle.getybreak() + paddle.getbreakwidth()
                && snow.x >= paddle.getxbreak() && snow.x <= paddle.getxbreak() + paddle.getbreakwidth();
    }

    private void applySnowBonus(Ball playball, long time) {
        playball.setvx(playball.getVx() * SLOWDOWN_FACTOR);
        playball.setvy(playball.getVy() * SLOWDOWN_FACTOR);

        playball.setvx(Math.max(playball.getVx(), TARGET_SLOW_SPEED));
        playball.setvy(Math.max(playball.getVy(), TARGET_SLOW_SPEED));

        model.getBall().setFill(new ImagePattern(new Image("snowball.png")));

        long elapsedTime = time - snowBonusStartTime;
        if (elapsedTime >= SNOW_BONUS_DURATION) {
            // Snow bonus duration expired, reset the ball speed
            resetSnowBonus(playball);
        }
    }

    private void resetSnowBonus(Ball playball) {
        playball.setvx(1.0); // Reset to the original speed
        playball.setvy(1.0);
        isSnowBonusActive = false;
        model.getBall().setFill(new ImagePattern(new Image("ball.png")));
    }

    private void activateSnowBonus() {
        isSnowBonusActive = true;
        snowBonusStartTime = time;
    }
    private void updateSnowPosition(Bonus snow, long time) {
        snow.y += ((time - snow.timeCreated) / 1000.0) + 1.0;
    }

    private void handleCaughtChoco(Bonus choco) {
        System.out.println("You Got it and +3 score for you");
        choco.taken = true;
        choco.choco.setVisible(false);
        score += 3;
        new Score().show(choco.x, choco.y, 3, view);
    }
    @Override
    public void onTime(long time) {
        this.time = time;
    }
}
