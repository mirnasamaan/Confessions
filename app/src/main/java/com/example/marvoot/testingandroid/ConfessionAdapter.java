package com.example.marvoot.testingandroid;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.example.marvoot.testingandroid.Model.Confession;
import com.example.marvoot.testingandroid.View.MainActivity;
import com.example.marvoot.testingandroid.ViewModel.ConfItemViewModel;
import com.example.marvoot.testingandroid.ViewModel.ConfessionsViewModel;
import com.example.marvoot.testingandroid.databinding.NeutralConfessionBinding;

import java.util.Collections;
import java.util.List;

/**
 * Created by Marvoot on 4/11/2016.
 */
public class ConfessionAdapter extends RecyclerView.Adapter<ConfessionAdapter.ConfessionViewHolder> {
    private List<Confession> confessions;
    int count = 0;

    public ConfessionAdapter() {
        this.confessions = Collections.emptyList();
    }

    public ConfessionAdapter(List<Confession> confessions) {
        this.confessions = confessions;
    }

    public void clearConfessions() {
        this.confessions.clear();
    }

    public void setConfessions(List<Confession> confessions) {
        //this.confessions.clear();
        this.confessions = confessions;
    }

    public void addConfessions(List<Confession> confessions){
        this.confessions.addAll(confessions);
    }

    @Override
    public ConfessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NeutralConfessionBinding binding;
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.neutral_confession,
                parent,
                false);

        return new ConfessionViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return confessions.size();
    }

    @Override
    public void onBindViewHolder(final ConfessionViewHolder holder, final int position) {
        holder.bindConfession(confessions.get(position));

        int confId = confessions.get(position).ConfId;
        holder.neutral_binding.swipe.setTag(R.string.ConfId, confId);

        // Drag From Left
        holder.neutral_binding.swipe.addDrag(SwipeLayout.DragEdge.Left, holder.neutral_binding.bottomWrapper1);

        // Drag From Right
        holder.neutral_binding.swipe.addDrag(SwipeLayout.DragEdge.Right, holder.neutral_binding.bottomWrapper);

        holder.neutral_binding.swipe.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
                if(!MainActivity.processing) {
                    Log.e("OnUpdate left out", xvel+"");
                    Log.e("OnUpdate top out", yvel+"");
                    MainActivity.processing = true;
                    int confId = Integer.parseInt(holder.neutral_binding.swipe.getTag(R.string.ConfId).toString());
                    int direction = 0;
                    if(xvel == 0) {
                        direction = 0;
                        MainActivity.confessionsViewModel.userInteraction(holder.getAdapterPosition(), direction);
                        holder.neutral_binding.swipe.removeSwipeListener(this);
                    } else {
                        String drag = layout.getDragEdge().toString();
                        if (drag == "Left") {
                            MainActivity.confessionsViewModel.ForwardInteraction(1, confId, -1);//Web service
                            direction = 1;
                            MainActivity.confessionsViewModel.userInteraction(holder.getAdapterPosition(), direction);//Layout
                            holder.neutral_binding.swipe.removeSwipeListener(this);
                        } else if (drag == "Right") {
                            MainActivity.confessionsViewModel.BackwardInteraction(1, confId, -1);
                            direction = -1;
                            MainActivity.confessionsViewModel.userInteraction(holder.getAdapterPosition(), direction);
                            holder.neutral_binding.swipe.removeSwipeListener(this);
                        }
                    }
                    MainActivity.processing = false;
                }
            }
        });
    }

    public class ConfessionViewHolder extends RecyclerView.ViewHolder {
        NeutralConfessionBinding neutral_binding;

        public ConfessionViewHolder(NeutralConfessionBinding binding) {
            super(binding.swipe);
            this.neutral_binding = binding;
        }

        void  bindConfession(Confession confession) {

            if(neutral_binding.getViewModel() == null){
                neutral_binding.setViewModel(new ConfItemViewModel(itemView.getContext(), confession));
            } else {
                neutral_binding.getViewModel().setConfession(confession);
            }
        }
    }
}
