package brickGame;

/**
 * This class represents the ball object in the game.
 *  It encapsulates the ball's position, velocity, and movement direction.
 *
 * @author Chai Ze Xuan
 */
public class Ball {
    private double xball;
    private double yball;
    private double ballradius;
    private double vx;
    private double vy;
    private boolean godownball;
    private boolean gorightball;

    /**
     * Constructs a new Ball object with the specified initial position and radius.
     * The default initial velocity is set to 1.0 in both the horizontal and vertical directions.
     *
     * @param xBall The initial x-coordinate of the ball.
     * @param yBall The initial y-coordinate of te ball.
     * @param radius The radius of the ball.
     */
    public Ball(double xBall,double yBall,double radius){
        this.xball=xBall;
        this.yball=yBall;
        this.ballradius=radius;
        this.vx=1.0;
        this.vy=1.0;
        this.godownball=true;
        this.gorightball=true;
    }

    /**
     * Updates the position of the ball based on its current velocity and movement direction.
     */
    public void updatePosition(){
        if (godownball) {
            this.yball += vy;
        } else {
            this.yball -= vy;
        }

        if (gorightball) {
            this.xball += vx;
        } else {
            this.xball -= vx;
        }
    }

    /**
     * Changes the movement direction of the ball to left.
     *
     * @return False, indicating the ball is now moving left.
     */
    public boolean goleftball(){
        return this.gorightball=false;
    }

    /**
     * Changes the movement direction of the ball to right.
     *
     * @return True, indicating the ball is now moving right.
     */
    public boolean gorightball(){
        return this.gorightball=true;
    }

    /**
     * Changes the movement direction of the ball to up.
     *
     * @return False, indicating the ball is now moving up.
     */
    public boolean goupball(){
        return this.godownball=false;
    }

    /**
     * Changes the movement direction of the ball to down.
     *
     * @return True, indicating the ball is now moving down.
     */
    public boolean godownball(){
        return this.godownball=true;
    }

    //Getter

    /**
     * Gets the x-coordinate of the ball.
     *
     * @return The x-coordinate of the ball.
     */
    public double getx(){
        return this.xball;
    }

    /**
     * Gets the y-coordinate of the ball.
     *
     * @return The y-coordinate of the ball.
     */
    public double gety(){
        return this.yball;
    }

    /**
     * Gets the horizontal velocity of the ball.
     *
     * @return The horizontal velocity of the ball.
     */
    public double getVx() {
        return vx;
    }

    /**
     * Gets the vertical velocity of the ball.
     *
     * @return The vertical velocity of the ball.
     */
    public double getVy() {
        return vy;
    }

    /**
     * Gets the radius of the ball.
     *
     * @return The radius of the ball.
     */
    public double getBallradius() {
        return ballradius;
    }

    //setter
    /**
     * Sets the x-coordinate of the ball.
     *
     * @param x The new x-coordinate of the ball.
     */
    public void setx(double x){
        this.xball=x;
    }

    /**
     * Sets the y-coordinate of the ball.
     *
     * @param y The new y-coordinate of the ball.
     */
    public void sety(double y){
        this.yball=y;
    }

    /**
     * Sets the horizontal velocity of the ball.
     *
     * @param vx The new horizontal velocity of the ball.
     */
    public void setvx(double vx){
        this.vx=vx;
    }

    /**
     * Sets the vertical velocity of the ball.
     *
     * @param vy The new vertical velocity of the ball.
     */
    public void setvy(double vy){
        this.vy=vy;
    }
}