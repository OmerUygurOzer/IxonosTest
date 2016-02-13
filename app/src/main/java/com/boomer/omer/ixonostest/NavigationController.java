package com.boomer.omer.ixonostest;

/**
 * Created by Omer on 2/12/2016.
 */
public interface NavigationController {
    public static final int HOME = 0;
    public static final int ABOUT = 1;
    public static final int SIGN_UP = 2;
    public void navigateTo(int destination);
}
