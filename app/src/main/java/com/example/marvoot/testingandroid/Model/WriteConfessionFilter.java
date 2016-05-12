package com.example.marvoot.testingandroid.Model;

/**
 * Created by Marvoot on 4/13/2016.
 */
public class WriteConfessionFilter {
    final String ConfTitle;
    final String ConfContent;
    final String ConfLocation;
    final String ConfUsername;
    final int ConfUserId;
    public int ConfCatId;


    public WriteConfessionFilter(int ConfUserId, String ConfUsername, String ConfTitle, String ConfContent, String ConfLocation, int ConfCatId) {
        this.ConfTitle = ConfTitle;
        this.ConfContent = ConfContent;
        this.ConfLocation = ConfLocation;
        this.ConfUsername = ConfUsername;
        this.ConfUserId = ConfUserId;
        this.ConfCatId = ConfCatId;
    }
}
