package com.example.marvoot.testingandroid.Model;

/**
 * Created by Marvoot on 4/19/2016.
 */
public class UserInteraction {
    final int UserId;
    final int ConfId;
    final int CommentId;

    public UserInteraction(int UserId, int ConfId, int CommentId) {
        this.UserId = UserId;
        this.ConfId = ConfId;
        this.CommentId = CommentId;
    }
}
