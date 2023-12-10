package brickGame;


import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Optional;

/**
 * The View class represents the graphical user interface of the Brick Breaker game.
 * It includes methods for creating and updating the game scene, handling buttons, displaying messages,
 * and interacting with the user.
 */
public class View {
    private Button load=new Button("Load Game");
    private Button newGame=new Button("Start New Game");
    public  Pane             root;
    private Label            scoreLabel;
    private Label            heartLabel;
    private Label            levelLabel;
    private int  heart    = 3;
    private int sceneWidth = 500;
    private int sceneHeigt = 700;
    private int ballRadius = 10;
    private Rectangle rect;
    private Circle ball;
    private ArrayList<Block> blocks = new ArrayList<Block>();

    private boolean loadFromSave = false;
    Scene scene;
    private Model model = new Model();
    private Sound sound = new Sound();
    private Main main;
    private Controller controller;

    /**
     * Initializes the load and new game buttons and sets their initial positions.
     */
    public void loadButton() {
        load = new Button("Load Game");
        newGame = new Button("Start New Game");
        load.setTranslateX(220);
        load.setTranslateY(300);
        newGame.setTranslateX(220);
        newGame.setTranslateY(340);
    }

    /**
     * Creates a new ball with a specified radius and fills it with the default ball image.
     *
     * @return The created `Circle` object representing the game ball.
     */
    public Circle createBall() {
        ball = new Circle();
        ball.setRadius(ballRadius);
        ball.setFill(new ImagePattern(new Image("ball.png")));
        return ball;
    }

    /**
     * Creates a new rectangle representing the game paddle and fills it with the default block image.
     *
     * @return The created `Rectangle` object representing the game paddle.
     */
    public Rectangle createRect(){
        rect = new Rectangle();
        rect.setWidth(model.getBreakWidth());
        rect.setHeight(model.getBreakHeight());
        rect.setX(model.getxBreak());
        rect.setY(model.getyBreak());
        ImagePattern pattern = new ImagePattern(new Image("block.jpg"));
        rect.setFill(pattern);
        return rect;
    }

    /**
     * Sets up the game scene with the specified model, block elements, and initial labels.
     *
     * @param model         The `Model` object representing the game state.
     * @param blocks        The list of `Block` objects representing the game blocks.
     * @param primaryStage  The primary stage of the application.
     * @param score         The current game score.
     * @param level         The current game level.
     */
    public void setScene(Model model, ArrayList<Block> blocks, Stage primaryStage,int score,int level){
        root = new Pane();
        scoreLabel = new Label("Score: " + score);
        levelLabel = new Label("Level: " + level);
        levelLabel.setTranslateY(20);
        heartLabel = new Label("Heart : " + heart);
        heartLabel.setTranslateX(sceneWidth - 70);
        if (loadFromSave == false && model.getLevel()!= 19) {
            root.getChildren().addAll(model.getRect(), model.getBall(), scoreLabel, heartLabel, levelLabel, newGame, load);
        } else {
            root.getChildren().addAll(model.getRect(), model.getBall(), scoreLabel, heartLabel, levelLabel);
        }
        for (Block block : blocks) {
            root.getChildren().add(block.rect);
        }
        Scene scene = new Scene(root, sceneWidth, sceneHeigt);
        scene.getStylesheets().add("style.css");

        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Hides the load and new game buttons.
     */
    public void hideButtons(){
        load.setVisible(false);
        newGame.setVisible(false);
    }

    /**
     * Displays a heart decrease animation when the player loses a life.
     */
    public void minusHeart(){
        new Score().show(sceneWidth / 2, sceneHeigt / 2, -1, this);
    }

    /**
     * Updates the score and heart labels on the game screen.
     *
     * @param score The current game score.
     * @param heart The current number of remaining lives.
     */
    public void showLabel(int score,int heart){
        scoreLabel.setText("Score: " + score);
        heartLabel.setText("Heart : " + heart);
    }

    /**
     * Activates the gold state, changing the ball image and background.
     */
    public void GoldState(){
        ball.setFill(new ImagePattern(new Image("goldball.png")));
        System.out.println("gold ball");
        root.setStyle("-fx-background-image: none;");
        root.getStyleClass().add("goldRoot");
    }

    /**
     * Removes the gold state, resetting the ball image and background.
     */
    public void RemoveGoldState(){
        ball.setFill(new ImagePattern(new Image("ball.png")));
        root.getStyleClass().remove("goldRoot");
        root.setStyle("-fx-background-image: url('bg.jpg');");
    }

    /**
     * Handles the caught chocolate bonus, plays a sound, updates the score, and displays a score animation.
     *
     * @param choco The `Bonus` object representing the chocolate bonus.
     * @param score The current game score.
     * @return The updated game score.
     */
    public int handleCaughtChoco(Bonus choco,int score) {
        sound.playBonusSound();
        System.out.println("You Got it and +3 score for you");
        choco.taken = true;
        choco.choco.setVisible(false);
        model.setScore(score+3);
        new Score().show(choco.x, choco.y, 3, this);
        return model.getScore();
    }

    /**
     * Displays a score animation for a block hit.
     *
     * @param block The `Block` object that was hit.
     */
    public void addScore(Block block){
        new Score().show(block.x, block.y, 1, this);
    }


    //show message

    /**
     * Displays a message for leveling up.
     */
    public void showLevelUpMsg(){
        new Score().showMessage("Level Up :)", this);
    }

    /**
     * Displays a message for pausing the game.
     */
    public void showPauseMsg(){
        new Score().showMessage("Game Paused", View.this);
    }

    /**
     * Displays a message for continuing the game.
     */
    public void showContinueMsg(){
        new Score().showMessage("Game Continue", View.this);
    }

    /**
     * Displays a message for saving the game.
     */
    public void showSavedMsg(){
        new Score().showMessage("Game Saved", View.this);
    }

    /**
     * Displays a message for entering a bonus level.
     */
    public void showBonusLevelMsg(){new Score().showMessage("Bonus Level",this);}

    /**
     * Displays the game over message and prompts the user to quit or restart the game.
     */
    public void showGameOverMsg(Controller controller){
        new Score().showGameOver(this, controller);
    }

    /**
     * Displays the win message.
     */
    public void showWinMsg(){
        new Score().showWin(this);
    }

    //getter
    public Button getLoad() {
        return load;
    }

    public Button getNewGame() {
        return newGame;
    }
    public Scene getScene(){
        return scene;
    }
    public Pane getRoot(){
        return root;
    }

}