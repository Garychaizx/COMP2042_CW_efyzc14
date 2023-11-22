package brickGame;

public class Ball {
    private double xball;
    private double yball;
    private double ballradius;
    private double vx;
    private double vy;
    private boolean godownball;
    private boolean gorightball;

    public Ball(double xBall,double yBall,double radius){
        this.xball=xBall;
        this.yball=yBall;
        this.ballradius=radius;
        this.vx=1.0;
        this.vy=1.0;
        this.godownball=true;
        this.gorightball=true;
    }

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

    public boolean goleftball(){
        return this.gorightball=false;
    }
    public boolean gorightball(){
        return this.gorightball=true;
    }
    public boolean goupball(){
        return this.godownball=false;
    }
    public boolean godownball(){
        return this.godownball=true;
    }

    //getter
    public double getx(){
        return this.xball;
    }
    public double gety(){
        return this.yball;
    }
    public double getVx() {
        return vx;
    }
    public double getVy() {
        return vy;
    }
    public double getBallradius() {
        return ballradius;
    }
    public boolean isGodownball() {
        return godownball;
    }
    public boolean isGorightball() {
        return gorightball;
    }

    //setter
    public void setx(double x){
        this.xball=x;
    }
    public void sety(double y){
        this.yball=y;
    }
    public void setvx(double vx){
        this.vx=vx;
    }
    public void setvy(double vy){
        this.vy=vy;
    }
    public void setGodownball(boolean godownball){
        this.godownball=godownball;
    }
    public void setGorightball(boolean gorightball){
        this.gorightball=gorightball;
    }
}