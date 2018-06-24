package com.roleon.scorecard.activities;

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
import android.widget.Toast;

import com.roleon.scorecard.R;
import com.roleon.scorecard.adapters.ScoresRecyclerAdapter;
import com.roleon.scorecard.helpers.SimpleDividerItemDecoration;
import com.roleon.scorecard.model.Score;
import com.roleon.scorecard.sql.repo.ScoreRepo;

import java.util.ArrayList;
import java.util.List;

public class ScoreListActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatTextView textViewUserName;
    private RecyclerView recyclerViewScores;
    private List<Score> listScores;
    private ScoresRecyclerAdapter scoresRecyclerAdapter;

    private AppCompatButton appCompatButtonCreateGame;
    private Score score;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores_list);
        getSupportActionBar().hide();

        initViews();
        initObjects();
        initListeners();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        textViewUserName = (AppCompatTextView) findViewById(R.id.textViewUserName);
        recyclerViewScores = (RecyclerView) findViewById(R.id.recyclerViewScores);

        appCompatButtonCreateGame = (AppCompatButton) findViewById(R.id.appCompatButtonCreateGame);
    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonCreateGame.setOnClickListener(this);
        recyclerViewScores.setOnClickListener(this);
        scoresRecyclerAdapter.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = recyclerViewScores.indexOfChild(v);
                score = scoresRecyclerAdapter.getItem(pos);

                Toast.makeText(ScoreListActivity.this, "On item clicked" + score.getScore_name(),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * This method is to initialize objects to be used
     */
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

        String userFromIntent = getIntent().getStringExtra("USER_NAME");
        textViewUserName.setText(userFromIntent);

        getDataFromSQLite();
    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonCreateGame:
                Toast.makeText(ScoreListActivity.this, "Create Game",
                        Toast.LENGTH_SHORT).show();
                break;
            /*case R.id.recyclerViewScores:
                int pos = recyclerViewScores.indexOfChild(v);
                score = scoresRecyclerAdapter.getItem(pos);

                Toast.makeText(ScoreListActivity.this, "On item clicked" + score.getScore_name(),
                        Toast.LENGTH_SHORT).show();*/
        }
    }

    /**
     * This method is to fetch all user records from SQLite
     */
    private void getDataFromSQLite() {

        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listScores.clear();
                listScores.addAll(ScoreRepo.getAllScore());

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

