package com.boomer.omer.ixonostest;

/**
 * Created by Omer on 2/12/2016.
 */
public interface NavigationController {
   static final int HOME = 0;
   static final int ABOUT = 1;
   static final int SIGN_UP = 2;
   void navigateTo(int destination);
}
