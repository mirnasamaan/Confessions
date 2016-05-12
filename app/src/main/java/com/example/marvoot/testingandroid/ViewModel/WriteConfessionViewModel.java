package com.example.marvoot.testingandroid.ViewModel;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.marvoot.testingandroid.ConfApplication;
import com.example.marvoot.testingandroid.Model.Confession;
import com.example.marvoot.testingandroid.Model.UserData;
import com.example.marvoot.testingandroid.Model.ConfessionService;
import com.example.marvoot.testingandroid.Model.WriteConfessionFilter;
import com.example.marvoot.testingandroid.PublicFunctions.AlertMessages;
import com.example.marvoot.testingandroid.PublicFunctions.CheckInternetConnection;
import com.example.marvoot.testingandroid.PublicFunctions.PopupMessages;
import com.example.marvoot.testingandroid.PublicFunctions.SessionManagement;
import com.example.marvoot.testingandroid.R;
import com.example.marvoot.testingandroid.View.ConfessionActivity;
import com.example.marvoot.testingandroid.View.RegisterActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;


public class WriteConfessionViewModel implements ViewModel {


    private Context context;
    private Subscription subscription;
    private DataListener dataListener;
    private Confession confession;
    //private ArrayList<Confession> allConfessions = new ArrayList<Confession>();
    private String editTextTitleValue;
    private String editTextMessageValue;
    public ObservableField<String> title_field_error;
    public ObservableField<String> message_field_error;
    public ObservableField<String> words_count;
    AlertMessages alertMessages;
    SessionManagement new_session;
    int userID;
    String userName;


    public WriteConfessionViewModel(Context context, DataListener dataListener) {
        this.context = context;
        this.dataListener = dataListener;
        this.title_field_error = new ObservableField<>(null);
        this.message_field_error = new ObservableField<>(null);
        this.words_count = new ObservableField<>(null);
        alertMessages = new AlertMessages();
        new_session = new SessionManagement(context);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap = new_session.getUserDetails();
        userID = Integer.parseInt(hashMap.get("userid"));
        userName = hashMap.get("username");
        String text = "المتبقى: "+ 300 + " حرف";
        words_count.set(text);
    }


    public void setDataListener(DataListener dataListener) {
        this.dataListener = dataListener;
    }


    @Override
    public void destroy() {
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        subscription = null;
        context = null;
        dataListener = null;
    }


    public interface DataListener {
        void onConfessionChanged(Confession confessions);
    }


    public void loadUserData(int userID, String userName, String confTitle, String confMessage, String confLocation, int ConfCatId) {
        ConfApplication application = ConfApplication.get(context);
        final ConfessionService confessionService = application.getConfessionService();
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        WriteConfessionFilter filter = new WriteConfessionFilter(userID,userName,confTitle,confMessage,confLocation,ConfCatId);
        subscription = confessionService.WriteConfession(filter)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new Subscriber<Confession>() {
                    @Override
                    public void onCompleted() {
                        alertMessages.HideAlert();
                        if (dataListener != null)
                            dataListener.onConfessionChanged(confession);
                        if (confession != null) {
                            //UserData userData = new UserData();
                            //context.startActivity(ConfessionActivity.newIntent(context, userData));
                            //((Activity) context).finish();
                            Intent openMainActivity= new Intent(context,ConfessionActivity.class);
                            openMainActivity.putExtra("load", "yes");
                            openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            context.startActivity(openMainActivity);
                            ((Activity) context).finish();
                        }
                        else
                        {
                            PopupMessages popup = new PopupMessages();
                            String error = "Something went wrong..Please try again later";
                            popup.showAlertDialog(context, "Sorry", error, null);
                        }
                    }


                    @Override
                    public void onError(Throwable error) {
                        alertMessages.HideAlert();
                        PopupMessages popup = new PopupMessages();
                        String error_msg = "Something went wrong..Please try again later";
                        popup.showAlertDialog(context, "Sorry", error_msg, null);
                    }


                    @Override
                    public void onNext(Confession confessions) {
                        //Log.i("Tag", "Confessions loaded " + confessions);
                        //WriteConfessionViewModel.this.confession = confessions;
                       // WriteConfessionViewModel.this.allConfessions.addAll(confessions);
                        WriteConfessionViewModel.this.confession = confessions;
                    }
                });
    }


    public void onClickSearch(View view) {
        //title_field_error.set(null);
        message_field_error.set(null);
        String empty = "";
        //if( editTextTitleValue == null || editTextTitleValue.equals(empty))
         //   title_field_error.set("Title field can't be empty");
        //else
        if( editTextMessageValue == null || editTextMessageValue.equals(empty))
            message_field_error.set("Message field can't be empty");
        else {
            //title_field_error.set(null);
            message_field_error.set(null);
            alertMessages.ShowAlert(context, "Checking Network", "Loading..",false);
            if (CheckInternetConnection.getInstance(context).isOnline())
            {
                //try{ Thread.sleep(3000); }catch(InterruptedException e){ }
                alertMessages.HideAlert();
                alertMessages.ShowAlert(context, "Please wait", "Loading..", false);
                loadUserData(userID, userName, editTextTitleValue, editTextMessageValue, "", 1);
            }
            else
            {
                alertMessages.HideAlert();
                PopupMessages popup = new PopupMessages();
                String error = "You are offline";
                popup.showAlertDialog(context, "Sorry", error, null);
            }
        }
    }


    public TextWatcher getTitleEditTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                editTextTitleValue = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
    }


    public TextWatcher getMessageEditTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                editTextMessageValue = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int count = editable.toString().length();
                String text = "المتبقى: "+ (300-count) + " حرف";
                words_count.set(text);
            }
        };
    }


    public View.OnFocusChangeListener getTitleEditFocusListner() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        };
    }


    public View.OnFocusChangeListener getMessageEditFocusListner() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        };
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
