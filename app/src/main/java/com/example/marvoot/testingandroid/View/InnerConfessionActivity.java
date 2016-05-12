package com.example.marvoot.testingandroid.View;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.marvoot.testingandroid.CommentAdapter;
import com.example.marvoot.testingandroid.R;
import com.example.marvoot.testingandroid.ViewModel.InnerConfessionViewModel;
import com.example.marvoot.testingandroid.databinding.InnerConfessionBinding;

/**
 * Created by Marvoot on 5/8/2016.
 */
public class InnerConfessionActivity extends AppCompatActivity {
    private static InnerConfessionBinding binding;
    public InnerConfessionViewModel innerConfessionViewModel;
    int count = 6;
    public static boolean processing = false;
    int lastCommentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.inner_confession);
        innerConfessionViewModel = new InnerConfessionViewModel(this);
        binding.setViewModel(innerConfessionViewModel);
        int confId = getIntent().getIntExtra("ConfId", -1);
        innerConfessionViewModel.getConfessionById(confId);
        setupRecyclerView(binding.commentsRecyclerView);
        innerConfessionViewModel.loadComments(lastCommentId, confId, count);

    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        CommentAdapter adapter = new CommentAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.commentsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                /*LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int count = layoutManager.getItemCount();
                int last_visible = layoutManager.findLastVisibleItemPosition();
                if (count == last_visible + 1) {
                    View lastItem = layoutManager.findViewByPosition(count - 1);
                    lastConfId = Integer.parseInt(lastItem.getTag(R.string.ConfId).toString());

                    page++;
                    if (!processing) {
                        processing = true;
                        confessionsViewModel.loadConfessions(lastConfId, 0, count, "latest");
                        binding.recyclerView.stopScroll();
                    }
                }*/
            }
        });
    }
}
