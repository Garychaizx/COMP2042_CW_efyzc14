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

public class View {
    private Button load=new Button("Load Game");
    private Button newGame=new Button("Start New Game");
    public  Pane             root;
    private Label            scoreLabel;
    private Label            heartLabel;
    private Label            levelLabel;
    private int  heart    = 3;
    //    private int  score    = 0;
//    private int level = 0;
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

    public void loadButton() {
        load = new Button("Load Game");
        newGame = new Button("Start New Game");
        load.setTranslateX(220);
        load.setTranslateY(300);
        newGame.setTranslateX(220);
        newGame.setTranslateY(340);
    }

    public Circle createBall() {
        ball = new Circle();
        ball.setRadius(ballRadius);
        ball.setFill(new ImagePattern(new Image("ball.png")));
        return ball;
    }
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

    public void setScene(Model model, ArrayList<Block> blocks, Stage primaryStage,int score,int level){
        root = new Pane();
        scoreLabel = new Label("Score: " + score);
        levelLabel = new Label("Level: " + level);//need to getlevel from controller afterwards
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
    public void hideButtons(){
        load.setVisible(false);
        newGame.setVisible(false);
    }
    public void minusHeart(){
        new Score().show(sceneWidth / 2, sceneHeigt / 2, -1, this);
    }
    public void showLabel(int score,int heart){
        scoreLabel.setText("Score: " + score);
        heartLabel.setText("Heart : " + heart);
    }
    public void GoldState(){
        ball.setFill(new ImagePattern(new Image("goldball.png")));
        System.out.println("gold ball");
        root.setStyle("-fx-background-image: none;");
        root.getStyleClass().add("goldRoot");
    }
    public void RemoveGoldState(){
        ball.setFill(new ImagePattern(new Image("ball.png")));
        root.getStyleClass().remove("goldRoot");
        root.setStyle("-fx-background-image: url('bg.jpg');");
    }
    public int handleCaughtChoco(Bonus choco,int score) {
        sound.playBonusSound();
        System.out.println("You Got it and +3 score for you");
        choco.taken = true;
        choco.choco.setVisible(false);
        model.setScore(score+3);
        new Score().show(choco.x, choco.y, 3, this);
        return model.getScore();
    }
    public void addScore(Block block){
        new Score().show(block.x, block.y, 1, this);
    }


    //show message
    public void showLevelUpMsg(){
        new Score().showMessage("Level Up :)", this);
    }
    public void showPauseMsg(){
        new Score().showMessage("Game Paused", View.this);
    }
    public void showContinueMsg(){
        new Score().showMessage("Game Continue", View.this);
    }
    public void showSavedMsg(){
        new Score().showMessage("Game Saved", View.this);
    }
    public void showBonusLevelMsg(){new Score().showMessage("Bonus Level",this);}
    public void showGameOverMsg(Controller controller){
        new Score().showGameOver(this, controller);
    }
    public void showWinMsg(){
        new Score().showWin(this);
    }
    public Optional<ButtonType> showQuitWindow(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit Game");
        alert.setHeaderText("Are you sure you want to quit?");
        alert.setContentText("Any unsaved progress will be lost.");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }

    //getter

    public Main getMain(){
        return main;
    }
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

    public Label getScoreLabel() {
        return scoreLabel;
    }
    public Label getHeartLabel(){
        return  heartLabel;
    }

}