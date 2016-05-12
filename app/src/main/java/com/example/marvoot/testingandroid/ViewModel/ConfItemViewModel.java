package com.example.marvoot.testingandroid.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.example.marvoot.testingandroid.Model.Confession;
import com.example.marvoot.testingandroid.PublicFunctions.DateTimeParsing;
import com.example.marvoot.testingandroid.R;
import com.example.marvoot.testingandroid.View.InnerConfessionActivity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Marvoot on 4/11/2016.
 */
public class ConfItemViewModel extends BaseObservable implements ViewModel {
    private Confession confession;
    private Context context;
    private Subscription subscription;

    public ConfItemViewModel(Context context, Confession confession) {
        this.confession = confession;
        this.context = context;
    }

    public String getConfId() {
        return ((Integer) confession.ConfId).toString();
    }

    public String getConfTitle() {
        return confession.ConfTitle;
    }

    public String getConfContent() {
        return confession.ConfContent;
    }

    public String getConfLocation() { return confession.ConfLocation; }

    public String getConfTimestamp() {
        return DateTimeParsing.parseDate(confession.ConfTimestamp);
    }

    public String getConfUsername() { return confession.ConfUsername; }

    public String getConfUserId() { return ((Integer) confession.ConfUserId).toString(); }

    public String getConfCatId() { return ((Integer) confession.ConfCatId).toString(); }

    public String getConfCommentsCount() { return ((Integer) confession.ConfCommentsCount).toString(); }

    public String getConfLikesCount() { return ((Integer) confession.ConfLikesCount).toString(); }

    public String getConfDislikesCount() { return ((Integer) confession.ConfDislikesCount).toString(); }

    public String getLoggedInUserInteractionType() { return ((Integer) confession.LoggedInUserInteractionType).toString(); }

    public void setLoggedInUserInteractionType(int type) {
        confession.LoggedInUserInteractionType = type;
    }

    public void setConfCommentsCount(int commentsCount) {
        confession.ConfCommentsCount = commentsCount;
    }

    public void setConfLikesCount(int likesCount) {
        confession.ConfLikesCount = likesCount;
    }

    public void setConfDislikesCount(int dislikesCount) {
        confession.ConfDislikesCount = dislikesCount;
    }

    /*public void onItemClick(View view) {
        context.startActivity(RepositoryActivity.newIntent(context, repository));
    }*/

    // Allows recycling ItemRepoViewModels within the recyclerview adapter
    public void setConfession(Confession confession) {
        this.confession = confession;
        notifyChange();
    }

    @Override
    public void destroy() {
        //In this case destroy doesn't need to do anything because there is not async calls
    }

    public void goToConfession(View view) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View convertView = mInflater.inflate(R.layout.inner_confession, null);
        Intent intent = new Intent(convertView.getContext(), InnerConfessionActivity.class);
        int confId = Integer.parseInt(view.getTag(R.string.ConfId).toString());
        intent.putExtra("ConfId", confId);
        view.getContext().startActivity(intent);
    }
}
