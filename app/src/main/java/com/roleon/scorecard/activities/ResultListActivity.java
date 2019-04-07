package com.roleon.scorecard.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
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
import com.roleon.scorecard.adapters.ResultsRecyclerAdapter;
import com.roleon.scorecard.helpers.AppHelper;
import com.roleon.scorecard.helpers.NetworkStateChecker;
import com.roleon.scorecard.helpers.SimpleDividerItemDecoration;
import com.roleon.scorecard.model.Result;
import com.roleon.scorecard.sql.repo.ResultRepo;
import com.roleon.scorecard.sql.repo.ScoreRepo;

import java.util.ArrayList;
import java.util.List;

public class ResultListActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatTextView textViewUserNameResultList;
    private RecyclerView recyclerViewResults;
    private List<Result> listResults;
    private ResultsRecyclerAdapter resultsRecyclerAdapter;

    private AppCompatButton appCompatButtonAddResult;
    private AppCompatButton appCompatButtonShowScoreList;
    private String scoreIdFromIntend;

    private BroadcastReceiver broadcastReceiver;
    private BroadcastReceiver networkCheckerReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_list);
        getSupportActionBar().hide();

        initViews();
        initObjects();
        initListeners();
    }

    @Override
    public void onResume() {
        super.onResume();

        //registering the broadcast receiver to update sync status
        registerReceiver(broadcastReceiver, new IntentFilter(AppHelper.DATA_SAVED_BROADCAST));
        registerReceiver(networkCheckerReceiver, new IntentFilter((ConnectivityManager.CONNECTIVITY_ACTION)));
    }

    @Override
    public void onPause() {
        super.onPause();

        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(networkCheckerReceiver);
    }

    private void initViews() {
        recyclerViewResults = (RecyclerView) findViewById(R.id.recyclerViewResults);
        appCompatButtonAddResult = (AppCompatButton) findViewById(R.id.appCompatButtonAddResult);
        appCompatButtonShowScoreList = (AppCompatButton) findViewById(R.id.appCompatButtonShowScoreList);
        textViewUserNameResultList = (AppCompatTextView) findViewById(R.id.textViewUserNameResultList);
    }

    private void initListeners() {
        appCompatButtonAddResult.setOnClickListener(this);
        appCompatButtonShowScoreList.setOnClickListener(this);

     }

    private void initObjects() {
        listResults = new ArrayList<>();
        resultsRecyclerAdapter = new ResultsRecyclerAdapter(listResults);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewResults.setLayoutManager(mLayoutManager);
        recyclerViewResults.setItemAnimator(new DefaultItemAnimator());
        recyclerViewResults.setHasFixedSize(true);
        recyclerViewResults.setAdapter(resultsRecyclerAdapter);

        recyclerViewResults.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));

        scoreIdFromIntend = getIntent().getStringExtra("SCORE_ID");

        textViewUserNameResultList.setText(ScoreRepo.getScoreById(scoreIdFromIntend).getScore_name());

        //the broadcast receiver to update sync status
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getDataFromSQLite();
            }
        };

        networkCheckerReceiver =  new NetworkStateChecker();

        //registering the broadcast receiver to update sync status
        registerReceiver(broadcastReceiver, new IntentFilter(AppHelper.DATA_SAVED_BROADCAST));
        registerReceiver(networkCheckerReceiver, new IntentFilter((ConnectivityManager.CONNECTIVITY_ACTION)));

        getDataFromSQLite();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonAddResult:
                addResult();
                break;
            case R.id.appCompatButtonShowScoreList:
                showScoreList();
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

    private void addResult() {
        Intent addResultIntent = new Intent(getApplicationContext(), CreateResultActivity.class);
        addResultIntent.putExtra("SCORE_ID", scoreIdFromIntend);
        startActivity(addResultIntent);
    }

    private void showScoreList() {
        Intent showScoreListIntent = new Intent(getApplicationContext(), ScoreListActivity.class);
        showScoreListIntent.putExtra("USER_NAME", AppHelper.currentUser.getName());
        startActivity(showScoreListIntent);
    }

    private void getDataFromSQLite() {

        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listResults.clear();
                listResults.addAll(ResultRepo.getResultsOfScore(scoreIdFromIntend));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                resultsRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
