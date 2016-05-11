package com.example.marvoot.testingandroid.ViewModel;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import com.example.marvoot.testingandroid.ConfApplication;
import com.example.marvoot.testingandroid.ConfessionAdapter;
import com.example.marvoot.testingandroid.Model.UserInteraction;
import com.example.marvoot.testingandroid.Model.Confession;
import com.example.marvoot.testingandroid.Model.ConfessionService;
import com.example.marvoot.testingandroid.Model.ConfessionsFilter;
import com.example.marvoot.testingandroid.View.ConfessionActivity;

import java.util.ArrayList;
import java.util.List;

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
    private List<Confession> addedConfessions;
    public ArrayList<Confession> allConfessions = new ArrayList<Confession>();

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
        try {
            ConfItemViewModel confItemViewModel = new ConfItemViewModel(context, this.allConfessions.get(position));
            String inter_type = confItemViewModel.getLoggedInUserInteractionType();
            if (direction == 1) {
                switch (inter_type) {
                    case "-1":
                        confItemViewModel.setLoggedInUserInteractionType(0);
                        int dislikesCount = Integer.parseInt(confItemViewModel.getConfDislikesCount());
                        confItemViewModel.setConfDislikesCount(dislikesCount - 1);
                        break;
                    case "0":
                        confItemViewModel.setLoggedInUserInteractionType(1);
                        int likesCount = Integer.parseInt(confItemViewModel.getConfLikesCount());
                        confItemViewModel.setConfLikesCount(likesCount + 1);
                        break;
                    default:
                        break;
                }
            } else if (direction == -1) {
                switch (inter_type) {
                    case "1":
                        confItemViewModel.setLoggedInUserInteractionType(0);
                        int likesCount = Integer.parseInt(confItemViewModel.getConfLikesCount());
                        confItemViewModel.setConfLikesCount(likesCount - 1);
                        break;
                    case "0":
                        confItemViewModel.setLoggedInUserInteractionType(-1);
                        int dislikesCount = Integer.parseInt(confItemViewModel.getConfDislikesCount());
                        confItemViewModel.setConfDislikesCount(dislikesCount + 1);
                        break;
                    default:
                        break;
                }
            }
            dataListener.userInteraction(position);
        }
        catch (Exception ex){
            Log.e("userInteractionError", ex.getMessage());
            Log.e("userInteractionConfSize", this.allConfessions.size() + "");
            //Log.e("userInteractionConf", this.confessions.get(0).ConfContent);
        }
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
                            if (addedConfessions.size() > 0) {
                                if (dataListener != null) {
                                    if (lastConfId == -1) {
                                        dataListener.onConfessionsChanged(new ArrayList<Confession>(allConfessions));
                                    } else {
                                        dataListener.onConfessionsAdded(addedConfessions);
                                        for (int i = 0 ; i< addedConfessions.size(); i++) {
                                            Log.i("AddedConfessions: " , addedConfessions.get(i).getConfId());
                                        }
                                    }
                                }
                            }
                        ConfessionActivity.atrees = false;
                        ConfessionActivity.processing = false;
                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.e("Tag", "Error loading GitHub repos ", error);
                    }

                    @Override
                    public void onNext(List<Confession> confessions) {
                        //Log.i("Tag", "Confessions loaded " + confessions);
                        ConfessionsViewModel.this.addedConfessions = confessions;
                        if (confessions.size() > 0) {

                            if (lastConfId == -1) ConfessionsViewModel.this.allConfessions.clear();
                            ConfessionsViewModel.this.allConfessions.addAll(confessions);
                        } else {
                            ConfessionActivity.meratAtrees = true;
                        }
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
                        ConfessionActivity.processing = false;
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
                        ConfessionActivity.processing = false;
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