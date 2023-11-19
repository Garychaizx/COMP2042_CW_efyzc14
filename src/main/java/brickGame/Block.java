package brickGame;


import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

public class Block implements Serializable {
    private static Block block = new Block(-1, -1, Color.TRANSPARENT, 99);
    public static int HIT_TOP_LEFT = 4;
    public static int HIT_TOP_RIGHT = 5;
    public static int HIT_BOTTOM_LEFT = 6;
    public static int HIT_BOTTOM_RIGHT = 7;

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
    public static int BLOCK_SNOW = 103;


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
        } else if (type == BLOCK_SNOW) {
            Image image = new Image("snow.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else {
            rect.setFill(color);
        }

    }


    public double checkHitToBlock(double xBall, double yBall, double ballRadius) {

        if (isDestroyed) {
            return NO_HIT;
        }

        double ballLeftEdge = xBall - ballRadius;
        double ballRightEdge = xBall + ballRadius;
        double ballTopEdge = yBall - ballRadius;
        double ballBottomEdge = yBall + ballRadius;

        boolean collideTop = ballBottomEdge >= y && ballTopEdge < y;
        boolean collideBottom = ballTopEdge <= y + height && ballBottomEdge > y + height;
        boolean collideLeft = ballRightEdge >= x && ballLeftEdge < x;
        boolean collideRight = ballLeftEdge <= x + width && ballRightEdge > x + width;

        if (collideTop && xBall >= x && xBall <= x + width) {
            return HIT_TOP;
        } else if (collideBottom && xBall >= x && xBall <= x + width) {
            return HIT_BOTTOM;
        } else if (collideLeft && yBall >= y && yBall <= y + height) {
            return HIT_LEFT;
        } else if (collideRight && yBall >= y && yBall <= y + height) {
            return HIT_RIGHT;
        }

        return NO_HIT;
    }
    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
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