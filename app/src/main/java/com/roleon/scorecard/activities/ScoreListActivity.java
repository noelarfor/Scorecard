package com.roleon.scorecard.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.roleon.scorecard.R;
import com.roleon.scorecard.adapters.ScoresRecyclerAdapter;
import com.roleon.scorecard.helpers.AppHelper;
import com.roleon.scorecard.helpers.SimpleDividerItemDecoration;
import com.roleon.scorecard.model.Score;
import com.roleon.scorecard.sql.repo.ResultRepo;
import com.roleon.scorecard.sql.repo.ScoreRepo;

import java.util.ArrayList;
import java.util.List;

public class ScoreListActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatTextView textViewUserName;
    private RecyclerView recyclerViewScores;
    private List<Score> listScores;
    private ScoresRecyclerAdapter scoresRecyclerAdapter;

    private AppCompatButton appCompatButtonCreateScoreList;
    private AppCompatButton appCompatButtonUserList;
    private AppCompatButton appCompatButtonAddGame;
    private Score score;

    private List<String> scoreIdsOfUser;
    String userFromIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores_list);
        getSupportActionBar().hide();

        initViews();
        initObjects();
        initListeners();
    }

    private void initViews() {
        textViewUserName = (AppCompatTextView) findViewById(R.id.textViewUserName);
        recyclerViewScores = (RecyclerView) findViewById(R.id.recyclerViewScores);

        appCompatButtonCreateScoreList = (AppCompatButton) findViewById(R.id.appCompatButtonCreateScoreList);
        appCompatButtonUserList = (AppCompatButton) findViewById(R.id.appCompatButtonUserList);
        appCompatButtonAddGame = (AppCompatButton) findViewById(R.id.appCompatButtonAddGame);
    }

    private void initListeners() {
        appCompatButtonCreateScoreList.setOnClickListener(this);
        recyclerViewScores.setOnClickListener(this);
        appCompatButtonUserList.setOnClickListener(this);
        appCompatButtonAddGame.setOnClickListener(this);
        scoresRecyclerAdapter.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = recyclerViewScores.indexOfChild(v);
                score = scoresRecyclerAdapter.getItem(pos);
                showResultList(score);
                finish();
            }
        });
    }

    private void initObjects() {
        listScores = new ArrayList<>();
        scoresRecyclerAdapter = new ScoresRecyclerAdapter(listScores);
        score = new Score();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewScores.setLayoutManager(mLayoutManager);
        recyclerViewScores.setItemAnimator(new DefaultItemAnimator());
        recyclerViewScores.setHasFixedSize(true);
        recyclerViewScores.setAdapter(scoresRecyclerAdapter);

        recyclerViewScores.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));

        userFromIntent = getIntent().getStringExtra("USER_NAME");
        textViewUserName.setText(userFromIntent);
        scoreIdsOfUser = ResultRepo.getScoreIdsOfUser(userFromIntent);

        if (!userFromIntent.equals("Admin")) {
            appCompatButtonUserList.setVisibility(View.INVISIBLE);
            appCompatButtonAddGame.setVisibility(View.INVISIBLE);
        }

        getDataFromSQLite();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonCreateScoreList:
                createScore();
                break;
            case R.id.appCompatButtonUserList:
                showUserList();
                break;
            case R.id.appCompatButtonAddGame:
                gameInitMenu();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!AppHelper.shouldAllowOnBackPressed) {
            // do nothing
        } else {
            super.onBackPressed();
        }
    }

    private void showUserList() {
        Intent showUserListIntent = new Intent(getApplicationContext(), UsersListActivity.class);
        showUserListIntent.putExtra("USER_NAME", textViewUserName.getText().toString().trim());
        startActivity(showUserListIntent);
    }

    private void createScore() {
        Intent createScoreIntent = new Intent(getApplicationContext(), CreateScoreActivity.class);
        createScoreIntent.putExtra("USER_NAME", textViewUserName.getText().toString().trim());
        startActivity(createScoreIntent);
    }

    private void gameInitMenu() {
        Intent addGameIntent = new Intent(getApplicationContext(), GameListActivity.class);
        addGameIntent.putExtra("USER_NAME", textViewUserName.getText().toString().trim());
        startActivity(addGameIntent);
    }

    private void showResultList(Score score) {
        Intent showResultIntent = new Intent(getApplicationContext(), ResultListActivity.class);
        showResultIntent.putExtra("SCORE_ID", Integer.toString(score.getScore_Id()));
        startActivity(showResultIntent);
    }

    private void getDataFromSQLite() {

        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listScores.clear();
                if (userFromIntent.equals("Admin")) {
                    listScores.addAll(ScoreRepo.getAllScore());
                }
                else {
                    for (int i = 0; i < scoreIdsOfUser.size(); i++) {
                        listScores.add(ScoreRepo.getScoreById(scoreIdsOfUser.get(i)));
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                scoresRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
}

