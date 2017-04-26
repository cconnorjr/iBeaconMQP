package com.ibeaconmqp.atwaterkentnav;

/**
 * Created by ark on 2/1/17.
 */

public class LMSBeacon {
    //instance variables
    private int x;
    private int y;

    public LMSBeacon(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
