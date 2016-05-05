package com.example.marvoot.testingandroid.View;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.marvoot.testingandroid.Model.UserData;
import com.example.marvoot.testingandroid.R;
import com.example.marvoot.testingandroid.ViewModel.RegisterViewModel;
import com.example.marvoot.testingandroid.databinding.RegisterLayoutBinding;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class RegisterActivity extends AppCompatActivity implements RegisterViewModel.DataListener {


    private RegisterLayoutBinding binding;
    private RegisterViewModel registerViewModel;
    private static final String REGISTER = "REGISTER";


    public static Intent newIntent(Context context, UserData userData) {
        Intent intent = new Intent(context, RegisterActivity.class);
        intent.putExtra(REGISTER, userData);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.register_layout);
        registerViewModel = new RegisterViewModel(this,this);
        binding.setViewModel(registerViewModel);

        /*Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));*/




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


