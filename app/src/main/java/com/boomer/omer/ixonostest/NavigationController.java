package com.boomer.omer.ixonostest;

/**
 * Created by Omer on 2/12/2016.
 */

/**
 * Interface for a class that is in charge of navigating
 * through the UI features. Fragments in this case.
 */
public interface NavigationController {

   /**
    * Common destinations of the application represented in static integers
    */
   static final int HOME = 0;
   static final int ABOUT = 1;
   static final int SIGN_UP = 2;
   void navigateTo(int destination);
}
