package com.roleon.scorecard.adapters;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roleon.scorecard.R;
import com.roleon.scorecard.model.Game;

import java.util.List;

public class GamesRecyclerAdapter extends RecyclerView.Adapter<GamesRecyclerAdapter.GameViewHolder> {

    private List<Game> listGames;

    public GamesRecyclerAdapter(List<Game> listGames) {
        this.listGames = listGames;
    }

    @Override
    public GamesRecyclerAdapter.GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game_recycler, parent, false);

        return new GameViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GamesRecyclerAdapter.GameViewHolder holder, int position) {
        holder.textViewGameName.setText(listGames.get(position).getGame_name());
        holder.textViewWinPoints.setText(Integer.toString(listGames.get(position).getWin_points()));
        holder.textViewLossPoints.setText(Integer.toString(listGames.get(position).getLoss_points()));
        holder.textViewDrawnPoints.setText(Integer.toString(listGames.get(position).getDrawn_points()));
    }

    @Override
    public int getItemCount() {
        Log.v(GamesRecyclerAdapter.class.getSimpleName(),""+listGames.size());
        return listGames.size();
    }

    public class GameViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textViewGameName;
        public AppCompatTextView textViewWinPoints;
        public AppCompatTextView textViewLossPoints;
        public AppCompatTextView textViewDrawnPoints;

        public GameViewHolder(View view) {
            super(view);
            textViewGameName = (AppCompatTextView) view.findViewById(R.id.textViewGameName);
            textViewWinPoints = (AppCompatTextView) view.findViewById(R.id.textViewWinPoints);
            textViewLossPoints = (AppCompatTextView) view.findViewById(R.id.textViewLossPoints);
            textViewDrawnPoints = (AppCompatTextView) view.findViewById(R.id.textViewDrawnPoints);
        }
    }
}
