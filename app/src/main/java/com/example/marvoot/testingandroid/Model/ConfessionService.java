package com.example.marvoot.testingandroid.Model;

import android.databinding.ObservableInt;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Marvoot on 4/12/2016.
 */
public interface ConfessionService {

    @POST("Confession/ListConfessions")
    Observable<List<Confession>> ListConfessions(@Body ConfessionsFilter filter);

    @POST("Interaction/ForwardInteraction")
    Observable<List<Confession>> ForwardInteraction(@Body UserInteraction interaction);

    @POST("Interaction/BackwardInteraction")
    Observable<List<Confession>> BackwardInteraction(@Body UserInteraction interaction);

    /*@POST("Interaction/CheckUserInteraction")
    Observable<CheckUserInteraction> CheckUserInteraction(@Body UserInteraction interaction);*/

    class Factory {
        public static ConfessionService create() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.8/ConfAPI/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(ConfessionService.class);
        }
    }

    public interface DataListener {
        void onConfessionsChanged(List<Confession> confessions);
        void onConfessionsAdded(List<Confession> confessions);
        void userInteraction(int position);
    }
}
