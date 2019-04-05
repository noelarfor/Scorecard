package com.roleon.scorecard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;

import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.roleon.scorecard.R;
import com.roleon.scorecard.helpers.AppHelper;
import com.roleon.scorecard.helpers.InputValidation;

import com.roleon.scorecard.helpers.URLs;
import com.roleon.scorecard.helpers.VolleySingleton;
import com.roleon.scorecard.model.Game;
import com.roleon.scorecard.sql.repo.GameRepo;
import com.roleon.scorecard.model.Result;
import com.roleon.scorecard.sql.repo.ResultRepo;
import com.roleon.scorecard.model.Score;
import com.roleon.scorecard.sql.repo.ScoreRepo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateScoreActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = CreateScoreActivity.this;
    public final String[] mode = {"endless play", "best of 3", "best of 5", "best of 7"};

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutScore;
    private TextInputEditText textInputEditTextScore;

    private AppCompatButton appCompatButtonCreateScore;
    private NumberPicker numberPickerTyp;
    private NumberPicker numberPickerMode;
    private NumberPicker numberPickerNumberUsers;

    private InputValidation inputValidation;
    private Score score;
    private ScoreRepo scoreRepo;
    private List<String> typList;
    private String[] typ;
    private int typIdx = 0;
    private int modeIdx = 0;
    private int numOfUsers = 2;
    private Result result;
    private ResultRepo resultRepo;
    private String created_at;
    private int score_id;
    boolean retVal;

    private GameRepo gameRepo;
    private List<Game> listGames;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_score);
        getSupportActionBar().hide();

        initObjects();
        initViews();
        initListeners();
    }

    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutScore = (TextInputLayout) findViewById(R.id.textInputLayoutScore);
        textInputEditTextScore = (TextInputEditText) findViewById(R.id.textInputEditTextScore);
        if (AppHelper.gameName != null) {
            textInputEditTextScore.setText(AppHelper.gameName, TextView.BufferType.EDITABLE);
        }
        appCompatButtonCreateScore = (AppCompatButton) findViewById(R.id.appCompatButtonCreateScore);

        numberPickerTyp = (NumberPicker) findViewById(R.id.numberPickerTyp);
        numberPickerTyp.setMinValue(0);
        numberPickerTyp.setMaxValue(typ.length - 1);
        numberPickerTyp.setWrapSelectorWheel(true);
        numberPickerTyp.setDisplayedValues(typ);

        numberPickerMode = (NumberPicker) findViewById(R.id.numberPickerMode);
        numberPickerMode.setMinValue(0);
        numberPickerMode.setMaxValue(mode.length - 1);
        numberPickerMode.setWrapSelectorWheel(true);
        numberPickerMode.setDisplayedValues(mode);

        numberPickerNumberUsers = (NumberPicker) findViewById(R.id.numberPickerNumberUsers);
        numberPickerNumberUsers.setMinValue(2);
        numberPickerNumberUsers.setMaxValue(2);
        numberPickerNumberUsers.setWrapSelectorWheel(false);
        numberPickerNumberUsers.setValue(numOfUsers);
    }

    private void initListeners() {
        appCompatButtonCreateScore.setOnClickListener(this);

        numberPickerTyp.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                typIdx = newVal;
            }
        });
        numberPickerMode.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                modeIdx = newVal;
            }
        });
        numberPickerNumberUsers.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                numOfUsers = newVal;
            }
        });

    }

    private void initObjects() {

        inputValidation = new InputValidation(activity);
        score = new Score();
        scoreRepo = new ScoreRepo();
        gameRepo = new GameRepo();

        listGames = new ArrayList<>();
        listGames.addAll(gameRepo.getAllGame());

        result = new Result();
        resultRepo = new ResultRepo();

        typList = new ArrayList<>();
        for (int i = 0; i < listGames.size(); i++) {
            typList.add(listGames.get(i).getGame_name());
        }
        typ = new String[typList.size()];
        typ = typList.toArray(typ);

        created_at = new String();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.appCompatButtonCreateScore:
                postDataToSQLite();
                finish();
                break;

        }
    }

    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextScore, textInputLayoutScore, getString(R.string.error_message_scorename))) {
            return;
        }
        AppHelper.gameName = textInputEditTextScore.getText().toString();

        if (AppHelper.listUsers.size() < numOfUsers) {
            Intent createScoreIntent = new Intent(activity, LoginActivity.class);
            createScoreIntent.putExtra("NUM_LOGIN", numOfUsers - 1);
            startActivity(createScoreIntent);
        }
        //check score in remote DB
        else {
            if (!checkScoreFromRemoteDB(textInputEditTextScore.getText().toString().trim())) {

                if (!scoreRepo.checkScore(textInputEditTextScore.getText().toString().trim())) {
                    score.setScore_name(textInputEditTextScore.getText().toString().trim());
                    score.setScore_typ(typIdx);
                    score.setScore_mode(modeIdx);
                    score.setNum_users(numOfUsers);
                    score.setGame_id(listGames.get(typIdx).getGame_id());
                    score.setLast_update(AppHelper.getDateTime());
                    score.setSyncStatus(AppHelper.NOT_SYNCED_WITH_SERVER);
                    scoreRepo.addScore(score);

                    score_id = scoreRepo.getScoreByName(textInputEditTextScore.getText().toString().trim()).getScore_Id();
                    created_at = AppHelper.getDateTime();

                    for (int i = 0; i < AppHelper.listUsers.size(); i++) {
                        result.setUser_name(AppHelper.listUsers.get(i).getName());
                        result.setScore_id(score_id);
                        result.setResult_win(0);
                        result.setResult_drawn(0);
                        result.setResult_loss(0);
                        result.setResult_diff(0);
                        result.setCreated_at(created_at);

                        resultRepo.addResult(result);
                    }

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ADD_SCORE,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        if (!obj.getBoolean("error")) {
                                            score = scoreRepo.getScoreByName(textInputEditTextScore.getText().toString().trim());
                                            scoreRepo.updateScore(score, AppHelper.SYNCED_WITH_SERVER);
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
                                    Toast.makeText(getApplicationContext(), "RemoteDB: " + error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("scorename", score.getScore_name());
                            params.put("scoretyp", Integer.toString(score.getScore_typ()));
                            params.put("scoremode", Integer.toString(score.getScore_mode()));
                            params.put("num_users", Integer.toString(score.getNum_users()));
                            params.put("timestamp", score.getLast_update());
                            params.put("gameid", Integer.toString(score.getGame_id()));
                            return params;
                        }
                    };

                    VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

                    Intent showResultIntent = new Intent(getApplicationContext(), ResultListActivity.class);
                    showResultIntent.putExtra("SCORE_ID", Integer.toString(score_id));
                    startActivity(showResultIntent);
                    AppHelper.listUsers.remove(AppHelper.listUsers.size() - 1);
                } else {
                    // Snack Bar to show error message that record already exists
                    Snackbar.make(nestedScrollView, getString(R.string.error_scorename_exists), Snackbar.LENGTH_LONG).show();
                }
            } else {
                // Snack Bar to show error message that record already exists
                Snackbar.make(nestedScrollView, getString(R.string.error_scorename_exists), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private boolean checkScoreFromRemoteDB(final String scorename){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_CHECK_SCORE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                Snackbar.make(nestedScrollView, "RemoteDB: " + obj.getString("message"), Snackbar.LENGTH_LONG).show();
                                retVal = true;
                            }
                            else {
                                Snackbar.make(nestedScrollView, "RemoteDB: " + obj.getString("message"), Snackbar.LENGTH_LONG).show();
                                retVal = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(nestedScrollView, "RemoteDB: " + error.getMessage(), Snackbar.LENGTH_LONG).show();
                        retVal = false;
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("scorename", scorename);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        return retVal;
    }
}
