package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;


/**
 * The `Model` class represents the game model in the Brick Breaker game. It encapsulates the game's state,
 * including the positions of the ball, paddle, blocks, bonuses, and various game parameters.
 * It also provides methods to initialize the game elements, handle collisions, and update the game state.
 *
 * This class includes functionality related to ball movement, block handling, paddle interactions, and bonus effects.
 *
 * The game model is responsible for maintaining information such as the player's score, remaining lives (hearts),
 * the current game level, and the state of various game flags.
 *
 * Methods are provided for initializing the game elements, handling collisions with walls, paddles, and blocks,
 * updating ball movement, and resetting the game state.
 *
 * Additionally, there are methods to manage bonuses, such as choco and snow bonuses, and to handle their effects.
 *
 * The class also includes getter and setter methods for accessing and modifying the state of the game model.
 *
 * @author Chai Ze Xuan
 */
public class Model {

    private Circle ball;
    private double xBall;
    private double yBall;
    private double xBreak =0.0f;
    private double yBreak = 640.0f ;
    private int breakWidth = 130;
    private int breakHeight    = 30;
    private int sceneHeigt = 700;
    private int ballRadius = 10;
    private int level = 0;
    private long goldTime = 0;
    private int destroyedBlockCount = 0;
    private ArrayList<Block> blocks = new ArrayList<Block>();
    private ArrayList<Bonus> chocos = new ArrayList<Bonus>();
    private ArrayList<Bonus> snows = new ArrayList<Bonus>();
    private Rectangle rect;
    private boolean isExistHeartBlock = false;
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
    private boolean isGoldStauts      = false;
    private long time     = 0;
    private long hitTime  = 0;
    private int  heart    = 3;
    private int score;
    private Sound sound = new Sound();

    /**
     * Initializes the ball at the start of the game.
     *
     * @return A `Ball` object representing the initialized ball.
     */
    public Ball initBall() {
        getBall();
        xBall = xBreak + (breakWidth / 2);
        yBall = yBreak - ballRadius - 40;
        return new Ball(xBall,yBall,ballRadius);
    }

    /**
     * Initializes the paddle (break) at the start of the game.
     *
     * @return A `Break` object representing the initialized paddle.
     */
    public Break initBreak() {
        getRect();
        return new Break(xBreak,yBreak,breakWidth,breakHeight);
    }

