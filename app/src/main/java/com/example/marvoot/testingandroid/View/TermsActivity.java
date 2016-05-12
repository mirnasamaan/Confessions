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
import com.example.marvoot.testingandroid.ViewModel.TermsViewModel;
import com.example.marvoot.testingandroid.databinding.RegisterLayoutBinding;
import com.example.marvoot.testingandroid.databinding.TermsAndConditionsBinding;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class TermsActivity extends AppCompatActivity implements TermsViewModel.DataListener {


    private TermsAndConditionsBinding binding;
    private TermsViewModel termsViewModel;
    private static final String TERMS = "TERMS";


    public static Intent newIntent(Context context, UserData userData) {
        Intent intent = new Intent(context, TermsActivity.class);
        intent.putExtra(TERMS, userData);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.terms_and_conditions);
        termsViewModel = new TermsViewModel(this);
        binding.setViewModel(termsViewModel);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.refresh);
        item.setVisible(false);
        this.invalidateOptionsMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.refresh) {
            return true;
        }*/
        switch (item.getItemId()){
            case android.R.id.home: {
                onBackPressed();
            }
            default: return super.onOptionsItemSelected(item);
        }

        //return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUserDataChanged(UserData userdata) {
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }





}


