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
import android.widget.Toast;

import com.roleon.scorecard.R;
import com.roleon.scorecard.helpers.AppHelper;
import com.roleon.scorecard.helpers.InputValidation;

import com.roleon.scorecard.model.Game;
import com.roleon.scorecard.sql.repo.GameRepo;
import com.roleon.scorecard.model.Score;
import com.roleon.scorecard.sql.repo.ScoreRepo;

import java.util.ArrayList;
import java.util.List;

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
    private List<String> typList = new ArrayList<String>();
    private String[] typ;
    private int typIdx = 0;
    private int modeIdx = 0;
    private int numOfUsers = 1;

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

    /**
     * This method is to initialize views
     */
    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutScore = (TextInputLayout) findViewById(R.id.textInputLayoutScore);
        textInputEditTextScore = (TextInputEditText) findViewById(R.id.textInputEditTextScore);
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
        numberPickerNumberUsers.setMinValue(1);
        numberPickerNumberUsers.setMaxValue(6);
        numberPickerNumberUsers.setWrapSelectorWheel(false);
        numberPickerNumberUsers.setValue(numOfUsers);
    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonCreateScore.setOnClickListener(this);

        numberPickerTyp.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                typIdx = newVal;
                String text = "Changed from " + oldVal + " to " + newVal;
                Toast.makeText(CreateScoreActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
        numberPickerMode.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                modeIdx = newVal;
                String text = "Changed from " + oldVal + " to " + newVal;
                Toast.makeText(CreateScoreActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
        numberPickerNumberUsers.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                numOfUsers = newVal;
                String text = "Changed from " + oldVal + " to " + newVal;
                Toast.makeText(CreateScoreActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {

        inputValidation = new InputValidation(activity);
        score = new Score();
        scoreRepo = new ScoreRepo();

        listGames = new ArrayList<>();
        listGames.addAll(GameRepo.getAllGame());

        for (int i = 0; i < listGames.size(); i++) {
            typList.add(listGames.get(i).getGame_name());
        }
        typ = new String[typList.size()];
        typ = typList.toArray(typ);
    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.appCompatButtonCreateScore:
                postDataToSQLite();
                break;

        }
    }

    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextScore, textInputLayoutScore, getString(R.string.error_message_scorename))) {
            return;
        }

        if (!scoreRepo.checkScore(textInputEditTextScore.getText().toString().trim())) {

            score.setScore_name(textInputEditTextScore.getText().toString().trim());
            score.setScore_typ(typIdx);
            score.setScore_mode(modeIdx);
            score.setNum_users(numOfUsers);
            score.setGame_id(listGames.get(typIdx).getGame_id());
            score.setLast_update(AppHelper.getDateTime());

            scoreRepo.addScore(score);

            // Snack Bar to show success message that record saved successfully
            Snackbar.make(nestedScrollView, getString(R.string.success_createScore), Snackbar.LENGTH_LONG).show();
            emptyInputEditText();

            Intent createScoreIntent = new Intent(activity, LoginActivity.class);
            createScoreIntent.putExtra("NUM_LOGIN", numOfUsers - 1);
            startActivity(createScoreIntent);

        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(nestedScrollView, getString(R.string.error_scorename_exists), Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextScore.setText(null);
    }
}
