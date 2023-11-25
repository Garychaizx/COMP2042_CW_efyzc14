package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Break {
    private double xbreak ;
    private double ybreak ;

    private int breakwidth ;
    private int breakheight ;
    private int halfbreakwidth ;
    private double centerBreakX;

    public Break (double xBreak,double yBreak,int breakWidth,int breakHeight){
        this.xbreak = xBreak;
        this.ybreak = yBreak;
        this.breakwidth = breakWidth;
        this.breakheight = breakHeight;
        this.halfbreakwidth = this.breakwidth/2;
    }

    //getter
    public double getxbreak(){
        return this.xbreak;
    }
    public double getybreak(){
        return this.ybreak;
    }
    public int getbreakwidth(){return this.breakwidth;}
    public int getbreakheight(){return this.breakheight;}
    public int gethalfbreakwidth(){return this.halfbreakwidth;}
    public double getcenterbreakX() {
        return centerBreakX;
    }

    //setter
    public void setxbreak (double xBreak){
        this.xbreak=xBreak;
    }
    public void setybreak (double yBreak){
        this.ybreak=yBreak;
    }
    public void setcenterbreakx(double centerBreakX){
        this.centerBreakX=centerBreakX;
    }
}
