package com.example.marvoot.testingandroid.PublicFunctions;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;


public class SessionManagement {


    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "LoginUser";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USERID = "userid";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";


    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createLoginSession(String userid, String username, String password){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERID, userid);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }


    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_USERID, pref.getString(KEY_USERID, null));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        return user;
    }


    public void logoutUser(){
        editor.clear();
        editor.commit();
    }


}
