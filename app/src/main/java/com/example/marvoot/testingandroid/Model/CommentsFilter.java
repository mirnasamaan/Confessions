package com.example.marvoot.testingandroid.Model;

/**
 * Created by Marvoot on 4/13/2016.
 */
public class CommentsFilter {
    final int LoggedInUser;
    final int ConfessionId;
    final int LastCommentId;
    final int Count;

    public CommentsFilter(int loggedInUser, int confessionId, int lastCommentId, int count) {
        this.LoggedInUser = loggedInUser;
        this.ConfessionId = confessionId;
        this.LastCommentId = lastCommentId;
        this.Count = count;
    }
}
