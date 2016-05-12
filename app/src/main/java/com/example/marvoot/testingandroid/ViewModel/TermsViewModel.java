package com.example.marvoot.testingandroid.ViewModel;


import android.content.Context;
import com.example.marvoot.testingandroid.Model.UserData;

import rx.Subscription;


public class TermsViewModel implements ViewModel {


    private Context context;
    private Subscription subscription;
    private DataListener dataListener;


    public TermsViewModel(Context context) {
        this.context = context;
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
        void onUserDataChanged(UserData userdata);
    }
}