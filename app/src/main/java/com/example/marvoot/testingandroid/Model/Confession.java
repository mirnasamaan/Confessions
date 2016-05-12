package com.example.marvoot.testingandroid.Model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marvoot on 4/11/2016.
 */
public class Confession extends BaseObservable implements Parcelable {
    public int ConfId;
    public String ConfTitle;
    public String ConfContent;
    public String ConfLocation;
    public String ConfTimestamp;
    public String ConfUsername;
    public int ConfUserId;
    public int ConfCatId;
    public int ConfCommentsCount;
    public int ConfLikesCount;
    public int ConfDislikesCount;
    public int LoggedInUserInteractionType;

    public Confession() {
    }

    public Confession(int confId, String confTitle, String confContent, String confLocation, String confTimestamp, String confUsername, int confUserId, int confCatId, int confLikesCount, int confDislikesCount, int confCommentsCount, int LoggedInUserInteractionType){
        this.ConfId = confId;
        this.ConfTitle = confTitle;
        this.ConfContent = confContent;
        this.ConfLocation = confLocation;
        this.ConfTimestamp = confTimestamp;
        this.ConfUsername = confUsername;
        this.ConfUserId = confUserId;
        this.ConfCatId = confCatId;
        this.ConfCommentsCount = confCommentsCount;
        this.ConfLikesCount = confLikesCount;
        this.ConfDislikesCount = confDislikesCount;
        this.LoggedInUserInteractionType = LoggedInUserInteractionType;
    }

    public static List<Confession> createConfessionList (JSONArray objList) {
        try {
            ArrayList<Confession> confessionList = new ArrayList<Confession>();
            for (int i=0; i<objList.length(); i++) {
                JSONObject object = objList.getJSONObject(i);
                Confession conf = new Confession(
                        object.getInt("ConfId"),
                        object.getString("ConfTitle"),
                        object.getString("ConfContent"),
                        object.getString("ConfLocation"),
                        object.getString("ConfTimestamp"),
                        object.getString("ConfUsername"),
                        object.getInt("ConfUserId"),
                        object.getInt("ConfCatId"),
                        object.getInt("ConfCommentsCount"),
                        object.getInt("ConfLikesCount"),
                        object.getInt("ConfDislikesCount"),
                        object.getInt("LoggedInUserInteractionType")
                );

                confessionList.add(conf);
            }
            return confessionList;
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
        dest.writeInt(this.ConfId);
        dest.writeString(this.ConfTitle);
        dest.writeString(this.ConfContent);
        dest.writeString(this.ConfLocation);
        dest.writeString(this.ConfTimestamp);
        dest.writeString(this.ConfLocation);
        dest.writeString(this.ConfUsername);
        dest.writeInt(this.ConfUserId);
        dest.writeInt(this.ConfCatId);
        dest.writeInt(this.ConfCommentsCount);
        dest.writeInt(this.ConfLikesCount);
        dest.writeInt(this.ConfDislikesCount);
        dest.writeInt(this.LoggedInUserInteractionType);
    }

    protected Confession(Parcel in) {
        this.ConfId = in.readInt();
        this.ConfTitle = in.readString();
        this.ConfContent = in.readString();
        this.ConfLocation = in.readString();
        this.ConfTimestamp = in.readString();
        this.ConfUsername = in.readString();
        this.ConfUserId = in.readInt();
        this.ConfCatId = in.readInt();
        this.ConfDislikesCount = in.readInt();
        this.ConfLikesCount = in.readInt();
        this.ConfCommentsCount = in.readInt();
        this.LoggedInUserInteractionType = in.readInt();
    }

    @Bindable
    public String getConfId() {
        return Integer.toString(this.ConfId);
    }
    @Bindable
    public String getConfTitle() {
        return this.ConfTitle;
    }
    @Bindable
    public String getConfContent() {
        return this.ConfContent;
    }
    @Bindable
    public String getConfLocation() {
        return this.ConfLocation;
    }
    @Bindable
    public String getConfTimestamp() {
        return this.ConfTimestamp;
    }
    @Bindable
    public String getConfUsername() {
        return this.ConfUsername;
    }
    @Bindable
    public String getConfUserId() { return Integer.toString(this.ConfUserId); }
    @Bindable
    public String getConfCatId() { return  Integer.toString(this.ConfCatId); }
    @Bindable
    public String getConfDislikesCount() { return Integer.toString(this.ConfDislikesCount); }
    @Bindable
    public String getConfLikesCount() { return Integer.toString(this.ConfLikesCount); }
    @Bindable
    public String getConfCommentsCount() { return Integer.toString(this.ConfCommentsCount); }
    @Bindable
    public String getLoggedInUserInteractionType() { return Integer.toString(this.LoggedInUserInteractionType); }

    public static final Parcelable.Creator<Confession> CREATOR = new Parcelable.Creator<Confession>() {
        public Confession createFromParcel(Parcel source) {
            return new Confession(source);
        }

        public Confession[] newArray(int size) {
            return new Confession[size];
        }
    };

}
