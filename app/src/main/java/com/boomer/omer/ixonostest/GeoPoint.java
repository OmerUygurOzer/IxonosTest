package com.boomer.omer.ixonostest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omer on 2/11/2016.
 */
public class GeoPoint {
    public double latitude;
    public double longitute;
    public List<String> addresses = new ArrayList<String>();
    public void reset(){
        addresses.clear();
        addresses.add("");
    }
}
