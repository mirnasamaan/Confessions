package com.example.marvoot.testingandroid.ViewModel;

import android.content.Context;
import android.databinding.ObservableField;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.marvoot.testingandroid.R;

import java.util.List;

import rx.Subscription;

/**
 * Created by Marvoot on 4/11/2016.
 */
public class MainViewModel implements ViewModel {

        public ObservableField<String> infoMessage;

        private Context context;
        private Subscription subscription;
        //private DataListener dataListener;

        public MainViewModel(Context context, DataListener dataListener) {
            this.context = context;
            //this.dataListener = dataListener;
            infoMessage = new ObservableField<>("Marvoot was here");
        }

        public void setDataListener(DataListener dataListener) {
            //this.dataListener = dataListener;
        }

        @Override
        public void destroy() {
            if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
            subscription = null;
            context = null;
            //dataListener = null;
        }

        /*private static boolean isHttp404(Throwable error) {
            return error instanceof HttpException && ((HttpException) error).code() == 404;
        }*/

        public interface DataListener {
            //void onRepositoriesChanged(List<Repository> repositories);
        }
    }