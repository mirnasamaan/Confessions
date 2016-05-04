package com.example.marvoot.testingandroid.Model;

/**
 * Created by Marvoot on 4/13/2016.
 */
public class ConfessionsFilter {
    final int loggedInUser;
    final int lastConfId;
    final int page;
    final int count;
    final String mode;

    public ConfessionsFilter(int loggedInUser, int lastConfId, int page, int count, String mode) {
        this.loggedInUser = loggedInUser;
        this.lastConfId = lastConfId;
        this.page = page;
        this.count = count;
        this.mode = mode;
    }
}
