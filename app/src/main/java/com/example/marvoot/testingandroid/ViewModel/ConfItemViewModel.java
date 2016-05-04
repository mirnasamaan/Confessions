package com.example.marvoot.testingandroid.ViewModel;

import android.content.Context;
import android.databinding.BaseObservable;

import com.example.marvoot.testingandroid.Model.Confession;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Marvoot on 4/11/2016.
 */
public class ConfItemViewModel extends BaseObservable implements ViewModel {
    private Confession confession;
    private Context context;

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
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date parsedDate = dateFormat.parse(confession.ConfTimestamp);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(timestamp);
            String result = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
            return result;
        }catch(Exception e) {
            return null;
        }
    }

    public String getConfUsername() { return confession.ConfUsername; }

    public String getConfUserId() { return ((Integer) confession.ConfUserId).toString(); }

    public String getConfCatId() { return ((Integer) confession.ConfCatId).toString(); }

    public String getConfCommentsCount() { return ((Integer) confession.ConfCommentsCount).toString(); }

    public String getConfLikesCount() { return ((Integer) confession.ConfLikesCount).toString(); }

    public String ConfDislikesCount() { return ((Integer) confession.ConfDislikesCount).toString(); }

    public String getLoggedInUserInteractionType() { return ((Integer) confession.LoggedInUserInteractionType).toString(); }

    public void setLoggedInUserInteractionType(int type) {
        confession.LoggedInUserInteractionType = type;
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
}
