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
    private int  heart    = 10000;
    private int score;

    public Ball initBall() {
        ball = new Circle();
        ball.setRadius(ballRadius);
        ball.setFill(new ImagePattern(new Image("ball.png")));
        xBall = xBreak + (breakWidth / 2);
        yBall = yBreak - ballRadius - 40;
        return new Ball(xBall,yBall,ballRadius);
    }
    public Break initBreak() {
        rect = new Rectangle();
        rect.setWidth(breakWidth);
        rect.setHeight(breakHeight);
        rect.setX(xBreak);
        rect.setY(yBreak);

        ImagePattern pattern = new ImagePattern(new Image("block.jpg"));

        rect.setFill(pattern);
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
//        if (!isGoldStauts) {
//            handleGameOver();
//        }
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

//    private void setPhysicsToBall() {
//        //v = ((time - hitTime) / 1000.000) + 1.000;
//
//        playball.updatePosition();
//
//        if (playball.gety() <= 0) {
//            //vX = 1.000;
//            resetColideFlags();
//            playball.godownball();
//            return;
//        }
//        if (playball.gety() >= sceneHeigt) {
//            resetColideFlags();
//            playball.goupball();
//            if (!isGoldStauts) {
//                //TODO gameover
//                heart--;
//                new Score().show(sceneWidth / 2, sceneHeigt / 2, -1, this);
//
//                if (heart == 0) {
//                    new Score().showGameOver(this);
//                    engine.stop();
//                }
//
//            }
//            //return;
//        }
//
//        if (playball.gety() >= paddle.getybreak() - ballRadius) {
//            //System.out.println("Colide1");
//            if (playball.getx() >= paddle.getxbreak() && playball.getx() <= paddle.getxbreak() + paddle.getbreakwidth()) {
//                hitTime = time;
//                resetColideFlags();
//                colideToBreak = true;
//                playball.goupball();
//
//                double relation = (playball.getx() - paddle.getcenterbreakX()) / (paddle.getbreakwidth() / 2);
//
//                if (Math.abs(relation) <= 0.3) {
//                    //vX = 0;
//                    playball.setvx(Math.abs(relation));
//                } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
//                    playball.setvx((Math.abs(relation) * 1.5) + (level / 3.500));
//                    //System.out.println("vX " + vX);
//                } else {
//                    playball.setvx((Math.abs(relation) * 2) + (level / 3.500));
//                    //System.out.println("vX " + vX);
//                }
//
//                if (playball.getx() - paddle.getcenterbreakX() > 0) {
//                    colideToBreakAndMoveToRight = true;
//                } else {
//                    colideToBreakAndMoveToRight = false;
//                }
//                //System.out.println("Colide2");
//            }
//        }
//
//        if (playball.getx() >= sceneWidth) {
//            resetColideFlags();
//            //vX = 1.000;
//            colideToRightWall = true;
//        }
//
//        if (playball.getx() <= 0) {
//            resetColideFlags();
//            //vX = 1.000;
//            colideToLeftWall = true;
//        }
//
//        if (colideToBreak) {
//            if (colideToBreakAndMoveToRight) {
//                playball.gorightball();
//            } else {
//                playball.goleftball();
//            }
//        }
//
//        //Wall Colide
//
//        if (colideToRightWall) {
//            playball.goleftball();
//        }
//
//        if (colideToLeftWall) {
//            playball.gorightball();
//        }
//
//        //Block Colide
//
//        if (colideToRightBlock) {
//            playball.gorightball();
//        }
//
//        if (colideToLeftBlock) {
//            playball.goleftball();
//        }
//
//        if (colideToTopBlock) {
//            playball.goupball();
//        }
//
//        if (colideToBottomBlock) {
//            playball.godownball();
//        }
//
//
//    }

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
//            isSnowStauts = false;
        isExistHeartBlock = false;
        hitTime = 0;
        time = 0;
        goldTime = 0;
//            snowTime=0;

        blocks.clear();
        chocos.clear();
        snows.clear();
    }

    //getter
    public int getScore(){return score;}
    public int getLevel(){return level;}
    public Circle getBall() {
        return ball;
    }
    public Rectangle getRect() {return rect;}
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
