package com.roleon.scorecard.adapters;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roleon.scorecard.R;
import com.roleon.scorecard.model.Score;

import java.util.List;

public class ScoresRecyclerAdapter extends RecyclerView.Adapter<ScoresRecyclerAdapter.ScoreViewHolder> {

    private List<Score> listScores;
    private View.OnClickListener mItemClickListener;

    public ScoresRecyclerAdapter(List<Score> listScores) {
        this.listScores = listScores;
    }

    @Override
    public ScoresRecyclerAdapter.ScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_score_recycler, parent, false);

        // set Listener on Item click
        ScoresRecyclerAdapter.ScoreViewHolder holder = new ScoreViewHolder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onClick(view);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ScoresRecyclerAdapter.ScoreViewHolder holder, int position) {
        holder.textViewScoreName.setText(listScores.get(position).getScore_name());
        holder.textViewScoreTyp.setText(Integer.toString(listScores.get(position).getScore_typ()));
        holder.textViewScoreMode.setText(Integer.toString(listScores.get(position).getScore_mode()));
        holder.textViewNumUsers.setText(Integer.toString(listScores.get(position).getNum_users()));
        holder.textViewLastUpdate.setText(listScores.get(position).getLast_update());
        if (listScores.get(position).getSyncStatus() == 0) {
            Log.d("SCORECARD_VIEW: ", "sync status " + listScores.get(position).getSyncStatus());
            holder.imageViewStatus.setImageResource(R.drawable.stopwatch);
        } else {
            Log.d("SCORECARD_VIEW: ", "sync status " + listScores.get(position).getSyncStatus());
            holder.imageViewStatus.setImageResource(R.drawable.success);
        }
    }

    @Override
    public int getItemCount() {
        Log.v(ScoresRecyclerAdapter.class.getSimpleName(),"" + listScores.size());
        return listScores.size();
    }

    public class ScoreViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textViewScoreName;
        public AppCompatTextView textViewScoreTyp;
        public AppCompatTextView textViewScoreMode;
        public AppCompatTextView textViewLastUpdate;
        public AppCompatTextView textViewNumUsers;
        public AppCompatImageView imageViewStatus;

        public ScoreViewHolder(View view) {
            super(view);
            textViewScoreName = (AppCompatTextView) view.findViewById(R.id.textViewScoreName);
            textViewScoreTyp = (AppCompatTextView) view.findViewById(R.id.textViewScoreTyp);
            textViewScoreMode = (AppCompatTextView) view.findViewById(R.id.textViewScoreMode);
            textViewNumUsers = (AppCompatTextView) view.findViewById(R.id.textViewNumUsers);
            textViewLastUpdate = (AppCompatTextView) view.findViewById(R.id.textViewLastUpdate);
            imageViewStatus = (AppCompatImageView) view.findViewById(R.id.imageSyncStatusScore);
        }
    }

    public void setClickListener(View.OnClickListener callback) {
        mItemClickListener = callback;
    }

    public Score getItem(int position) {
        return listScores.get(position);
    }
}
