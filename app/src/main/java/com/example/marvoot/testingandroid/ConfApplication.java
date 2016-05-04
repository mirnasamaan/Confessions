package com.example.marvoot.testingandroid;

import android.app.Application;
import android.content.Context;

import com.example.marvoot.testingandroid.Model.ConfessionService;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by Marvoot on 4/12/2016.
 */
public class ConfApplication extends Application {
    private ConfessionService confessionService;
    private Scheduler defaultSubscribeScheduler;

    public static ConfApplication get(Context context) {
        return (ConfApplication) context.getApplicationContext();
    }

    public ConfessionService getConfessionService() {
        if (confessionService == null) {
            confessionService = ConfessionService.Factory.create();
        }
        return confessionService;
    }

    //For setting mocks during testing
    public void setConfessionService(ConfessionService confessionService) {
        this.confessionService = confessionService;
    }

    public Scheduler defaultSubscribeScheduler() {
        if (defaultSubscribeScheduler == null) {
            defaultSubscribeScheduler = Schedulers.io();
        }
        return defaultSubscribeScheduler;
    }

    //User to change scheduler from tests
    public void setDefaultSubscribeScheduler(Scheduler scheduler) {
        this.defaultSubscribeScheduler = scheduler;
    }
}
