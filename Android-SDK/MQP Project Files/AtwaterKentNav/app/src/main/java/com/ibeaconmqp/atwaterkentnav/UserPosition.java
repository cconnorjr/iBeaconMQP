package com.ibeaconmqp.atwaterkentnav;

/**
 * Created by d4veg on 4/10/2017.
 */

public class UserPosition {
    private int xpos;
    private int ypos;

    public UserPosition(){
        this.xpos=0;
        this.ypos=0;
    }

    public UserPosition(int xpos, int ypos){
        this.xpos=convertXpos(xpos);
        this.ypos=convertYpos(ypos);
    }

    public int getXpos(){
        return xpos;
    }

    public int getYpos(){
        return ypos;
    }

    //Takes the x postion from the LMS and converts it to a pixel location
    private int convertXpos(int x){
        if(x>0 && x<80)
            return 400-(10*x);
        else
            return 0;
    }

    //Takes the Y position from the LMS and converts it to a pixel location
    private int convertYpos(int y){
        if(y>0 && y<80)
            return 100+(10*y);
        else
            return 0;
    }
}
