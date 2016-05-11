package com.example.marvoot.testingandroid.ViewModel;

import android.content.Context;
import android.databinding.BaseObservable;

import com.example.marvoot.testingandroid.Model.Comment;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Marvoot on 5/8/2016.
 */
public class CommentItemViewModel extends BaseObservable implements ViewModel {
    private Comment comment;
    private Context context;
    private Subscription subscription;

    public CommentItemViewModel(Context context, Comment comment) {
        this.comment = comment;
        this.context = context;
    }

    public String getCommentId() {
        return ((Integer) comment.CommentId).toString();
    }

    public String getCommentContent() {
        return comment.CommentContent;
    }

    public String getCommentTimestamp() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date parsedDate = dateFormat.parse(comment.CommentTimestamp);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(timestamp);
            String result = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
            return result;
        }catch(Exception e) {
            return null;
        }
    }

    public String getCommentUserId() { return ((Integer) comment.CommentUserId).toString(); }

    public String getCommentConfId() { return ((Integer) comment.CommentConfId).toString(); }

    public String getCommentLikesCount() { return ((Integer) comment.CommentLikesCount).toString(); }

    public String getCommentDislikesCount() { return ((Integer) comment.CommentDislikesCount).toString(); }

    public String getLoggedInUserInteractionType() { return ((Integer) comment.LoggedInUserInteractionType).toString(); }

    public void setLoggedInUserInteractionType(int type) {
        comment.LoggedInUserInteractionType = type;
    }

    public void setCommentLikesCount(int likesCount) {
        comment.CommentLikesCount = likesCount;
    }

    public void setCommentDislikesCount(int dislikesCount) {
        comment.CommentDislikesCount = dislikesCount;
    }

    // Allows recycling ItemRepoViewModels within the recyclerview adapter
    public void setComment(Comment comment) {
        this.comment = comment;
        notifyChange();
    }

    @Override
    public void destroy() {
        //In this case destroy doesn't need to do anything because there is not async calls
    }

}
