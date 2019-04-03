package com.roleon.scorecard.adapters;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roleon.scorecard.R;
import com.roleon.scorecard.model.Result;

import java.util.List;

public class ResultsRecyclerAdapter extends RecyclerView.Adapter<ResultsRecyclerAdapter.ResultViewHolder> {

    private List<Result> listResults;

    public ResultsRecyclerAdapter(List<Result> listResults) {
        this.listResults = listResults;
    }

    @Override
    public ResultsRecyclerAdapter.ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_result_recycler, parent, false);

         return new ResultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResultsRecyclerAdapter.ResultViewHolder holder, int position) {

        holder.textViewUserNameResultList.setText(listResults.get(position).getUser_name());
        holder.textViewResultWin.setText(Integer.toString(listResults.get(position).getResult_win()));
        holder.textViewResultDrawn.setText(Integer.toString(listResults.get(position).getResult_drawn()));
        holder.textViewResultLoss.setText(Integer.toString(listResults.get(position).getResult_loss()));
        holder.textViewResultDiff.setText(Integer.toString(listResults.get(position).getResult_diff()));
        holder.textViewResultPoints.setText(Integer.toString(listResults.get(position).getResult_points()));
    }

    @Override
    public int getItemCount() {
        Log.v(ResultsRecyclerAdapter.class.getSimpleName(),"" + listResults.size());
        return listResults.size();
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textViewUserNameResultList;
        public AppCompatTextView textViewResultWin;
        public AppCompatTextView textViewResultDrawn;
        public AppCompatTextView textViewResultLoss;
        public AppCompatTextView textViewResultDiff;
        public AppCompatTextView textViewResultPoints;

        public ResultViewHolder(View view) {
            super(view);
            textViewUserNameResultList = (AppCompatTextView) view.findViewById(R.id.textViewUserNameResultList);
            textViewResultWin = (AppCompatTextView) view.findViewById(R.id.textViewResultWin);
            textViewResultDrawn = (AppCompatTextView) view.findViewById(R.id.textViewResultDrawn);
            textViewResultLoss = (AppCompatTextView) view.findViewById(R.id.textViewResultLoss);
            textViewResultDiff = (AppCompatTextView) view.findViewById(R.id.textViewResultDiff);
            textViewResultPoints = (AppCompatTextView) view.findViewById(R.id.textViewResultPoints);
        }
    }

    public Result getItem(int position) {
        return listResults.get(position);
    }
}
