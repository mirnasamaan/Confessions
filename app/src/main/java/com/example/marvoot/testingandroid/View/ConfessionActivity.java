package com.example.marvoot.testingandroid.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.databinding.DataBindingUtil;
import android.view.View;


import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.marvoot.testingandroid.ConfessionAdapter;
import com.example.marvoot.testingandroid.Model.Confession;
import com.example.marvoot.testingandroid.Model.ConfessionService;
import com.example.marvoot.testingandroid.Model.UserData;
import com.example.marvoot.testingandroid.PublicFunctions.SessionManagement;
import com.example.marvoot.testingandroid.R;
import com.example.marvoot.testingandroid.ViewModel.ConfessionsViewModel;
import com.example.marvoot.testingandroid.databinding.ActivityConfessionsBinding;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

public class ConfessionActivity extends AppCompatActivity implements ConfessionService.DataListener, SwipeRefreshLayout.OnRefreshListener {

    public static ConfessionsViewModel confessionsViewModel;
    private ActivityConfessionsBinding binding;
    private Subscription subscription;
    private static final String CONFESSION = "CONFESSION";
    SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Confession> confList;
    public static boolean processing = false;
    int page = 0;
    int count = 6;
    int lastConfId = -1;

    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    String[] mDrawerListItems;


    public static Intent newIntent(Context context, UserData userData) {
        Intent intent = new Intent(context, ConfessionActivity.class);
        intent.putExtra(CONFESSION, userData);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confessions);
        confessionsViewModel = new ConfessionsViewModel(this, this);
        binding.setViewModel(confessionsViewModel);

        mDrawerLayout = binding.drawerLayout;
        mDrawerList = binding.navList;
        mDrawerListItems = getResources().getStringArray(R.array.drawer_list);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDrawerListItems));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //int editedPosition = position + 1;
                //Toast.makeText(ConfessionActivity.this, "You selected item" + editedPosition, Toast.LENGTH_SHORT).show();
                switch (position){
                    case 0: {
                        Confession confession = new Confession();
                        startActivity(WriteConfessionActivity.newIntent(context, confession));
                        break;
                    }
                    case 1: {
                        UserData userData = new UserData();
                        context.startActivity(TermsActivity.newIntent(context, userData));
                        break;
                    }
                    case 2: {
                        SessionManagement new_session = new SessionManagement(context);
                        new_session.logoutUser();
                        UserData userData = new UserData();
                        context.startActivity(MainActivity.newIntent(context, userData));
                        ((Activity) context).finish();
                        break;
                    }
                    default:
                }
                mDrawerLayout.closeDrawer(mDrawerList);

            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                binding.toolbar,
                R.string.drawer_open,
                R.string.drawer_close){
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
                invalidateOptionsMenu();
                syncState();
            }
            public void onDrawerOpened(View v){
                super.onDrawerOpened(v);
                invalidateOptionsMenu();
                syncState();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        /*CoordinatorLayout co_layout = binding.coordinatorLayout;
        final Snackbar snackbar = Snackbar.make(co_layout, "", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View snackView = mInflater.inflate(R.layout.write_confession, null);*/
        //ImageView imageView = (ImageView) snackView.findViewById(R.id.image);
        //imageView.setImageBitmap(image);
        //TextView textViewTop = (TextView) snackView.findViewById(R.id.text);
        //textViewTop.setText(text);
        //textViewTop.setTextColor(Color.WHITE);
        ///layout.addView(snackView, 0);
        //snackbar.show();
        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Confession confession = new Confession();
                startActivity(WriteConfessionActivity.newIntent(context, confession));
                /*if (snackbar.isShown()) {
                    snackbar.dismiss();
                }
                else {
                    snackbar.show();
                }*/

            }
        });
        mDrawerToggle.syncState();


        setupRecyclerView((RecyclerView) findViewById(R.id.recycler_view));
        if(!processing){
            processing=true;
            confessionsViewModel.loadConfessions(lastConfId, 0, count, "latest");
        }

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        setupRecyclerView(binding.recyclerView);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();
        String yes = "yes";
        String load = intent.getStringExtra("load");
        if (load != null) {
            if (load.equals(yes)) {
                onRefresh();
            }
        }
        /*if (intent.getBundleExtra("load") != null) {
            String load = intent.getBundleExtra("load").toString();
            if (load == "yes") {
                onRefresh();
            }
        }*/
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null)
            setIntent(intent);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
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
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.refresh) {
            return true;
        }*/
        switch (item.getItemId()){
            case android.R.id.home: {
                if (mDrawerLayout.isDrawerOpen(mDrawerList)){
                    mDrawerLayout.closeDrawer(mDrawerList);
                } else {
                    mDrawerLayout.openDrawer(mDrawerList);
                }
                break;
            }
            case R.id.refresh: {
                onRefresh();
                break;
            }
            default: return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        page = 0;
        lastConfId = -1;
        if(!processing) {
            processing = true;
            confessionsViewModel.loadConfessions(lastConfId, 0, count, "latest");
        }
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onConfessionsChanged(List<Confession> fetchedConfessions) {
        ConfessionAdapter adapter = (ConfessionAdapter) binding.recyclerView.getAdapter();
        adapter.clearConfessions();
        adapter.setConfessions(fetchedConfessions);
        //adapter.notifyDataSetChanged();
        binding.recyclerView.setAdapter(adapter);
        //adapter.notifyItemRangeChanged(0, adapter.getItemCount());
    }

    public void onConfessionsAdded(List<Confession> confessions) {
        ConfessionAdapter adapter = (ConfessionAdapter) binding.recyclerView.getAdapter();
        adapter.addConfessions(confessions);
        adapter.notifyItemRangeChanged(adapter.getItemCount(), count);
    }

    public void userInteraction(int position) {
        try {
            ConfessionAdapter adapter = (ConfessionAdapter) binding.recyclerView.getAdapter();
            adapter.notifyItemChanged(position);
            Log.i("userInteraction pos", position+"");
        }
        catch(Exception ex)
        {
            Log.e("userInteraction err", ex.toString());
        }
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        ConfessionAdapter adapter = new ConfessionAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int last = layoutManager.getItemCount();
                int last_visible = layoutManager.findLastVisibleItemPosition();
                if (last == last_visible + 1) {
                    View lastItem = layoutManager.findViewByPosition(last_visible);
                    lastConfId = Integer.parseInt(lastItem.getTag(R.string.ConfId).toString());

                    page++;
                    if (!processing) {
                        processing = true;
                        confessionsViewModel.loadConfessions(lastConfId, 0, count, "latest");
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
}
