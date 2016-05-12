package com.example.marvoot.testingandroid.Model;

/**
 * Created by Marvoot on 4/13/2016.
 */
public class ConfessionId {
    final int ConfId;
    final int LoggedInUserId;

    public ConfessionId(int confessionId, int LoggedInUserId) {
        this.ConfId = confessionId;
        this.LoggedInUserId = LoggedInUserId;
    }
}
