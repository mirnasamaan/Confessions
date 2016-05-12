package com.example.marvoot.testingandroid.ViewModel;

import android.content.Context;
import android.database.Observable;
import android.databinding.BaseObservable;
import android.util.Log;

import com.example.marvoot.testingandroid.ConfApplication;
import com.example.marvoot.testingandroid.Model.Comment;
import com.example.marvoot.testingandroid.Model.CommentsFilter;
import com.example.marvoot.testingandroid.Model.Confession;
import com.example.marvoot.testingandroid.Model.ConfessionId;
import com.example.marvoot.testingandroid.Model.ConfessionService;
import com.example.marvoot.testingandroid.Model.ConfessionsFilter;
import com.example.marvoot.testingandroid.PublicFunctions.DateTimeParsing;
import com.example.marvoot.testingandroid.View.ConfessionActivity;
import com.example.marvoot.testingandroid.View.InnerConfessionActivity;
import android.databinding.ObservableField;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Marvoot on 5/8/2016.
 */
public class InnerConfessionViewModel {
    private Context context;
    private Subscription subscription;
    private Confession confession;
    public ObservableField<Confession> confessionObservable = new ObservableField<Confession>();
    private List<Comment> addedComments;
    public ArrayList<Comment> allComments = new ArrayList<Comment>();

    public InnerConfessionViewModel(Context context) {
        this.context = context;
    }

    public String getConfTimestamp() {
        return DateTimeParsing.parseDate(confession.ConfTimestamp);
    }

    public void getConfessionById(final int confId) {
        final ConfApplication application = ConfApplication.get(context);
        final ConfessionService confessionService = application.getConfessionService();
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();

        ConfessionId confessionId = new ConfessionId(confId, 1);
        subscription = confessionService.GetConfessionById(confessionId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new Subscriber<Confession>() {
                    @Override
                    public void onCompleted() {
                        confessionObservable.set(confession);
                        InnerConfessionActivity.processing = false;
                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.e("Tag", "Error loading GitHub repos ", error);
                    }

                    @Override
                    public void onNext(Confession conf) {
                        //Log.i("Tag", "Confessions loaded " + confessions);
                        conf.ConfTimestamp = DateTimeParsing.parseDate(conf.ConfTimestamp);
                        InnerConfessionViewModel.this.confession = conf;
                    }
                });
    }

    public void loadComments(final int lastCommentId, int confessionId, int count) {
        ConfApplication application = ConfApplication.get(context);
        final ConfessionService confessionService = application.getConfessionService();
        //if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        CommentsFilter filter = new CommentsFilter(1, confessionId, lastCommentId, count);
        subscription = confessionService.ListComments(filter)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new Subscriber<List<Comment>>() {
                    @Override
                    public void onCompleted() {
                        /*if (dataListener != null) {
                            if (lastConfId == -1) {
                                //ArrayList<Confession> fetchedConfessions = new ArrayList<Confession>(allConfessions);
                                dataListener.onConfessionsChanged(new ArrayList<Confession>(allConfessions));
                            } else {
                                dataListener.onConfessionsAdded(addedConfessions);
                            }
                        }*/
                        InnerConfessionActivity.processing = false;
                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.e("Tag", "Error loading GitHub repos ", error);
                    }

                    @Override
                    public void onNext(List<Comment> comments) {
                        InnerConfessionViewModel.this.addedComments = comments;
                        if(lastCommentId == -1) InnerConfessionViewModel.this.allComments.clear();
                        InnerConfessionViewModel.this.allComments.addAll(comments);
                    }
                });
    }
}
