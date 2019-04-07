package com.roleon.scorecard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.roleon.scorecard.R;
import com.roleon.scorecard.helpers.AppHelper;
import com.roleon.scorecard.helpers.URLs;
import com.roleon.scorecard.helpers.VolleySingleton;
import com.roleon.scorecard.model.Score;
import com.roleon.scorecard.sql.repo.ScoreRepo;
import com.roleon.scorecard.model.Result;
import com.roleon.scorecard.sql.repo.ResultRepo;
import com.roleon.scorecard.model.Game;
import com.roleon.scorecard.sql.repo.GameRepo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateResultActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = CreateResultActivity.this;

    private AppCompatButton appCompatButtonAddResult;
    private AppCompatTextView textViewUserName1;
    private AppCompatTextView textViewUserName2;

    private NumberPicker numberPickerResult1;
    private NumberPicker numberPickerResult2;
    private List<Result> userResultList;
    private ResultRepo resultRepo;
    private Score score;
    private ScoreRepo scoreRepo;
    private Game game;
    private GameRepo gameRepo;
    private String scoreIdFromIntend;

    private int resultUser1;
    private int resultUser2;
    private int numUsers;
    private int resultIndex;

    private int[] numberOfWin;
    private int[] numberOfDrawn;
    private int[] numberOfLoss;
    private int[] diffPoints;
    private int[] resultPoints;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_result);
        getSupportActionBar().hide();

        initViews();
        initObjects();
        initListeners();
    }

    private void initViews() {
        textViewUserName1 = (AppCompatTextView) findViewById(R.id.textViewUserName1);
        textViewUserName2 = (AppCompatTextView) findViewById(R.id.textViewUserName2);

        appCompatButtonAddResult = (AppCompatButton) findViewById(R.id.appCompatButtonAddResult);

        numberPickerResult1 = (NumberPicker) findViewById(R.id.numberPickerResult1);
        numberPickerResult1.setMinValue(0);
        numberPickerResult1.setMaxValue(10);
        numberPickerResult1.setWrapSelectorWheel(false);
        numberPickerResult1.setValue(0);

        numberPickerResult2 = (NumberPicker) findViewById(R.id.numberPickerResult2);
        numberPickerResult2.setMinValue(0);
        numberPickerResult2.setMaxValue(10);
        numberPickerResult2.setWrapSelectorWheel(false);
        numberPickerResult2.setValue(0);
    }

    private void initListeners() {
        appCompatButtonAddResult.setOnClickListener(this);

        numberPickerResult1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                resultUser1 = newVal;
            }
        });
        numberPickerResult2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                resultUser2 = newVal;
            }
        });
    }

    private void initObjects() {

        scoreIdFromIntend = getIntent().getStringExtra("SCORE_ID");
        score = new Score();
        scoreRepo = new ScoreRepo();
        score = scoreRepo.getScoreById(scoreIdFromIntend);

        game = new Game();
        gameRepo = new GameRepo();
        game = gameRepo.getGame(Integer.toString(score.getGame_id()));

        resultRepo = new ResultRepo();
        userResultList = new ArrayList<>();
        userResultList.addAll(resultRepo.getResultsOfScore(String.valueOf(scoreIdFromIntend)));
        numUsers = userResultList.size();

        if (score.getNum_users() != numUsers) {
            Toast.makeText(this, "Number Of Users Dont Match", Toast.LENGTH_LONG).show();
            return;
        }

        textViewUserName1.setText(userResultList.get(0).getUser_name());
        textViewUserName2.setText(userResultList.get(1).getUser_name());

        numberOfWin = new int[numUsers];
        numberOfDrawn = new int[numUsers];
        numberOfLoss = new int[numUsers];
        diffPoints = new int[numUsers];
        resultPoints = new int[numUsers];

        for (int i = 0; i < numUsers; i++) {
            numberOfWin[i] = userResultList.get(i).getResult_win();
            numberOfDrawn[i] = userResultList.get(i).getResult_drawn();
            numberOfLoss[i] = userResultList.get(i).getResult_loss();
            diffPoints[i] = userResultList.get(i).getResult_diff();
            resultPoints[i] = userResultList.get(i).getResult_points();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.appCompatButtonAddResult:
                postDataToSQLite();
                break;

        }
    }

    private void postDataToSQLite() {

        if (resultUser1 > resultUser2) {
            numberOfWin[0]++;
            diffPoints[0]+= resultUser1 - resultUser2;
            resultPoints[0]+= game.getWin_points();

            numberOfLoss[1]++;
            diffPoints[1]-= resultUser1 - resultUser2;
            resultPoints[0]+= game.getLoss_points();
        }
        else if (resultUser1 < resultUser2) {
            numberOfWin[1]++;
            diffPoints[1]+= resultUser2 - resultUser1;
            resultPoints[1]+= game.getWin_points();

            numberOfLoss[0]++;
            diffPoints[0]-= resultUser2 - resultUser1;
            resultPoints[1]+= game.getLoss_points();
        }
        else {
            for (int i = 0; i < numUsers; i++) {
                numberOfDrawn[i]++;
                resultPoints[i]+= game.getDrawn_points();
            }
        }

        for (resultIndex = 0; resultIndex < numUsers; resultIndex++) {
            userResultList.get(resultIndex).setResult_win(numberOfWin[resultIndex]);
            userResultList.get(resultIndex).setResult_drawn(numberOfDrawn[resultIndex]);
            userResultList.get(resultIndex).setResult_loss(numberOfLoss[resultIndex]);
            userResultList.get(resultIndex).setResult_diff(diffPoints[resultIndex]);
            userResultList.get(resultIndex).setResult_points(resultPoints[resultIndex]);

            updateResult(userResultList.get(resultIndex));
        }

        Intent showResultListIntent = new Intent(activity, ResultListActivity.class);
        showResultListIntent.putExtra("SCORE_ID", scoreIdFromIntend);
        startActivity(showResultListIntent);
        finish();
    }

    private void updateResult(final Result result) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ADD_RESULT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                resultRepo.updateResult(result, AppHelper.SYNCED_WITH_SERVER);
                            } else {
                                resultRepo.updateResult(result, AppHelper.NOT_SYNCED_WITH_SERVER);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // on error storing the name to sqlite with status unsynced
                        resultRepo.updateResult(result, AppHelper.NOT_SYNCED_WITH_SERVER);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("win", Integer.toString(userResultList.get(resultIndex).getResult_win()));
                params.put("drawn", Integer.toString(userResultList.get(resultIndex).getResult_drawn()));
                params.put("loss", Integer.toString(userResultList.get(resultIndex).getResult_loss()));
                params.put("diff", Integer.toString(userResultList.get(resultIndex).getResult_diff()));
                params.put("points", Integer.toString(userResultList.get(resultIndex).getResult_points()));
                params.put("timestamp", userResultList.get(resultIndex).getCreated_at());
                params.put("localresultid", Integer.toString(userResultList.get(resultIndex).getResult_id()));
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}