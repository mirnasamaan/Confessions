package com.example.marvoot.testingandroid.View;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.example.marvoot.testingandroid.ConfessionAdapter;
import com.example.marvoot.testingandroid.Model.Confession;
import com.example.marvoot.testingandroid.Model.ConfessionService;
import com.example.marvoot.testingandroid.R;
import com.example.marvoot.testingandroid.ViewModel.ConfessionsViewModel;
import com.example.marvoot.testingandroid.databinding.ActivityConfessionsBinding;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

public class MainActivity extends AppCompatActivity implements ConfessionService.DataListener, SwipeRefreshLayout.OnRefreshListener {

    public static ConfessionsViewModel confessionsViewModel;
    private ActivityConfessionsBinding binding;
    private Subscription subscription;
    SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Confession> confList;
    public static boolean processing = false;
    int page = 0;
    int count = 6;
    int lastConfId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confessions);
        confessionsViewModel = new ConfessionsViewModel(this, this);
        binding.setViewModel(confessionsViewModel);
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
                if(last == last_visible + 1) {
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
