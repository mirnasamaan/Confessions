package com.example.marvoot.testingandroid.Model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marvoot on 4/11/2016.
 */
public class Comment extends BaseObservable implements Parcelable {
    public int CommentId;
    public String CommentContent;
    public String CommentTimestamp;
    public int CommentUserId;
    public int CommentConfId;
    public int CommentLikesCount;
    public int CommentDislikesCount;
    public int LoggedInUserInteractionType;

    public Comment() {
    }

    public Comment(int commentId, String commentContent, String commentTimestamp, int commentUserId, int commentConfId, int commentLikesCount, int commentDislikesCount, int LoggedInUserInteractionType){
        this.CommentId = commentId;
        this.CommentContent = commentContent;
        this.CommentTimestamp = commentTimestamp;
        this.CommentUserId = commentUserId;
        this.CommentConfId = commentConfId;
        this.CommentLikesCount = commentLikesCount;
        this.CommentDislikesCount = commentDislikesCount;
        this.LoggedInUserInteractionType = LoggedInUserInteractionType;
    }

    public static List<Comment> createCommentList (JSONArray objList) {
        try {
            ArrayList<Comment> commentsList = new ArrayList<Comment>();
            for (int i=0; i<objList.length(); i++) {
                JSONObject object = objList.getJSONObject(i);
                Comment comment = new Comment(
                        object.getInt("CommentId"),
                        object.getString("CommentContent"),
                        object.getString("CommentTimestamp"),
                        object.getInt("CommentUserId"),
                        object.getInt("CommentConfId"),
                        object.getInt("CommentLikesCount"),
                        object.getInt("CommentDislikesCount"),
                        object.getInt("LoggedInUserInteractionType")
                );

                commentsList.add(comment);
            }
            return commentsList;
        }catch(Exception e){
            Log.d("Error:", e.toString());
            return null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.CommentId);
        dest.writeString(this.CommentContent);
        dest.writeString(this.CommentTimestamp);
        dest.writeInt(this.CommentUserId);
        dest.writeInt(this.CommentConfId);
        dest.writeInt(this.CommentLikesCount);
        dest.writeInt(this.CommentDislikesCount);
        dest.writeInt(this.LoggedInUserInteractionType);
    }

    protected Comment(Parcel in) {
        this.CommentId = in.readInt();
        this.CommentContent = in.readString();
        this.CommentTimestamp = in.readString();
        this.CommentUserId = in.readInt();
        this.CommentConfId = in.readInt();
        this.CommentDislikesCount = in.readInt();
        this.CommentLikesCount = in.readInt();
        this.LoggedInUserInteractionType = in.readInt();
    }

    @Bindable
    public String getCommentId() {
        return Integer.toString(this.CommentId);
    }
    @Bindable
    public String getCommentContent() {
        return this.CommentContent;
    }
    @Bindable
    public String getCommentTimestamp() {
        return this.CommentTimestamp;
    }
    @Bindable
    public String getCommentUserId() { return Integer.toString(this.CommentUserId); }
    @Bindable
    public String getCommentConfId() { return  Integer.toString(this.CommentConfId); }
    @Bindable
    public String getCommentDisLikesCount() { return Integer.toString(this.CommentDislikesCount); }
    @Bindable
    public String getCommentLikesCount() { return Integer.toString(this.CommentLikesCount); }
    @Bindable
    public String getLoggedInUserInteractionType() { return Integer.toString(this.LoggedInUserInteractionType); }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

}
