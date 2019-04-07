package com.roleon.scorecard.adapters;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roleon.scorecard.R;
import com.roleon.scorecard.model.User;

import java.util.List;

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.UserViewHolder> {

    private List<User> listUsers;

    public UsersRecyclerAdapter(List<User> listUsers) {
        this.listUsers = listUsers;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_recycler, parent, false);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.textViewName.setText(listUsers.get(position).getName());
        holder.textViewPassword.setText(listUsers.get(position).getPassword());
        holder.textViewDate.setText(listUsers.get(position).getCreated_at());
        if (listUsers.get(position).getSyncStatus() == 0)
            holder.imageViewStatus.setImageResource(R.drawable.stopwatch);
        else
            holder.imageViewStatus.setImageResource(R.drawable.success);
    }

    @Override
    public int getItemCount() {
        Log.v(UsersRecyclerAdapter.class.getSimpleName(),""+listUsers.size());
        return listUsers.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textViewName;
        public AppCompatTextView textViewPassword;
        public AppCompatTextView textViewDate;
        public AppCompatImageView imageViewStatus;

        public UserViewHolder(View view) {
            super(view);
            textViewName = (AppCompatTextView) view.findViewById(R.id.textViewName);
            textViewPassword = (AppCompatTextView) view.findViewById(R.id.textViewPassword);
            textViewDate = (AppCompatTextView) view.findViewById(R.id.textViewDate);
            imageViewStatus = (AppCompatImageView) view.findViewById(R.id.imageSyncStatusUser);
        }
    }
}
