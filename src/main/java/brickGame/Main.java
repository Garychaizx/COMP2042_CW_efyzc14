package brickGame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;


import java.io.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {


    private int level = 0;

    private double xBreak = 0.0f;
    private double centerBreakX;
    private double yBreak = 640.0f;

    private int breakWidth     = 130;
    private int breakHeight    = 30;
    private int halfBreakWidth = breakWidth / 2;

    private int sceneWidth = 500;
    private int sceneHeigt = 700;

    private static int LEFT  = 1;
    private static int RIGHT = 2;

    private Circle ball;
    private double xBall;
    private double yBall;

    private boolean isGoldStauts      = false;
    private boolean isExistHeartBlock = false;

    private Rectangle rect;
    private int       ballRadius = 10;

    private int destroyedBlockCount = 0;

//    private double v = 1.000;

    private int  heart    = 10000;
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
    public  Pane             root;
    private Label            scoreLabel;
    private Label            heartLabel;
    private Label            levelLabel;

    private boolean loadFromSave = false;
    private boolean isPaused = false;
    private boolean isGameRunning = false;

    Stage  primaryStage;
    Button load    = null;
    Button newGame = null;
    private Ball playball;
    private Break paddle;
    private Model model = new Model();
    private long savedTime;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        if (loadFromSave == false) {
            level++;
            if (level >1){
                new Score().showMessage("Level Up :)", this);
            }
            if (level == 18) {
                new Score().showWin(this);
                return;
            }

            playball=model.initBall();
            paddle=model.initBreak();
            model.initBoard(blocks,level,isExistHeartBlock);

            load = new Button("Load Game");
            newGame = new Button("Start New Game");
            load.setTranslateX(220);
            load.setTranslateY(300);
            newGame.setTranslateX(220);
            newGame.setTranslateY(340);

        }


        root = new Pane();
        scoreLabel = new Label("Score: " + score);
        levelLabel = new Label("Level: " + level);
        levelLabel.setTranslateY(20);
        heartLabel = new Label("Heart : " + heart);
        heartLabel.setTranslateX(sceneWidth - 70);
        if (loadFromSave == false) {
            root.getChildren().addAll(model.getRect(), model.getBall(), scoreLabel, heartLabel, levelLabel, newGame, load);
        } else {
            root.getChildren().addAll(model.getRect(), model.getBall(), scoreLabel, heartLabel, levelLabel);
        }
        for (Block block : blocks) {
            root.getChildren().add(block.rect);
        }
        Scene scene = new Scene(root, sceneWidth, sceneHeigt);
        scene.getStylesheets().add("style.css");
        scene.setOnKeyPressed(this);


        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        if (loadFromSave == false) {
            if (level > 1 && level < 18) {
                load.setVisible(false);
                newGame.setVisible(false);
                engine = new GameEngine();
                engine.setOnAction(this);
                engine.setFps(120);
                engine.start();
            }

            load.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    loadGame();

                    load.setVisible(false);
                    newGame.setVisible(false);
                }
            });

            newGame.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    engine = new GameEngine();
                    engine.setOnAction(Main.this);
                    engine.setFps(120);
                    engine.start();

                    load.setVisible(false);
                    newGame.setVisible(false);
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

