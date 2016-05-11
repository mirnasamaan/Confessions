package com.example.marvoot.testingandroid;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.daimajia.swipe.SwipeLayout;
import com.example.marvoot.testingandroid.Model.Comment;
import com.example.marvoot.testingandroid.Model.Confession;
import com.example.marvoot.testingandroid.View.ConfessionActivity;
import com.example.marvoot.testingandroid.ViewModel.CommentItemViewModel;
import com.example.marvoot.testingandroid.ViewModel.ConfItemViewModel;
import com.example.marvoot.testingandroid.databinding.CommentItemBinding;
import com.example.marvoot.testingandroid.databinding.NeutralConfessionBinding;

import java.util.Collections;
import java.util.List;

/**
 * Created by Marvoot on 4/11/2016.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> comments;
    int count = 0;

    public CommentAdapter() {
        this.comments = Collections.emptyList();
    }

    public CommentAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    public void clearComments() {
        this.comments.clear();
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComments(List<Comment> comments){
        this.comments.addAll(comments);
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommentItemBinding binding;
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.comment_item,
                parent,
                false);

        return new CommentViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @Override
    public void onBindViewHolder(final CommentViewHolder holder, final int position) {
        holder.bindComment(comments.get(position));

        int commentId = comments.get(position).CommentId;
        holder.commentItemBinding.commentlistitem.setTag(R.string.CommentId, commentId);
        holder.commentItemBinding.commentlistitem.setTag(R.string.CommentId, commentId);
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        CommentItemBinding commentItemBinding;

        public CommentViewHolder(CommentItemBinding binding) {
            super(binding.commentlistitem);
            this.commentItemBinding = binding;
        }

        void  bindComment(Comment comment) {

            if(commentItemBinding.getViewModel() == null){
                commentItemBinding.setViewModel(new CommentItemViewModel(itemView.getContext(), comment));
            } else {
                commentItemBinding.getViewModel().setComment(comment);
            }
        }
    }
}
