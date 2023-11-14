package brickGame;


import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

public class Block implements Serializable {
    private static Block block = new Block(-1, -1, Color.TRANSPARENT, 99);

    public int row;
    public int column;


    public boolean isDestroyed = false;

    private Color color;
    public int type;

    public int x;
    public int y;

    private int width = 100;
    private int height = 30;
    private int paddingTop = height * 2;
    private int paddingH = 50;
    public Rectangle rect;


    public static int NO_HIT = -1;
    public static int HIT_RIGHT = 0;
    public static int HIT_BOTTOM = 1;
    public static int HIT_LEFT = 2;
    public static int HIT_TOP = 3;

    public static int BLOCK_NORMAL = 99;
    public static int BLOCK_CHOCO = 100;
    public static int BLOCK_STAR = 101;
    public static int BLOCK_HEART = 102;


    public Block(int row, int column, Color color, int type) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.type = type;

        draw();
    }

    private void draw() {
        x = (column * width) + paddingH;
        y = (row * height) + paddingTop;

        rect = new Rectangle();
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setX(x);
        rect.setY(y);

        if (type == BLOCK_CHOCO) {
            Image image = new Image("choco.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_HEART) {
            Image image = new Image("heart.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_STAR) {
            Image image = new Image("star.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else {
            rect.setFill(color);
        }

    }


    // Update the return type of the method to return the collision type
// Update the return type of the method to return the collision type
    public int checkHitToBlock(double xBall, double yBall, double ballRadius) {
        if (isDestroyed) {
            return NO_HIT;
        }

        double blockTop = y;
        double blockBottom = y + height;
        double blockLeft = x;
        double blockRight = x + width;

        // Check if the ball intersects with the block
        if (xBall + ballRadius >= blockLeft && xBall - ballRadius <= blockRight &&
                yBall + ballRadius >= blockTop && yBall - ballRadius <= blockBottom) {

            // Check collisions at multiple points along the ball's path
            boolean hitTop = yBall - ballRadius <= blockTop && yBall + ballRadius > blockTop;
            boolean hitBottom = yBall + ballRadius >= blockBottom && yBall - ballRadius < blockBottom;
            boolean hitLeft = xBall - ballRadius <= blockLeft && xBall + ballRadius > blockLeft;
            boolean hitRight = xBall + ballRadius >= blockRight && xBall - ballRadius < blockRight;

            if (hitTop && !hitBottom && !hitLeft && !hitRight) {
                return HIT_TOP;
            } else if (hitBottom && !hitTop && !hitLeft && !hitRight) {
                return HIT_BOTTOM;
            } else if (hitLeft && !hitTop && !hitBottom && !hitRight) {
                return HIT_LEFT;
            } else if (hitRight && !hitTop && !hitBottom && !hitLeft) {
                return HIT_RIGHT;
            }

            // Continuous collision detection to handle fast ball movement
            if (hitTop && (yBall - ballRadius) < blockTop) {
                return HIT_TOP;
            } else if (hitBottom && (yBall + ballRadius) > blockBottom) {
                return HIT_BOTTOM;
            } else if (hitLeft && (xBall - ballRadius) < blockLeft) {
                return HIT_LEFT;
            } else if (hitRight && (xBall + ballRadius) > blockRight) {
                return HIT_RIGHT;
            }
        }

        return NO_HIT;
    }




    public static int getPaddingTop() {
        return block.paddingTop;
    }

    public static int getPaddingH() {
        return block.paddingH;
    }

    public static int getHeight() {
        return block.height;
    }

    public static int getWidth() {
        return block.width;
    }

}
