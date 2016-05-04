package com.example.marvoot.testingandroid.ViewModel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.marvoot.testingandroid.ConfApplication;
import com.example.marvoot.testingandroid.ConfessionAdapter;
import com.example.marvoot.testingandroid.Model.UserInteraction;
import com.example.marvoot.testingandroid.Model.Confession;
import com.example.marvoot.testingandroid.Model.ConfessionService;
import com.example.marvoot.testingandroid.Model.ConfessionsFilter;
import com.example.marvoot.testingandroid.View.MainActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Marvoot on 4/11/2016.
 */
public class ConfessionsViewModel implements ViewModel {

    private Context context;
    private Subscription subscription;
    private ConfessionService.DataListener dataListener;
    private List<Confession> confessions;

    public ConfessionsViewModel(Context context, ConfessionService.DataListener dataListener) {
        this.context = context;
        this.dataListener = dataListener;
    }

    public void setDataListener(ConfessionService.DataListener dataListener) {
        this.dataListener = dataListener;
    }

    @Override
    public void destroy() {
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        subscription = null;
        context = null;
        dataListener = null;
    }

    /*private static boolean isHttp404(Throwable error) {
        return error instanceof HttpException && ((HttpException) error).code() == 404;
    }*/

    public void userInteraction(int position, int direction) {
        Log.i("Tag", "position: " + position);
        ConfItemViewModel confItemViewModel = new ConfItemViewModel(context, confessions.get(position));
        String inter_type = confItemViewModel.getLoggedInUserInteractionType();
        if(direction == 1) {
            switch (inter_type) {
                case "-1": confItemViewModel.setLoggedInUserInteractionType(0); break;
                case "0": confItemViewModel.setLoggedInUserInteractionType(1); break;
                default: break;
            }
        } else if(direction == -1) {
            switch (inter_type) {
                case "1": confItemViewModel.setLoggedInUserInteractionType(0); break;
                case "0": confItemViewModel.setLoggedInUserInteractionType(-1); break;
                default: break;
            }
        }
        dataListener.userInteraction(position);
    }

    public void loadConfessions(final int lastConfId, int page, int count, String mode) {
        ConfApplication application = ConfApplication.get(context);
        final ConfessionService confessionService = application.getConfessionService();
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        ConfessionsFilter filter = new ConfessionsFilter(1, lastConfId, page, count, mode);
        subscription = confessionService.ListConfessions(filter)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new Subscriber<List<Confession>>() {
                    @Override
                    public void onCompleted() {
                        if (dataListener != null) {
                            if (lastConfId == -1) {
                                dataListener.onConfessionsChanged(confessions);
                            } else {
                                dataListener.onConfessionsAdded(confessions);
                            }
                        }
                        MainActivity.processing = false;
                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.e("Tag", "Error loading GitHub repos ", error);
                    }

                    @Override
                    public void onNext(List<Confession> confessions) {
                        Log.i("Tag", "Confessions loaded " + confessions);
                        ConfessionsViewModel.this.confessions = confessions;
                    }
                });
    }

    public void ForwardInteraction(int userId, int confId, int commentId){
        ConfApplication application = ConfApplication.get(context);
        final ConfessionService confessionService = application.getConfessionService();
        if(subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        UserInteraction inter = new UserInteraction(userId, confId, commentId);
        subscription = confessionService.ForwardInteraction(inter)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new Subscriber() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }

                });
    }

    public void BackwardInteraction(int userId, int confId, int commentId){
        ConfApplication application = ConfApplication.get(context);
        final ConfessionService confessionService = application.getConfessionService();
        if(subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        UserInteraction inter = new UserInteraction(userId, confId, commentId);
        subscription = confessionService.BackwardInteraction(inter)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new Subscriber() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
    }

}