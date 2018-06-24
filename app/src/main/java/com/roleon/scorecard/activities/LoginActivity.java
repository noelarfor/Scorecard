package com.roleon.scorecard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import com.roleon.scorecard.R;
import com.roleon.scorecard.helpers.AppHelper;
import com.roleon.scorecard.helpers.InputValidation;
import com.roleon.scorecard.model.Score;
import com.roleon.scorecard.sql.repo.ScoreRepo;
import com.roleon.scorecard.sql.repo.UserRepo;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = LoginActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutUser;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextUser;
    private TextInputEditText textInputEditTextPassword;

    private AppCompatButton appCompatButtonLogin;
    private AppCompatButton appCompatButtonUserList;

    private AppCompatTextView textViewLinkRegister;

    private InputValidation inputValidation;
    private UserRepo userRepo;

    private ScoreRepo scoreRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {

        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutUser = (TextInputLayout) findViewById(R.id.textInputLayoutUser);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

        textInputEditTextUser = (TextInputEditText) findViewById(R.id.textInputEditTextUser);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);

        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);
        appCompatButtonUserList = (AppCompatButton) findViewById(R.id.appCompatButtonUserList);

        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);

    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        appCompatButtonUserList.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {

        inputValidation = new InputValidation(activity);
        userRepo = new UserRepo();
        scoreRepo = new ScoreRepo();
    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonLogin:
                verifyFromSQLite();
                break;
            case R.id.textViewLinkRegister:
                // Navigate to RegisterActivity
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
            case R.id.appCompatButtonUserList:
                showUserList();
                break;
        }
    }

    private void showUserList() {
        Intent accountsIntent = new Intent(activity, UsersListActivity.class);
        accountsIntent.putExtra("USER_NAME", "Admin");
        startActivity(accountsIntent);
    }

    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    private void verifyFromSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextUser, textInputLayoutUser, getString(R.string.error_message_username))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (userRepo.checkUser(textInputEditTextUser.getText().toString().trim()
                , textInputEditTextPassword.getText().toString().trim())) {

            Toast.makeText(LoginActivity.this, "User and Password match",
                    Toast.LENGTH_SHORT).show();

            Score score = new Score();
            score.setScore_name("TestScore1");
            score.setUser_Id((userRepo.getUser(textInputEditTextUser.getText().toString())).getId());
            score.setScore_typ(0);
            score.setScore_mode(0);
            score.setNum_users(2);
            score.setGame_id(1);
            score.setLast_update(AppHelper.getDateTime());

            scoreRepo.addScore(score);

            score.setScore_name("TestScore2");
            score.setUser_Id((userRepo.getUser(textInputEditTextUser.getText().toString())).getId());
            score.setScore_typ(0);
            score.setScore_mode(0);
            score.setNum_users(2);
            score.setGame_id(1);
            score.setLast_update(AppHelper.getDateTime());

            scoreRepo.addScore(score);

            score.setScore_name("TestScore3");
            score.setUser_Id((userRepo.getUser(textInputEditTextUser.getText().toString())).getId());
            score.setScore_typ(0);
            score.setScore_mode(0);
            score.setNum_users(2);
            score.setGame_id(1);
            score.setLast_update(AppHelper.getDateTime());

            scoreRepo.addScore(score);

            if (scoreRepo.checkScore("TestScore3")) {
                Toast.makeText(LoginActivity.this, "Name already used!",
                        Toast.LENGTH_SHORT).show();
            }

            Intent accountsIntent = new Intent(activity, ScoreListActivity.class);
            accountsIntent.putExtra("USER_NAME", textInputEditTextUser.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);


        } else {
            // Snack Bar to show success message that record is wrong
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_username_password), Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextUser.setText(null);
        textInputEditTextPassword.setText(null);
    }
}
