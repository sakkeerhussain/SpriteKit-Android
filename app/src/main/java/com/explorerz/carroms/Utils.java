package com.explorerz.carroms;
/*
 * Created by sakkeerhussain on 25/03/17.
 */

public class Utils {

    public static float[] concat(float[] first, float[] second) {
        float[] r = new float[first.length + second.length];
        System.arraycopy(first, 0, r, 0, first.length);
        System.arraycopy(second, 0, r, first.length, second.length);
        return r;
    }
}
