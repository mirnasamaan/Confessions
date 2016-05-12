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
import com.example.marvoot.testingandroid.View.ConfessionActivity;
import com.example.marvoot.testingandroid.View.MainActivity;
import com.example.marvoot.testingandroid.ViewModel.ConfItemViewModel;
import com.example.marvoot.testingandroid.ViewModel.ConfessionsViewModel;
import com.example.marvoot.testingandroid.databinding.NeutralConfessionBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by Marvoot on 4/11/2016.
 */
public class ConfessionAdapter extends RecyclerView.Adapter<ConfessionAdapter.ConfessionViewHolder> {
    private List<Confession> confessions;
    int count = 0;
    //private ArrayList<NameValuePair> swipeListeners;
    boolean mirvatFlag;
    private HashMap<Integer, SwipeLayout.SwipeListener> swipeListeners;

    public ConfessionAdapter() {
        this.confessions = Collections.emptyList();
        this.swipeListeners = new HashMap<Integer, SwipeLayout.SwipeListener>();
    }

    public ConfessionAdapter(List<Confession> confessions) {
        this.confessions = confessions;
    }

    public void clearConfessions() {
        this.confessions.clear();
        this.swipeListeners.clear();
        this.mirvatFlag = false;
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
        boolean hasSwipeListner = false;
        final int confId = confessions.get(position).ConfId;
        holder.neutral_binding.listitem.setTag(R.string.ConfId, confId);
        holder.neutral_binding.swipe.setTag(R.string.ConfId, confId);

        // Drag From Left
        holder.neutral_binding.swipe.addDrag(SwipeLayout.DragEdge.Left, holder.neutral_binding.bottomWrapper1);

        // Drag From Right
        holder.neutral_binding.swipe.addDrag(SwipeLayout.DragEdge.Right, holder.neutral_binding.bottomWrapper);

        for (Map.Entry<Integer, SwipeLayout.SwipeListener> pair : swipeListeners.entrySet())
            if (pair.getKey() == confId)
                hasSwipeListner = true;

        //if(!hasSwipeListner) {
            Log.i("AddingSwipeListner:Con ", confId + "");
            SwipeLayout.SwipeListener mirvat = new SwipeLayout.SwipeListener() {
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
                    if (!ConfessionActivity.processing) {
                        Log.i("processing semaphore = ", ConfessionActivity.processing + "");
                        Log.i("xvel = ", xvel + "");
                        ConfessionActivity.processing = true;
                        int confId = Integer.parseInt(holder.neutral_binding.listitem.getTag(R.string.ConfId).toString());
                        Log.i("confid = ", confId + "");
                        int direction = 0;
                        //Log.e("xvel = ", Float.toString(xvel));
                        if (xvel == 0) {
                            ConfessionActivity.processing = false;
                        /*Log.i("xvel value: ", Float.toString(xvel));*/
                            direction = 0;
                            ConfessionActivity.confessionsViewModel.userInteraction(holder.getAdapterPosition(), direction);
                       /* holder.neutral_binding.swipe.removeSwipeListener(this);*/
                            return;

                        } else {
                            String drag = layout.getDragEdge().toString();
                            if (drag == "Left") {
                            /*if(xvel == 0) {
                                ConfessionActivity.processing = false;
                                return;
                            }*/
                                ConfessionActivity.confessionsViewModel.ForwardInteraction(1, confId, -1);//Web service
                                direction = 1;
                                ConfessionActivity.confessionsViewModel.userInteraction(holder.getAdapterPosition(), direction);//Layout
                                //holder.neutral_binding.swipe.removeSwipeListener(this);
                            } else if (drag == "Right") {
                            /*if(xvel == 0) {
                                ConfessionActivity.processing = false;
                                return;
                            }*/
                                ConfessionActivity.confessionsViewModel.BackwardInteraction(1, confId, -1);
                                //direction=xvel == 0?0:-1;
                                direction = -1;
                                ConfessionActivity.confessionsViewModel.userInteraction(holder.getAdapterPosition(), direction);
                                //holder.neutral_binding.swipe.removeSwipeListener(this);
                            }
                        }
                        //ConfessionActivity.processing = false;
                    }
                }
            };
            holder.neutral_binding.swipe.removeSwipeListener(mirvat);
            holder.neutral_binding.swipe.addSwipeListener(mirvat);
        if(!hasSwipeListner) {
            swipeListeners.put(confId, mirvat);
        }
        //}
        holder.neutral_binding.swipe.addSwipeListener(mirvat);
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