//    private void initBoard() {
//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < level + 1; j++) {
//                int r = new Random().nextInt(500);
//                if (r % 5 == 0) {
//                    continue;
//                }
//                int type;
//                if (r % 10 == 1) {
//                    type = Block.BLOCK_CHOCO;
//                } else if (r % 10 == 2) {
//                    if (!isExistHeartBlock) {
//                        type = Block.BLOCK_HEART;
//                        isExistHeartBlock = true;
//                    } else {
//                        type = Block.BLOCK_NORMAL;
//                    }
//                } else if (r % 10 == 3) {
//                    type = Block.BLOCK_STAR;
//                } else if (r % 10 == 4) {
//                    type = Block.BLOCK_SNOW;
//                } else {
//                    type = Block.BLOCK_NORMAL;
//                }
//                blocks.add(new Block(j, i, colors[r % (colors.length)], type));
//                //System.out.println("colors " + r % (colors.length));
//            }
//        }
//    }


    public static void main(String[] args) {
        launch(args);
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
        new Score().showMessage("Game Paused", Main.this);
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
            new Score().showMessage("Game Continue", Main.this);
        }
    }

    private void togglePause() {
        isPaused = !isPaused;

        if (isPaused) {
            // Game is paused, stop the game engine and save the time state
            engine.stop();
            isGameRunning = false;
            savedTime = engine.getCurrentTime(); // Save the current time state
            new Score().showMessage("Game Paused", Main.this);
        } else {
            // Game is resumed, start or resume the game engine and restore the time state
            if (!isGameRunning) {
                engine = new GameEngine();
                engine.setOnAction(this);
                engine.setFps(120);
                engine.start();  // Note: You may need to initialize the engine before restoring the time state
                engine.restoreTimeState(savedTime); // Restore the saved time state
                isGameRunning = true;
                new Score().showMessage("Game Continue", Main.this);
            }
        }
    }



    float oldXBreak;

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


//    private void initBall() {
//        Random random = new Random();
//        yBall = random.nextInt(sceneHeigt - 200) + ((level + 1) * Block.getHeight()) + 15;
//        xBall = random.nextInt(sceneWidth) + 1;
//        xBall = xBreak + (breakWidth / 2);
//        yBall = yBreak - ballRadius - 40;
//        ball = new Circle();
//        ball.setRadius(ballRadius);
//        ball.setFill(new ImagePattern(new Image("ball.png")));
//        playball= new Ball(xBall,yBall,ballRadius);
//
//    }

//    private void initBreak() {
//        rect = new Rectangle();
//        rect.setWidth(breakWidth);
//        rect.setHeight(breakHeight);
//        rect.setX(xBreak);
//        rect.setY(yBreak);
//
//        ImagePattern pattern = new ImagePattern(new Image("block.jpg"));
//        paddle=new Break(xBreak,yBreak,breakWidth,breakHeight);
//        rect.setFill(pattern);
//    }


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


//    private void resetColideFlags() {
//
//        colideToBreak = false;
//        colideToBreakAndMoveToRight = false;
//        colideToRightWall = false;
//        colideToLeftWall = false;
//
//        colideToRightBlock = false;
//        colideToBottomBlock = false;
//        colideToLeftBlock = false;
//        colideToTopBlock = false;
//    }

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
            new Score().show(sceneWidth / 2, sceneHeigt / 2, -1, this);

            if (heart == 0) {
                new Score().showGameOver(this);
                engine.stop();
            }
        }
    }

//
//    private void handleBallMovement() {
//        if (colideToBreak) {
//            if (colideToBreakAndMoveToRight) {
//                playball.gorightball();
//            } else {
//                playball.goleftball();
//            }
//        } else if (colideToRightWall) {
//            playball.goleftball();
//        } else if (colideToLeftWall) {
//            playball.gorightball();
//        } else if (colideToRightBlock) {
//            playball.gorightball();
//        } else if (colideToLeftBlock) {
//            playball.goleftball();
//        } else if (colideToTopBlock) {
//            playball.goupball();
//        } else if (colideToBottomBlock) {
//            playball.godownball();
//        }
//    }



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

                    new Score().showMessage("Game Saved", Main.this);


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


        isExistHeartBlock = loadSave.isExistHeartBlock;
        isGoldStauts = loadSave.isGoldStauts;
//        isSnowStauts = loadSave.isSnowStauts;
        goDownBall = loadSave.goDownBall;
        goRightBall = loadSave.goRightBall;
        colideToBreak = loadSave.colideToBreak;
        colideToBreakAndMoveToRight = loadSave.colideToBreakAndMoveToRight;
        colideToRightWall = loadSave.colideToRightWall;
        colideToLeftWall = loadSave.colideToLeftWall;
        colideToRightBlock = loadSave.colideToRightBlock;
        colideToBottomBlock = loadSave.colideToBottomBlock;
        colideToLeftBlock = loadSave.colideToLeftBlock;
        colideToTopBlock = loadSave.colideToTopBlock;
        level = loadSave.level;
        score = loadSave.score;
        heart = loadSave.heart;
        destroyedBlockCount = loadSave.destroyedBlockCount;
        xBall = loadSave.xBall;
        yBall = loadSave.yBall;
        xBreak = loadSave.xBreak;
        yBreak = loadSave.yBreak;
        centerBreakX = loadSave.centerBreakX;
        time = loadSave.time;
        goldTime = loadSave.goldTime;
