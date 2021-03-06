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
import android.widget.NumberPicker;
import android.view.View;

import com.roleon.scorecard.R;
import com.roleon.scorecard.helpers.AppHelper;
import com.roleon.scorecard.helpers.InputValidation;
import com.roleon.scorecard.model.Game;
import com.roleon.scorecard.sql.repo.GameRepo;

public class CreateGameActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = CreateGameActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutGame;
    private TextInputEditText textInputEditTextGame;

    private AppCompatButton appCompatButtonGameList;
    private NumberPicker numberPickerWinPoints;
    private NumberPicker numberPickerLossPoints;
    private NumberPicker numberPickerDrawnPoints;

    private InputValidation inputValidation;
    private Game game;
    private GameRepo gameRepo;
    private int winPoints = 3; // initialize default values, will be overwritten onValueChange
    private int lossPoints = 0;
    private int drawnPoints = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        getSupportActionBar().hide();

        initObjects();
        initViews();
        initListeners();
    }

    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutGame = (TextInputLayout) findViewById(R.id.textInputLayoutGame);
        textInputEditTextGame = (TextInputEditText) findViewById(R.id.textInputEditTextGame);
        appCompatButtonGameList = (AppCompatButton) findViewById(R.id.appCompatButtonGameList);

        numberPickerWinPoints = (NumberPicker) findViewById(R.id.numberPickerWinPoints);
        numberPickerWinPoints.setMinValue(1);
        numberPickerWinPoints.setMaxValue(100);
        numberPickerWinPoints.setWrapSelectorWheel(true);
        numberPickerWinPoints.setValue(winPoints);

        numberPickerLossPoints = (NumberPicker) findViewById(R.id.numberPickerLossPoints);
        numberPickerLossPoints.setMinValue(0);
        numberPickerLossPoints.setMaxValue(100);
        numberPickerLossPoints.setWrapSelectorWheel(true);
        numberPickerLossPoints.setValue(lossPoints);

        numberPickerDrawnPoints = (NumberPicker) findViewById(R.id.numberPickerDrawnPoints);
        numberPickerDrawnPoints.setMinValue(1);
        numberPickerDrawnPoints.setMaxValue(100);
        numberPickerDrawnPoints.setWrapSelectorWheel(true);
        numberPickerDrawnPoints.setValue(drawnPoints);
    }

    private void initListeners() {
        appCompatButtonGameList.setOnClickListener(this);

        numberPickerWinPoints.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                winPoints = newVal;
            }
        });
        numberPickerLossPoints.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                lossPoints = newVal;
            }
        });
        numberPickerDrawnPoints.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                drawnPoints = newVal;
            }
        });

    }

    private void initObjects() {

        inputValidation = new InputValidation(activity);
        game = new Game();
        gameRepo = new GameRepo();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.appCompatButtonGameList:
                postDataToSQLite();
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

    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextGame, textInputLayoutGame, getString(R.string.error_message_gamename))) {
            return;
        }

        if (!gameRepo.checkGame(textInputEditTextGame.getText().toString().trim())) {

            game.setGame_name(textInputEditTextGame.getText().toString().trim());
            game.setWin_points(winPoints);
            game.setLoss_points(lossPoints);
            game.setDrawn_points(drawnPoints);

            gameRepo.addGame(game);

            // Snack Bar to show success message that record saved successfully
            Snackbar.make(nestedScrollView, getString(R.string.success_addGame), Snackbar.LENGTH_LONG).show();
            emptyInputEditText();
            Intent showGameListIntent = new Intent(getApplicationContext(), GameListActivity.class);
            startActivity(showGameListIntent);
            finish();

        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(nestedScrollView, getString(R.string.error_gamename_exists), Snackbar.LENGTH_LONG).show();
            return;
        }
    }

    private void emptyInputEditText() {
        textInputEditTextGame.setText(null);
    }
}