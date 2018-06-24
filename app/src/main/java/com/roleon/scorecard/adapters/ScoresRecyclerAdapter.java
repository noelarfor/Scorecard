package com.roleon.scorecard.adapters;

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
        //return new ScoresRecyclerAdapter.ScoreViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ScoresRecyclerAdapter.ScoreViewHolder holder, int position) {
        holder.textViewScoreName.setText(listScores.get(position).getScore_name());
        holder.textViewScoreTyp.setText(R.string.hint_fifa);
        holder.textViewScoreMode.setText(R.string.hint_not_implemented);
        holder.textViewLastUpdate.setText(listScores.get(position).getLast_update());
    }

    @Override
    public int getItemCount() {
        Log.v(ScoresRecyclerAdapter.class.getSimpleName(),""+listScores.size());
        return listScores.size();
    }

    /**
     * ViewHolder class
     */
    public class ScoreViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textViewScoreName;
        public AppCompatTextView textViewScoreTyp;
        public AppCompatTextView textViewScoreMode;
        public AppCompatTextView textViewLastUpdate;

        public ScoreViewHolder(View view) {
            super(view);
            textViewScoreName = (AppCompatTextView) view.findViewById(R.id.textViewScoreName);
            textViewScoreTyp = (AppCompatTextView) view.findViewById(R.id.textViewScoreTyp);
            textViewScoreMode = (AppCompatTextView) view.findViewById(R.id.textViewScoreMode);
            textViewLastUpdate = (AppCompatTextView) view.findViewById(R.id.textViewLastUpdate);
        }
    }

    public void setClickListener(View.OnClickListener callback) {
        mItemClickListener = callback;
    }

    public Score getItem(int position) {
        return listScores.get(position);
    }
}
