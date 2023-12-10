package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * The {@code Break} class represents a break object in this game.
 * A break is typically associated with a paddle and defines its position and dimensions.
 * The class provides information about the break's position, dimensions, and a method to retrieve its center.
 *
 * @author Chai Ze Xuan
 */
public class Break {

    /**
     * The x-coordinate of the break.
     */
    private double xbreak ;

    /**
     * The y-coordinate of the break.
     */
    private double ybreak ;

    /**
     * The width of the break.
     */
    private int breakwidth ;

    /**
     * The height of the break.
     */
    private int breakheight ;

    /**
     * Half of the break's width.
     */
    private int halfbreakwidth ;

    /**
     * The x-coordinate of the center of the break.
     */
    private double centerBreakX;

    /**
     * Constructs a new Break object with the specified x-coordinate, y-coordinate, width, height and the half break width.
     *
     * @param xBreak The x-coordinate of the break.
     * @param yBreak The y-coordinate of the break.
     * @param breakWidth The width of the break.
     * @param breakHeight The height of the break.
     */
    public Break (double xBreak,double yBreak,int breakWidth,int breakHeight){
        this.xbreak = xBreak;
        this.ybreak = yBreak;
        this.breakwidth = breakWidth;
        this.breakheight = breakHeight;
        this.halfbreakwidth = this.breakwidth/2;
    }

    //Getter
    /**
     * Gets the x-coordinate of the break.
     *
     * @return The x-coordinate of the break.
     */
    public double getxbreak(){
        return this.xbreak;
    }

    /**
     * Gets the y-coordinate of the break.
     *
     * @return The y-coordinate of the break.
     */
    public double getybreak(){
        return this.ybreak;
    }

    /**
     * Gets the width of the break.
     *
     * @return The width of the break.
     */
    public int getbreakwidth(){return this.breakwidth;}

    /**
     * Gets the height of the break.
     *
     * @return The height of the break.
     */
    public int getbreakheight(){return this.breakheight;}

    /**
     * Gets half of the break's width.
     *
     * @return Half of the break's width.
     */
    public int gethalfbreakwidth(){return this.halfbreakwidth;}

    /**
     * Gets the x-coordinate of the center of the break.
     *
     * @return The x-coordinate of the center of the break.
     */
    public double getcenterbreakX() {
        return centerBreakX;
    }

    //setter
    /**
     * Sets the x-coordinate of the break.
     *
     * @param xBreak The new x-coordinate of the break.
     */
    public void setxbreak (double xBreak){
        this.xbreak=xBreak;
    }

    /**
     * Sets the y-coordinate of the break.
     *
     * @param yBreak The new y-coordinate of the break.
     */
    public void setybreak (double yBreak){
        this.ybreak=yBreak;
    }

    /**
     * Sets the x-coordinate of the center of the break.
     *
     * @param centerBreakX The new x-coordinate of the center of the break.
     */
    public void setcenterbreakx(double centerBreakX){
        this.centerBreakX=centerBreakX;
    }
}
