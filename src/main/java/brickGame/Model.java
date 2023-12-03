package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class Model {

    private Circle ball;
    private double xBall;
    private double yBall;
    private double xBreak =0.0f;
    private double yBreak = 640.0f ;
    private int breakWidth = 130;
    private int breakHeight    = 30;
    private int sceneWidth = 500;
    private int sceneHeigt = 700;
    private int ballRadius = 10;
    private int level = 0;
    private long goldTime = 0;
    private int destroyedBlockCount = 0;
    private ArrayList<Block> blocks = new ArrayList<Block>();
    private ArrayList<Bonus> chocos = new ArrayList<Bonus>();
    private ArrayList<Bonus> snows = new ArrayList<Bonus>();
    private Ball playball;
    private Break paddle;

    private Main main;
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
//    private View view = new View();

    public Ball initBall() {
        getBall();
        xBall = xBreak + (breakWidth / 2);
        yBall = yBreak - ballRadius - 40;
        return new Ball(xBall,yBall,ballRadius);
    }
    public Break initBreak() {
        getRect();
        return new Break(xBreak,yBreak,breakWidth,breakHeight);
    }
    public void initBoard(ArrayList<Block> blocks,int level,boolean isExistHeartBlock) {
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
    public void initBonus(ArrayList<Block> blocks) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < level + 14; j++) {
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

    public void handleTopWallCollision(Ball playball) {
        resetColideFlags();
        playball.godownball();
    }

    public void handleBottomWallCollision(Ball playball) {
        resetColideFlags();
        playball.goupball();
    }

    public void handleRightWallCollision(Ball playball) {
        resetColideFlags();
        colideToRightWall = true;
    }

    public void handleLeftWallCollision(Ball playball) {
        resetColideFlags();
        colideToLeftWall = true;
    }

    public void handlePaddleCollision(Ball playball,Break paddle) {
        if (playball.gety() >= paddle.getybreak() - ballRadius) {
            if (playball.getx() >= paddle.getxbreak() && playball.getx() <= paddle.getxbreak() + paddle.getbreakwidth()) {
                handlePaddleHit(playball,paddle);
            }
        }
    }

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
    public void clearGameElements() {
        blocks.clear();
        chocos.clear();
        snows.clear();
        destroyedBlockCount = 0;
    }
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
    public void restoreBlocks(LoadSave loadSave,ArrayList<Block> blocks){
        blocks.clear();
        chocos.clear();
        for (BlockSerializable ser : loadSave.blocks) {
            int r = new Random().nextInt(200);
            blocks.add(new Block(ser.row, ser.j, colors[r % colors.length], ser.type));
        }
    }
    public void clearPenalty(Ball playball) {
        playball.setvx(1.0); // Reset to the original speed
        playball.setvy(1.0);
        ball.setFill(new ImagePattern(new Image("ball.png")));
        isSnowBonusActive = false;
        snowBonusStartTime = 0;
    }
    public void setBallPaddle(Ball playball,Break paddle){
        getRect().setX(paddle.getxbreak());
        getRect().setY(paddle.getybreak());
        getBall().setCenterX(playball.getx());
        getBall().setCenterY(playball.gety());
    }
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
    public boolean shouldSkipChoco(Bonus choco) {
        return choco.y > sceneHeigt || choco.taken;
    }

    public boolean isChocoCaught(Bonus choco,Break paddle) {
        return choco.y >= paddle.getybreak() && choco.y <= paddle.getybreak() + paddle.getbreakheight()
                && choco.x >= paddle.getxbreak() && choco.x <= paddle.getxbreak() + paddle.getbreakwidth();
    }
    public void updateChocoPosition(Bonus choco,long time) {
        choco.y += ((time - choco.timeCreated) / 1000.000) + 1.000;
    }
//    public boolean shouldSkipSnow(Bonus snow) {
//        return snow.y > sceneHeigt || snow.taken;
//    }
//    public boolean isSnowCaught(Bonus snow,Break paddle) {
//        return snow.y >= paddle.getybreak() && snow.y <= paddle.getybreak() + paddle.getbreakwidth()
//                && snow.x >= paddle.getxbreak() && snow.x <= paddle.getxbreak() + paddle.getbreakwidth();
//    }
//    public void updateSnowPosition(Bonus snow,long time) {
//        snow.y += ((time - snow.timeCreated) / 1000.000) + 1.000;
//    }
//    private double targetSlowSpeed = 0.3; // Set your desired slow speed
//    private double slowdownFactor = 0.98; // Adjust the factor to control the slowdown rate
//    private final long SNOW_BONUS_DURATION = 10000; // Duration of snow bonus in milliseconds (10 seconds)
//    private boolean isSnowBonusActive = false;
//    public void PenaltyActive(Ball playball,long time,long snowBonusStartTime){
//        playball.setvx(playball.getVx()*slowdownFactor);
//        playball.setvy(playball.getVy()*slowdownFactor);
//        // Ensure the speed doesn't go below the targetSlowSpeed
//        playball.setvx(Math.max(playball.getVx(), targetSlowSpeed));
//        playball.setvy(Math.max(playball.getVy(), targetSlowSpeed));
//        ball.setFill(new ImagePattern(new Image("snowball.png")));
//        long elapsedTime = time - snowBonusStartTime;
//        if (elapsedTime >= SNOW_BONUS_DURATION) {
//            // Snow bonus duration expired, reset the ball speed
//            playball.setvx(1.0); // Reset to the original speed
//            playball.setvy(1.0);
//            isSnowBonusActive = false;
//            ball.setFill(new ImagePattern(new Image("ball.png")));}
//    }

    private static final double TARGET_SLOW_SPEED = 0.3;
    private static final double SLOWDOWN_FACTOR = 0.98;
    private static final long SNOW_BONUS_DURATION = 10000;

    private boolean isSnowBonusActive = false;
    private long snowBonusStartTime = 0;
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

    private boolean isSnowCaught(Bonus snow, Break paddle) {
        return snow.y >= paddle.getybreak() && snow.y <= paddle.getybreak() + paddle.getbreakwidth()
                && snow.x >= paddle.getxbreak() && snow.x <= paddle.getxbreak() + paddle.getbreakwidth();
    }

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

    private void resetSnowBonus(Ball playball) {
        playball.setvx(1.0); // Reset to the original speed
        playball.setvy(1.0);
        isSnowBonusActive = false;
        ball.setFill(new ImagePattern(new Image("ball.png")));
    }

    private void activateSnowBonus(long time) {
        sound.playSnowBallSound();
        isSnowBonusActive = true;
        snowBonusStartTime = time;
    }
    public void deactivateSnowBonus() {
        isSnowBonusActive = false;
    }
    public void updateSnowPosition(Bonus snow, long time) {
        snow.y += ((time - snow.timeCreated) / 1000.0) + 1.0;
    }


    //getter

    public int getBreakHeight() {
        return breakHeight;
    }
    public int getBreakWidth(){
        return breakWidth;
    }

    public double getxBreak() {
        return xBreak;
    }

    public double getyBreak() {
        return yBreak;
    }

    public int getDestroyedBlockCount(){return destroyedBlockCount;}
    public int getHeart(){return heart;}
    public int getScore(){return score;}
    public int getLevel(){return level;}
    public Circle getBall() {
        return ball;
    }
    public Rectangle getRect() {return rect;}
    public boolean getisSnowBonusActive(){return isSnowBonusActive;}
    public boolean isColideToBreak(){
        return colideToBreak;
    }
    public boolean colideToBreakAndMoveToRight(){
        return colideToBreakAndMoveToRight;
    }
    public boolean iscolideToRightWall(){
        return colideToRightWall;
    }
    public boolean iscolideToLeftWall(){
        return colideToLeftWall;
    }
    public boolean isColideToRightBlock(){
        return colideToRightBlock;
    }
    public boolean isColideToLeftBlock(){
        return colideToLeftBlock;
    }
    public boolean isColideToBottomBlock(){
        return colideToBottomBlock;
    }
    public boolean isColideToTopBlock(){
        return colideToTopBlock;
    }

    //setter
    public void setBall(Circle ball){
        this.ball=ball;
    }
    public void setRect(Rectangle rect){
        this.rect=rect;
    }
    public void setIsGoldStauts(boolean isGoldStauts) {
        this.isGoldStauts = isGoldStauts;
    }

    public void setGoDownBall(boolean goDownBall) {
        this.goDownBall = goDownBall;
    }

    public void setGoRightBall(boolean goRightBall) {
        this.goRightBall = goRightBall;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public void setDestroyedBlockCount(int destroyedBlockCount) {
        this.destroyedBlockCount = destroyedBlockCount;
    }

    public void setXBall(double xBall) {
        this.xBall = xBall;
    }

    public void setYBall(double yBall) {
        this.yBall = yBall;
    }

    public void setXBreak(double xBreak) {
        this.xBreak = xBreak;
    }

    public void setYBreak(double yBreak) {
        this.yBreak = yBreak;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setGoldTime(long goldTime) {
        this.goldTime = goldTime;
    }
    public void setExistHeartBlock(boolean isExistHeartBlock){this.isExistHeartBlock=isExistHeartBlock;}
    public void setColideToBreak(boolean colideToBreak) {
        this.colideToBreak = colideToBreak;
    }

    public void setColideToBreakAndMoveToRight(boolean colideToBreakAndMoveToRight) {
        this.colideToBreakAndMoveToRight = colideToBreakAndMoveToRight;
    }

    public void setColideToRightWall(boolean colideToRightWall) {
        this.colideToRightWall = colideToRightWall;
    }

    public void setColideToLeftWall(boolean colideToLeftWall) {
        this.colideToLeftWall = colideToLeftWall;
    }

    public void setColideToRightBlock(boolean colideToRightBlock) {
        this.colideToRightBlock = colideToRightBlock;
    }

    public void setColideToLeftBlock(boolean colideToLeftBlock) {
        this.colideToLeftBlock = colideToLeftBlock;
    }

    public void setColideToBottomBlock(boolean colideToBottomBlock) {
        this.colideToBottomBlock = colideToBottomBlock;
    }

    public void setColideToTopBlock(boolean colideToTopBlock) {
        this.colideToTopBlock = colideToTopBlock;
    }


}