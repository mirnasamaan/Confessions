package com.example.marvoot.testingandroid.Model;


import android.os.Parcel;
import android.os.Parcelable;


public class UserData implements Parcelable {


    public int userId;
    public String username;
    public String password;
    public String gender;


    public UserData() {
    }


    public UserData(int userId, String username, String password, String gender){
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.gender = gender;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.gender);
    }


    protected UserData(Parcel in) {
        this.userId = in.readInt();
        this.username = in.readString();
        this.password = in.readString();
        this.gender = in.readString();
    }


    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        public UserData createFromParcel(Parcel source) {
            return new UserData(source);
        }

        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };


}
