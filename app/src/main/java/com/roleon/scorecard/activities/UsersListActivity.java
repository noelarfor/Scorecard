package com.roleon.scorecard.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.roleon.scorecard.R;
import com.roleon.scorecard.adapters.UsersRecyclerAdapter;
import com.roleon.scorecard.helpers.SimpleDividerItemDecoration;
import com.roleon.scorecard.model.User;
import com.roleon.scorecard.sql.repo.UserRepo;

import java.util.ArrayList;
import java.util.List;

public class UsersListActivity extends AppCompatActivity {

    private AppCompatTextView textViewName;
    private RecyclerView recyclerViewUsers;
    private List<User> listUsers;
    private UsersRecyclerAdapter usersRecyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        getSupportActionBar().hide();

        initViews();
        initObjects();

    }

    private void initViews() {
        textViewName = (AppCompatTextView) findViewById(R.id.textViewName);
        recyclerViewUsers = (RecyclerView) findViewById(R.id.recyclerViewUsers);
    }

    private void initObjects() {
        listUsers = new ArrayList<>();
        usersRecyclerAdapter = new UsersRecyclerAdapter(listUsers);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewUsers.setLayoutManager(mLayoutManager);
        recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setAdapter(usersRecyclerAdapter);

        recyclerViewUsers.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));

        String userFromIntent = getIntent().getStringExtra("USER_NAME");
        textViewName.setText(userFromIntent);

        getDataFromSQLite();
    }

    private void getDataFromSQLite() {

        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                //listUsers.clear();
                listUsers.addAll(UserRepo.getAllUser());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                usersRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
