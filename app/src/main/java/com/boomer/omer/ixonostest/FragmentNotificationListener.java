package com.boomer.omer.ixonostest;

/**
 * Created by Omer on 2/11/2016.
 */

/**
 * Interface for a FragmentNotificationListener.
 * A class that is responsible for any fragments that may be needing
 * to fire notifications
 */
public interface FragmentNotificationListener {
    /**
     *
     * @param message The message that the FragmentNotificationListener will generate the notification for.
     */
    void createNotification(String message);
}
