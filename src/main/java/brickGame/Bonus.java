package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.Random;

/**
 * The {@code Bonus} class represents bonus object in this game.
 * This class contains bonus and penalty.
 * This class provides information about bonus position, type and graphical representation.
 * This class implements the {@code Serializable} interface for object serialization.
 *
 * @author Chai Ze Xuan
 */
public class Bonus implements Serializable {

    /**
     * Rectangle representing a chocolate bonus.
     */
    public Rectangle choco;

    /**
     * Rectangle representing a snow bonus.
     */
    public Rectangle snow;

    /**
     * The x-coordinate of the bonus.
     */
    public double x;

    /**
     * The y-coordinate of the bonus.
     */
    public double y;

    /**
     * The time when the bonus was created.
     */
    public long timeCreated;

    /**
     * Indicates whether the bonus has collided with the pedal.
     */
    public boolean taken = false;

    /**
     * Constructs a new Bonus object with the specified row and column indices.
     * The bonus is randomly generated and positioned based on the row and column.
     *
     * @param row The row index of the associated block in the game grid.
     * @param column The column index of the associated block in the game grid.
     */
    public Bonus(int row, int column) {
        x = (column * (Block.getWidth())) + Block.getPaddingH() + (Block.getWidth() / 2) - 15;
        y = (row * (Block.getHeight())) + Block.getPaddingTop() + (Block.getHeight() / 2) - 15;

        draw();
    }

    /**
     * method to initialise the bonus's and penalty's graphical representation.
     */
    private void draw() {
        choco = new Rectangle();
        choco.setWidth(30);
        choco.setHeight(30);
        choco.setX(x);
        choco.setY(y);
        snow = new Rectangle();
        snow.setWidth(30);
        snow.setHeight(30);
        snow.setX(x);
        snow.setY(y);

        String rrl;
        rrl = "penalty.png";
        String url;
        if (new Random().nextInt(20) % 2 == 0) {
            url = "bonus1.png";
        } else {
            url = "bonus2.png";
        }

        //set fill based on bonus tyoe and penalty
        choco.setFill(new ImagePattern(new Image(url)));
        snow.setFill(new ImagePattern(new Image(rrl)));
    }



}