    /**
     * Initializes the game board with blocks based on the current level and other parameters.
     *
     * @param blocks             The list to store the initialized blocks.
     * @param level              The current game level.
     * @param isExistHeartBlock  A flag indicating whether a heart block exists.
     */
    public void initBoard(ArrayList<Block> blocks,int level,boolean isExistHeartBlock) {
        if (level == 18){
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 10; j++) {
                    int r = new Random().nextInt(500);
                    if (r % 5 == 0) {
                        continue;
                    }
                    int type;
                    if (r % 10 == 1) {
                        type = Block.BLOCK_CHOCO;
                    } else if (r % 10 == 2) {
                        if (!isExistHeartBlock) {
                            type = Block.BLOCK_HEART;
                            isExistHeartBlock = true;
                        } else {
                            type = Block.BLOCK_SNOW;
                        }
                    } else if (r % 10 == 3) {
                        type = Block.BLOCK_STAR;
                    } else {
                        type = Block.BLOCK_CHOCO;
                    }
                    blocks.add(new Block(j, i, colors[r % (colors.length)], type));
                    //System.out.println("colors " + r % (colors.length));
                }
            }
        }
        else {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < level + 1; j++) {
                    int r = new Random().nextInt(500);
                    if (r % 5 == 0) {
                        continue;
                    }
                    int type;
                    if (r % 10 == 1) {
                        type = Block.BLOCK_CHOCO;
                    } else if (r % 10 == 2) {
                        if (!isExistHeartBlock) {
                            type = Block.BLOCK_HEART;
                            isExistHeartBlock = true;
                        } else {
                            type = Block.BLOCK_NORMAL;
                        }
                    } else if (r % 10 == 3) {
                        type = Block.BLOCK_STAR;
                    } else if (r % 10 == 4) {
                        type = Block.BLOCK_SNOW;
                    } else {
                        type = Block.BLOCK_NORMAL;
                    }
                    blocks.add(new Block(j, i, colors[r % (colors.length)], type));
                    //System.out.println("colors " + r % (colors.length));
                }
            }
        }
    }

    /**
     * Handles collisions with the walls of the game scene.
     *
     * @param playball    The `Ball` object representing the game ball.
     * @param sceneWidth  The width of the game scene.
     */
    public void handleWallCollisions(Ball playball,int sceneWidth) {
        if (playball.gety() <= 0) {
            handleTopWallCollision(playball);
        } else if (playball.gety() >= sceneHeigt) {
            handleBottomWallCollision(playball);
        } else if (playball.getx() >= sceneWidth) {
            handleRightWallCollision(playball);
        } else if (playball.getx() <= 0) {
            handleLeftWallCollision(playball);
        }
    }

    /**
     * Handles the collision between the ball and the top wall of the game scene.
     * Resets collision flags and makes the ball move downward.
     *
     * @param playball The `Ball` object representing the game ball.
     */
    public void handleTopWallCollision(Ball playball) {
        resetColideFlags();
        playball.godownball();
    }

    /**
     * Handles the collision between the ball and the bottom wall of the game scene.
     * Resets collision flags and makes the ball move upward.
     *
     * @param playball The `Ball` object representing the game ball.
     */
    public void handleBottomWallCollision(Ball playball) {
        resetColideFlags();
        playball.goupball();
    }

    /**
     * Handles the collision between the ball and the right wall of the game scene.
     * Resets collision flags and sets the flag indicating a collision with the right wall.
     *
     * @param playball The `Ball` object representing the game ball.
     */
    public void handleRightWallCollision(Ball playball) {
        resetColideFlags();
        colideToRightWall = true;
    }

    /**
     * Handles the collision between the ball and the left wall of the game scene.
     * Resets collision flags and sets the flag indicating a collision with the left wall.
     *
     * @param playball The `Ball` object representing the game ball.
     */
    public void handleLeftWallCollision(Ball playball) {
        resetColideFlags();
        colideToLeftWall = true;
    }

    /**
     * Handles the collision between the ball and the paddle (break) of the game scene.
     * If the ball hits the paddle, adjusts its velocity and sets the appropriate flags.
     *
     * @param playball The `Ball` object representing the game ball.
     * @param paddle   The `Break` object representing the game paddle.
     */
    public void handlePaddleCollision(Ball playball,Break paddle) {
        if (playball.gety() >= paddle.getybreak() - ballRadius) {
            if (playball.getx() >= paddle.getxbreak() && playball.getx() <= paddle.getxbreak() + paddle.getbreakwidth()) {
                handlePaddleHit(playball,paddle);
            }
        }
    }

    /**
     * Handles the hit of the ball on the paddle (break).
     * Adjusts the ball's velocity based on the hit position on the paddle.
     *
     * @param playball The `Ball` object representing the game ball.
     * @param paddle   The `Break` object representing the game paddle.
     */
    public void handlePaddleHit(Ball playball,Break paddle) {
        hitTime = time;
        resetColideFlags();
        colideToBreak = true;
        playball.goupball();

        double relation = (playball.getx() - paddle.getcenterbreakX()) / (paddle.getbreakwidth() / 2);

        if (Math.abs(relation) <= 0.3) {
            playball.setvx(Math.abs(relation));
        } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
            playball.setvx((Math.abs(relation) * 1.5) + (level / 3.500));
        } else {
            playball.setvx((Math.abs(relation) * 2) + (level / 3.500));
        }

        colideToBreakAndMoveToRight = playball.getx() - paddle.getcenterbreakX() > 0;
    }

    /**
     * Handles the movement of the ball based on collision flags.
     * Adjusts the ball's movement direction according to the collision state.
     *
     * @param playball The `Ball` object representing the game ball.
     */
    public void handleBallMovement(Ball playball) {
        if (colideToBreak) {
            if (colideToBreakAndMoveToRight) {
                playball.gorightball();
            } else {
                playball.goleftball();
            }
        } else if (colideToRightWall) {
            playball.goleftball();
        } else if (colideToLeftWall) {
            playball.gorightball();
        } else if (colideToRightBlock) {
            playball.gorightball();
        } else if (colideToLeftBlock) {
            playball.goleftball();
        } else if (colideToTopBlock) {
            playball.goupball();
        } else if (colideToBottomBlock) {
            playball.godownball();
        }
    }

    /**
     * Resets all collision flags to their initial state.
     */
    public void resetColideFlags() {

        colideToBreak = false;
        colideToBreakAndMoveToRight = false;
        colideToRightWall = false;
        colideToLeftWall = false;

        colideToRightBlock = false;
        colideToBottomBlock = false;
        colideToLeftBlock = false;
        colideToTopBlock = false;
    }

    /**
     * Resets various game parameters to their initial state.
     *
     * @param playball The `Ball` object representing the game ball.
     */
    public void resetGameParameters(Ball playball) {
        playball.setvx(1.000);
        resetColideFlags();
        playball.godownball();
        isGoldStauts = false;
        isExistHeartBlock = false;
        hitTime = 0;
        time = 0;
        goldTime = 0;
    }

    /**
     * Clears all game elements, including blocks, chocos, and snow bonuses.
     */
    public void clearGameElements() {
        blocks.clear();
        chocos.clear();
        snows.clear();
        destroyedBlockCount = 0;
    }

    /**
     * Resets the game to its initial state, clearing elements and setting default values.
     *
     * @param playball The `Ball` object representing the game ball.
     */
    public void resetGame(Ball playball) {
        level = 0;
        heart = 3;
        score = 0;
        playball.setvx(1.000);
        destroyedBlockCount = 0;
        resetColideFlags();
        playball.godownball();
        isGoldStauts = false;
        isExistHeartBlock = false;
        hitTime = 0;
        time = 0;
        goldTime = 0;
        blocks.clear();
        chocos.clear();
        snows.clear();
    }

    /**
     * Sets the game state based on the provided `LoadSave` object.
     *
     * @param loadSave The `LoadSave` object containing the saved game state.
     * @param playball The `Ball` object representing the game ball.
     * @param paddle   The `Break` object representing the game paddle.
     */
    public void setGameState(LoadSave loadSave,Ball playball,Break paddle){
        this.setExistHeartBlock(loadSave.isExistHeartBlock);
        this.setIsGoldStauts(loadSave.isGoldStauts);
        this.setGoDownBall(loadSave.goDownBall);
        this.setGoRightBall(loadSave.goRightBall);
        this.setColideToBreak(loadSave.colideToBreak);
        this.setColideToBreakAndMoveToRight(loadSave.colideToBreakAndMoveToRight);
        this.setColideToRightWall(loadSave.colideToRightWall);
        this.setColideToLeftWall(loadSave.colideToLeftWall);
        this.setColideToRightBlock(loadSave.colideToRightBlock);
        this.setColideToBottomBlock(loadSave.colideToBottomBlock);
        this.setColideToLeftBlock(loadSave.colideToLeftBlock);
        this.setColideToTopBlock(loadSave.colideToTopBlock);
        this.setLevel(loadSave.level);
        this.setScore(loadSave.score);
        this.setHeart(loadSave.heart);
        this.setDestroyedBlockCount(loadSave.destroyedBlockCount);
        this.setXBall(loadSave.xBall);
        this.setYBall(loadSave.yBall);
        this.setXBreak(loadSave.xBreak);
        this.setYBreak(loadSave.yBreak);
        this.setTime(loadSave.time);
        this.setGoldTime(loadSave.goldTime);
        playball.setvx(loadSave.vX);
        paddle.setcenterbreakx(loadSave.centerBreakX);
    }

    /**
     * Restores the blocks from the provided `LoadSave` object and populates the blocks list.
     *
     * @param loadSave The `LoadSave` object containing block information.
     * @param blocks   The list to store the initialized blocks.
     */
    public void restoreBlocks(LoadSave loadSave,ArrayList<Block> blocks){
        blocks.clear();
        chocos.clear();
        for (BlockSerializable ser : loadSave.blocks) {
            int r = new Random().nextInt(200);
            blocks.add(new Block(ser.row, ser.j, colors[r % colors.length], ser.type));
        }
    }

    /**
     * Clears the penalty applied to the ball, restoring its original speed and appearance.
     *
     * @param playball The `Ball` object representing the game ball.
     */
    public void clearPenalty(Ball playball) {
        playball.setvx(1.0); // Reset to the original speed
        playball.setvy(1.0);
        ball.setFill(new ImagePattern(new Image("ball.png")));
        isSnowBonusActive = false;
        snowBonusStartTime = 0;
    }

    /**
     * Sets the initial positions of the ball and paddle on the game scene.
     *
     * @param playball The `Ball` object representing the game ball.
     * @param paddle   The `Break` object representing the game paddle.
     */
    public void setBallPaddle(Ball playball,Break paddle){
        getRect().setX(paddle.getxbreak());
        getRect().setY(paddle.getybreak());
        getBall().setCenterX(playball.getx());
        getBall().setCenterY(playball.gety());
    }

    /**
     * Sets the collision state based on the provided hit code.
     *
     * @param hitCode The hit code indicating the collision location.
     */
    public void setCollision(double  hitCode){
        if (hitCode == Block.HIT_RIGHT) {
            setColideToRightBlock(true);
        } else if (hitCode == Block.HIT_BOTTOM) {
            setColideToBottomBlock(true);
        } else if (hitCode == Block.HIT_LEFT) {
            setColideToLeftBlock(true);
        } else if (hitCode == Block.HIT_TOP) {
            setColideToTopBlock(true);
        }
    }

    /**
     * Checks if the choco bonus should be skipped (considered out of bounds or already taken).
     *
     * @param choco The `Bonus` object representing the choco bonus.
     * @return `true` if the choco bonus should be skipped, `false` otherwise.
     */
    public boolean shouldSkipChoco(Bonus choco) {
        return choco.y > sceneHeigt || choco.taken;
    }


    /**
     * Checks if the choco bonus is caught by the paddle.
     *
     * @param choco  The `Bonus` object representing the choco bonus.
     * @param paddle The `Break` object representing the game paddle.
     * @return `true` if the choco bonus is caught, `false` otherwise.
     */
    public boolean isChocoCaught(Bonus choco,Break paddle) {
        return choco.y >= paddle.getybreak() && choco.y <= paddle.getybreak() + paddle.getbreakheight()
                && choco.x >= paddle.getxbreak() && choco.x <= paddle.getxbreak() + paddle.getbreakwidth();
    }

    /**
     * Updates the position of the choco bonus based on elapsed time.
     *
     * @param choco The `Bonus` object representing the choco bonus.
     * @param time  The elapsed time since the bonus was created.
     */
    public void updateChocoPosition(Bonus choco,long time) {
        choco.y += ((time - choco.timeCreated) / 1000.000) + 1.000;
    }
    private static final double TARGET_SLOW_SPEED = 0.3;
    private static final double SLOWDOWN_FACTOR = 0.98;
    private static final long SNOW_BONUS_DURATION = 10000;

    private boolean isSnowBonusActive = false;
    private long snowBonusStartTime = 0;

    /**
     * Handles collisions with the snow bonus, applying a penalty to the ball.
     *
     * @param snow   The `Bonus` object representing the snow bonus.
     * @param playball The `Ball` object representing the game ball.
     * @param paddle The `Break` object representing the paddle.
     * @param time   The current time in the game.
     */
    public void handleSnowBonusCollision(Bonus snow, Ball playball, Break paddle, long time) {
        if (snow.y > sceneHeigt || snow.taken) {
            return;
        }

        if (isSnowCaught(snow, paddle)) {
            System.out.println("You Got a Penalty! (Ball will slow down for 10 seconds)");
            snow.taken = true;
            snow.snow.setVisible(false);
            activateSnowBonus(time); // Activate snow bonus on collision
        }
    }

    /**
     * Checks if the snow bonus is caught by the paddle.
     *
     * @param snow   The `Bonus` object representing the snow bonus.
     * @param paddle The `Break` object representing the game paddle.
     * @return `true` if the snow bonus is caught, `false` otherwise.
     */
    private boolean isSnowCaught(Bonus snow, Break paddle) {
        return snow.y >= paddle.getybreak() && snow.y <= paddle.getybreak() + paddle.getbreakwidth()
                && snow.x >= paddle.getxbreak() && snow.x <= paddle.getxbreak() + paddle.getbreakwidth();
    }

    /**
     * Applies the snow bonus to the ball, slowing down its speed.
     * Updates the ball's velocity, sets a new image for the ball, and tracks the duration of the bonus.
     *
     * @param playball The `Ball` object representing the game ball.
     * @param time     The current game time.
     */
    public void applySnowBonus(Ball playball, long time) {
        playball.setvx(playball.getVx() * SLOWDOWN_FACTOR);
        playball.setvy(playball.getVy() * SLOWDOWN_FACTOR);

        playball.setvx(Math.max(playball.getVx(), TARGET_SLOW_SPEED));
        playball.setvy(Math.max(playball.getVy(), TARGET_SLOW_SPEED));

        ball.setFill(new ImagePattern(new Image("snowball.png")));

        long elapsedTime = time - snowBonusStartTime;
        if (elapsedTime >= SNOW_BONUS_DURATION) {
            // Snow bonus duration expired, reset the ball speed
            resetSnowBonus(playball);
        }
    }

    /**
     * Resets the snow bonus applied to the ball.
     * Restores the original speed, deactivates the snow bonus, and resets the ball image.
     *
     * @param playball The `Ball` object representing the game ball.
     */
    private void resetSnowBonus(Ball playball) {
        playball.setvx(1.0); // Reset to the original speed
        playball.setvy(1.0);
        isSnowBonusActive = false;
        ball.setFill(new ImagePattern(new Image("ball.png")));
    }

    /**
     * Activates the snow bonus, playing a sound and setting the bonus duration start time.
     *
     * @param time The current game time.
     */
    private void activateSnowBonus(long time) {
        sound.playSnowBallSound();
        isSnowBonusActive = true;
        snowBonusStartTime = time;
    }

    /**
     * Updates the position of the snow bonus based on elapsed time.
     *
     * @param snow The `Bonus` object representing the snow bonus.
     * @param time The elapsed time since the bonus was created.
     */
    public void updateSnowPosition(Bonus snow, long time) {
        snow.y += ((time - snow.timeCreated) / 1000.0) + 1.0;
    }


    //getter
    /**
     * Gets the height of the break in the game.
     *
     * @return The height of the break.
     */
    public int getBreakHeight() {
        return breakHeight;
    }

    /**
     * Gets the width of the break in the game.
     *
     * @return The width of the break.
     */
    public int getBreakWidth(){
        return breakWidth;
    }

    /**
     * Gets the x-coordinate of the break in the game.
     *
     * @return The x-coordinate of the break.
     */
    public double getxBreak() {
        return xBreak;
    }

    /**
     * Gets the y-coordinate of the break in the game.
     *
     * @return The y-coordinate of the break.
     */
    public double getyBreak() {
        return yBreak;
    }

    /**
     * Gets the remaining heart count in the game.
     *
     * @return The remaining heart count.
     */
    public int getHeart(){return heart;}

    /**
     * Gets the current score in the game.
     *
     * @return The current score.
     */
    public int getScore(){return score;}

    /**
     * Gets the current level in the game.
     *
     * @return The current level.
     */
    public int getLevel(){return level;}

    /**
     * Gets the ball object in the game.
     *
     * @return The ball object.
     */
    public Circle getBall() {
        return ball;
    }

    /**
     * Gets the rectangle object in the game.
     *
     * @return The rectangle object.
     */
    public Rectangle getRect() {return rect;}

    /**
     * Checks if the snow bonus is active in the game.
     *
     * @return True if the snow bonus is active, false otherwise.
     */
    public boolean getisSnowBonusActive(){return isSnowBonusActive;}

    //setter

    /**
     * Sets the ball in the game.
     *
     * @param ball The ball to be set in the game.
     */
    public void setBall(Circle ball){
        this.ball=ball;
    }

    /**
     * Sets the rectangle in the game.
     *
     * @param rect The rectangle to be set in the game.
     */
    public void setRect(Rectangle rect){
        this.rect=rect;
    }

    /**
     * Sets the status of the gold bonus in the game.
     *
     * @param isGoldStauts True if the gold bonus is active, false otherwise.
     */
    public void setIsGoldStauts(boolean isGoldStauts) {
        this.isGoldStauts = isGoldStauts;
    }

    /**
     * Sets the movement direction of the ball to go down in the game.
     *
     * @param goDownBall True if the ball should move down, false otherwise.
     */

    public void setGoDownBall(boolean goDownBall) {
        this.goDownBall = goDownBall;
    }

    /**
     * Sets the movement direction of the ball to go right in the game.
     *
     * @param goRightBall True if the ball should move right, false otherwise.
     */
    public void setGoRightBall(boolean goRightBall) {
        this.goRightBall = goRightBall;
    }

    /**
     * Sets the current level in the game.
     *
     * @param level The level to be set.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Sets the current score in the game.
     *
     * @param score The score to be set.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Sets the remaining heart count in the game.
     *
     * @param heart The remaining heart count to be set.
     */
    public void setHeart(int heart) {
        this.heart = heart;
    }

    /**
     * Sets the count of destroyed blocks in the game.
     *
     * @param destroyedBlockCount The count of destroyed blocks to be set.
     */
    public void setDestroyedBlockCount(int destroyedBlockCount) {
        this.destroyedBlockCount = destroyedBlockCount;
    }

    /**
     * Sets the x-coordinate of the ball in the game.
     *
     * @param xBall The x-coordinate of the ball to be set.
     */
    public void setXBall(double xBall) {
        this.xBall = xBall;
    }

    /**
     * Sets the y-coordinate of the ball in the game.
     *
     * @param yBall The y-coordinate of the ball to be set.
     */
    public void setYBall(double yBall) {
        this.yBall = yBall;
    }

    /**
     * Sets the x-coordinate of the break in the game.
     *
     * @param xBreak The x-coordinate of the break to be set.
     */
    public void setXBreak(double xBreak) {
        this.xBreak = xBreak;
    }

    /**
     * Sets the y-coordinate of the break in the game.
     *
     * @param yBreak The y-coordinate of the break to be set.
     */
    public void setYBreak(double yBreak) {
        this.yBreak = yBreak;
    }

    /**
     * Sets the elapsed time in the game.
     *
     * @param time The elapsed time to be set.
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Sets the remaining time for the gold bonus in the game.
     *
     * @param goldTime The remaining time for the gold bonus to be set.
     */
    public void setGoldTime(long goldTime) {
        this.goldTime = goldTime;
    }

    /**
     * Sets the existence status of the heart block in the game.
     *
     * @param isExistHeartBlock True if the heart block exists, false otherwise.
     */
    public void setExistHeartBlock(boolean isExistHeartBlock){this.isExistHeartBlock=isExistHeartBlock;}

    /**
     * Sets the collision status with a regular block in the game.
     *
     * @param colideToBreak True if a collision with a regular block occurred, false otherwise.
     */
    public void setColideToBreak(boolean colideToBreak) {
        this.colideToBreak = colideToBreak;
    }

    /**
     * Sets the collision status with a regular block and movement to the right in the game.
     *
     * @param colideToBreakAndMoveToRight True if a collision with a block and movement to the right occurred, false otherwise.
     */
    public void setColideToBreakAndMoveToRight(boolean colideToBreakAndMoveToRight) {
        this.colideToBreakAndMoveToRight = colideToBreakAndMoveToRight;
    }

    /**
     * Sets the collision status with the right wall in the game.
     *
     * @param colideToRightWall True if a collision with the right wall occurred, false otherwise.
     */
    public void setColideToRightWall(boolean colideToRightWall) {
        this.colideToRightWall = colideToRightWall;
    }

    /**
     * Sets the collision status with the left wall in the game.
     *
     * @param colideToLeftWall True if a collision with the left wall occurred, false otherwise.
     */
    public void setColideToLeftWall(boolean colideToLeftWall) {
        this.colideToLeftWall = colideToLeftWall;
    }

    /**
     * Sets the collision status with a block on the right side in the game.
     *
     * @param colideToRightBlock True if a collision with a block on the right side occurred, false otherwise.
     */
    public void setColideToRightBlock(boolean colideToRightBlock) {
        this.colideToRightBlock = colideToRightBlock;
    }

    /**
     * Sets the collision status with a block on the left side in the game.
     *
     * @param colideToLeftBlock True if a collision with a block on the left side occurred, false otherwise.
     */
    public void setColideToLeftBlock(boolean colideToLeftBlock) {
        this.colideToLeftBlock = colideToLeftBlock;
    }

    /**
     * Sets the collision status with the bottom of a block in the game.
     *
     * @param colideToBottomBlock True if a collision with the bottom of a block occurred, false otherwise.
     */
    public void setColideToBottomBlock(boolean colideToBottomBlock) {
        this.colideToBottomBlock = colideToBottomBlock;
    }

    /**
     * Sets the collision status with the top of a block in the game.
     *
     * @param colideToTopBlock True if a collision with the top of a block occurred, false otherwise.
     */
    public void setColideToTopBlock(boolean colideToTopBlock) {
        this.colideToTopBlock = colideToTopBlock;
    }
}