package com.example.marvoot.testingandroid.View;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marvoot.testingandroid.Model.UserData;
import com.example.marvoot.testingandroid.PublicFunctions.SessionManagement;
import com.example.marvoot.testingandroid.R;
import com.example.marvoot.testingandroid.ViewModel.MainViewModel;
import com.example.marvoot.testingandroid.databinding.ActivityMainBinding;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity implements MainViewModel.DataListener {


    private ActivityMainBinding binding;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionManagement new_session = new SessionManagement(this);
        new_session.logoutUser();
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap = new_session.getUserDetails();
        String empty = "";
        if (hashMap.get("name") != null && !hashMap.get("name").equals(empty) && hashMap.get("email") != null && !hashMap.get("email").equals(empty)) {
            /*CharSequence text = "You should be directed to the confessions list";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();*/
            UserData userData = new UserData();
            userData.username = hashMap.get("name");
            userData.password = hashMap.get("email");
            this.startActivity(ConfessionActivity.newIntent(this, userData));
            this.finish();
        }
        else {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
            mainViewModel = new MainViewModel(this, this);
            binding.setViewModel(mainViewModel);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUserDataChanged(UserData userdata) {
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }





}


