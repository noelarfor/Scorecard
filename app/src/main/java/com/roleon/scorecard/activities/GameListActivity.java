package com.roleon.scorecard.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.roleon.scorecard.R;
import com.roleon.scorecard.adapters.GamesRecyclerAdapter;
import com.roleon.scorecard.helpers.AppHelper;
import com.roleon.scorecard.helpers.SimpleDividerItemDecoration;
import com.roleon.scorecard.model.Game;
import com.roleon.scorecard.sql.repo.GameRepo;

import java.util.ArrayList;
import java.util.List;

public class GameListActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerViewGames;
    private List<Game> listGames;
    private GamesRecyclerAdapter gamesRecyclerAdapter;
    private AppCompatButton appCompatButtonCreateGame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list);
        getSupportActionBar().hide();

        initViews();
        initObjects();
        initListeners();

    }

    private void initListeners() {
        appCompatButtonCreateGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.appCompatButtonCreateGame:
                createGame();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!AppHelper.shouldAllowOnBackPressed) {
            String userFromIntent = getIntent().getStringExtra("USER_NAME");
            Intent showScoreListIntent = new Intent(getApplicationContext(), ScoreListActivity.class);
            showScoreListIntent.putExtra("USER_NAME", userFromIntent);
            startActivity(showScoreListIntent);
        } else {
            super.onBackPressed();
        }
    }

    private void createGame() {
        Intent createGameIntent = new Intent(getApplicationContext(), CreateGameActivity.class);
        startActivity(createGameIntent);
    }

    private void initViews() {
        recyclerViewGames = (RecyclerView) findViewById(R.id.recyclerViewGames);
        appCompatButtonCreateGame = (AppCompatButton) findViewById(R.id.appCompatButtonCreateGame);
    }

    private void initObjects() {
        listGames = new ArrayList<>();
        gamesRecyclerAdapter = new GamesRecyclerAdapter(listGames);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewGames.setLayoutManager(mLayoutManager);
        recyclerViewGames.setItemAnimator(new DefaultItemAnimator());
        recyclerViewGames.setHasFixedSize(true);
        recyclerViewGames.setAdapter(gamesRecyclerAdapter);

        recyclerViewGames.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));

        getDataFromSQLite();
    }

    /**
     * This method is to fetch all user records from SQLite
     */
    private void getDataFromSQLite() {

        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                //listUsers.clear();
                listGames.addAll(GameRepo.getAllGame());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                gamesRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