//        snowTime = loadSave.snowTime;
        vX = loadSave.vX;

        blocks.clear();
        chocos.clear();
        snows.clear();

        for (BlockSerializable ser : loadSave.blocks) {
            int r = new Random().nextInt(200);
            blocks.add(new Block(ser.row, ser.j, colors[r % colors.length], ser.type));
        }


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

//    private void nextLevel() {
//        if (!nextLevelInProgress) {
//            nextLevelInProgress = true;
//
//            Platform.runLater(() -> {
//                try {
//                    vX = 1.000;
//
//                    engine.stop();
//                    model.resetColideFlags();
//                    playball.godownball();
//
//                    isGoldStauts = false;
////                    isSnowStauts = false;
//                    isExistHeartBlock = false;
//
//                    hitTime = 0;
//                    time = 0;
//                    goldTime = 0;
////                    snowTime=0;
//
//                    engine.stop();
//                    blocks.clear();
//                    chocos.clear();
//                    snows.clear();
//                    destroyedBlockCount = 0;
//                    start(primaryStage);
//
//                    nextLevelInProgress = false;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//    }

    public void restartGame() {

        try {
            level = 0;
            heart = 3;
            score = 0;
            vX = 1.000;
            destroyedBlockCount = 0;
            model.resetColideFlags();
            playball.godownball();

            isGoldStauts = false;
//            isSnowStauts = false;
            isExistHeartBlock = false;
            hitTime = 0;
            time = 0;
            goldTime = 0;
//            snowTime=0;

            blocks.clear();
            chocos.clear();
            snows.clear();

            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpdate() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                scoreLabel.setText("Score: " + score);
                heartLabel.setText("Heart : " + heart);

                model.getRect().setX(paddle.getxbreak());
                model.getRect().setY(paddle.getybreak());
                model.getBall().setCenterX(playball.getx());
                model.getBall().setCenterY(playball.gety());

                for (Bonus choco : chocos) {
                    choco.choco.setY(choco.y);
                }
                for (Bonus snow : snows) {
                    snow.snow.setY(snow.y);
                }
            }
        });


        if (playball.gety() >= Block.getPaddingTop() && playball.gety() <= (Block.getHeight() * (level + 1)) + Block.getPaddingTop()) {
            for (final Block block : blocks) {
                double hitCode = block.checkHitToBlock(playball.getx(), playball.gety(), ballRadius);
                if (hitCode != Block.NO_HIT) {
                    score += 1;

                    new Score().show(block.x, block.y, 1, this);

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
                                root.getChildren().add(choco.choco);
                            }
                        });
                        chocos.add(choco);
                    }

                    if (block.type == Block.BLOCK_STAR) {
                        goldTime = time;
                        model.getBall().setFill(new ImagePattern(new Image("goldball.png")));
                        System.out.println("gold ball");
                        root.getStyleClass().add("goldRoot");
                        isGoldStauts = true;
                    }
                    if (block.type == Block.BLOCK_SNOW) {
                        final Bonus snow = new Bonus(block.row, block.column);
                        snow.timeCreated = time;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                root.getChildren().add(snow.snow);
                            }
                        });
                        snows.add(snow);

                    }

                    if (block.type == Block.BLOCK_HEART) {
                        heart++;
                    }

                    if (hitCode == Block.HIT_RIGHT) {
                        model.setColideToRightBlock(true);
                    } else if (hitCode == Block.HIT_BOTTOM) {
                        model.setColideToBottomBlock(true);
                    } else if (hitCode == Block.HIT_LEFT) {
                        model.setColideToLeftBlock(true);
                    } else if (hitCode == Block.HIT_TOP) {
                        model.setColideToTopBlock(true);
                    }

                }

                //TODO hit to break and some work here....
                //System.out.println("Break in row:" + block.row + " and column:" + block.column + " hit");
            }
        }
    }
    @Override
    public void onInit() {

    }
    private boolean isSnowBonusActive = false;
    private long snowBonusStartTime = 0;
    private double targetSlowSpeed = 0.3; // Set your desired slow speed
    private double slowdownFactor = 0.98; // Adjust the factor to control the slowdown rate
    private final long SNOW_BONUS_DURATION = 10000; // Duration of snow bonus in milliseconds (10 seconds)

    @Override
    public void onPhysicsUpdate() {
        checkDestroyedCount();
        setPhysicsToBall();
        if (isPaused) {
            return; // Skip physics update if the game is paused
        }

        //make the gold status call once
        // Check if the gold ball status is active and update the background
        if (isGoldStauts) {
            if (time - goldTime <= 5000) {
                model.getBall().setFill(new ImagePattern(new Image("goldball.png")));
                root.getStyleClass().add("goldRoot");
                root.setStyle("-fx-background-image: none;");
                System.out.println("called");
            } else {
                // Reset to normal ball and background after the gold ball duration
                model.getBall().setFill(new ImagePattern(new Image("ball.png")));
                root.getStyleClass().remove("goldRoot");
                root.setStyle("-fx-background-image: url('bg.jpg');");
                isGoldStauts = false;
                goldTime = 0; // Reset goldTime
                System.out.println("called");
            }
        }
        root.getStyleClass().remove("goldRoot");

        for (Bonus choco : chocos) {
            if (choco.y > sceneHeigt || choco.taken) {
                continue;
            }
            if (choco.y >= paddle.getybreak() && choco.y <= paddle.getybreak() + paddle.getbreakheight() && choco.x >= paddle.getxbreak() && choco.x <= paddle.getxbreak() + paddle.getbreakwidth()) {
                System.out.println("You Got it and +3 score for you");
                choco.taken = true;
                choco.choco.setVisible(false);
                score += 3;
                new Score().show(choco.x, choco.y, 3, this);
            }
            choco.y += ((time - choco.timeCreated) / 1000.000) + 1.000;
        }
        for (Bonus snow : snows) {
            if (snow.y > sceneHeigt || snow.taken) {
                continue;
            }
            if (snow.y >= paddle.getybreak() && snow.y <= paddle.getybreak() + paddle.getbreakwidth() && snow.x >= paddle.getxbreak() && snow.x <= paddle.getxbreak() + paddle.getbreakwidth()) {
                System.out.println("You Got a Penalty!(Ball will slow down for 10 seconds)");
                snow.taken = true;
                snow.snow.setVisible(false);
                activateSnowBonus(); // Activate snow bonus on collision
            }
            snow.y += ((time - snow.timeCreated) / 1000.000) + 1.000;
        }

        // Check for active snow bonus and update the ball speed
        if (isSnowBonusActive) {
            // Adjust the ball speed during the snow bonus
            playball.setvx(playball.getVx()*slowdownFactor);
            playball.setvy(playball.getVy()*slowdownFactor);
            // Ensure the speed doesn't go below the targetSlowSpeed
            playball.setvx(Math.max(playball.getVx(), targetSlowSpeed));
            playball.setvy(Math.max(playball.getVy(), targetSlowSpeed));
            model.getBall().setFill(new ImagePattern(new Image("snowball.png")));
            long elapsedTime = time - snowBonusStartTime;
            if (elapsedTime >= SNOW_BONUS_DURATION) {
                // Snow bonus duration expired, reset the ball speed
                playball.setvx(1.0); // Reset to the original speed
                playball.setvy(1.0);
                isSnowBonusActive = false;
                model.getBall().setFill(new ImagePattern(new Image("ball.png")));
            }
        }
    }
    private void activateSnowBonus() {
        isSnowBonusActive = true;
        snowBonusStartTime = time;
    }




    @Override
    public void onTime(long time) {
        this.time = time;
    }

}