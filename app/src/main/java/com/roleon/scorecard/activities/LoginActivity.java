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
import com.roleon.scorecard.model.User;
import com.roleon.scorecard.sql.repo.UserRepo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutUser;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextUser;
    private TextInputEditText textInputEditTextPassword;

    private AppCompatButton appCompatButtonLogin;

    private AppCompatTextView textViewLinkRegister;

    private InputValidation inputValidation;
    private UserRepo userRepo;
    private User user;
    private boolean isFromIntent = false;

    private int numOfLogin = 1;
    private static final String TAG = "HTTP Response";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }

    private void initViews() {

        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutUser = (TextInputLayout) findViewById(R.id.textInputLayoutUser);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

        textInputEditTextUser = (TextInputEditText) findViewById(R.id.textInputEditTextUser);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);

        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);

        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);

    }

    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }

    private void initObjects() {

        inputValidation = new InputValidation(this);
        userRepo = new UserRepo();
        user = new User();

        Intent intent = getIntent();
        if (intent.hasExtra("NUM_LOGIN")) {
            numOfLogin = intent.getExtras().getInt("NUM_LOGIN");
            isFromIntent = true;
            Snackbar.make(nestedScrollView, numOfLogin + " " + getString(R.string.text_remaining_login), Snackbar.LENGTH_LONG).show();
        }
}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonLogin:
                AppHelper.hideKeyboard(this);
                verifyFromSQLite();
                break;
            case R.id.textViewLinkRegister:
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
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

    private void verifyFromSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextUser, textInputLayoutUser, getString(R.string.error_message_username))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (userRepo.checkUser(textInputEditTextUser.getText().toString().trim()
                , AppHelper.MD5(textInputEditTextPassword.getText().toString().trim()))) {

            if (AppHelper.isInit) {
                AppHelper.currentUser = userRepo.getUser(textInputEditTextUser.getText().toString().trim());
                AppHelper.isInit = false;
            }

            for (int i = 0; i < AppHelper.listUsers.size(); i++) {
                if (AppHelper.listUsers.get(i).getId() == userRepo.getUser(textInputEditTextUser.getText().toString().trim()).getId()) {
                    emptyInputEditText();
                    Snackbar.make(nestedScrollView, getString(R.string.text_user_already_logged_in), Snackbar.LENGTH_LONG).show();
                    return;
                }
            }

            AppHelper.listUsers.add(userRepo.getUser(textInputEditTextUser.getText().toString().trim()));
            numOfLogin--;
            if ((numOfLogin < 1) && isFromIntent) {
                emptyInputEditText();
                Intent createScoreActivityIntent = new Intent(getApplicationContext(), CreateScoreActivity.class);
                startActivity(createScoreActivityIntent);
                finish();
            }
            else if (numOfLogin < 1){
                Intent showScoreListActivityIntent = new Intent(getApplicationContext(), ScoreListActivity.class);
                showScoreListActivityIntent.putExtra("USER_NAME", textInputEditTextUser.getText().toString().trim());
                startActivity(showScoreListActivityIntent);
                emptyInputEditText();
                finish();
            }
            else {
                emptyInputEditText();
                Snackbar.make(nestedScrollView, getString(R.string.success_message_Add_User) + numOfLogin, Snackbar.LENGTH_LONG).show();
            }

        } else {
            // check if user is stored in remote db
            getUserFromRemoteDB(textInputEditTextUser.getText().toString().trim()
                    , AppHelper.MD5(textInputEditTextPassword.getText().toString().trim()));

            // Snack Bar to show success message that record is wrong
            //Snackbar.make(nestedScrollView, getString(R.string.error_valid_username_password), Snackbar.LENGTH_LONG).show();
        }
    }

    private void emptyInputEditText() {
        textInputEditTextUser.setText(null);
        textInputEditTextPassword.setText(null);
    }

    private void getUserFromRemoteDB(final String username, final String password){
        AppHelper.showProgressDialogTimed(this, "SyncDB", "Searching for user...", 2000);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GETUSER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                Snackbar.make(nestedScrollView, "RemoteDB: " + obj.getString("message"), Snackbar.LENGTH_LONG).show();
                                user.setName(obj.getString("username"));
                                user.setPassword(obj.getString("password"));
                                user.setCreated_at(obj.getString("timestamp"));
                                user.setSyncStatus(AppHelper.SYNCED_WITH_SERVER);
                                userRepo.addUser(user);
                            }
                            else {
                                Snackbar.make(nestedScrollView, "RemoteDB: " + obj.getString("message"), Snackbar.LENGTH_LONG).show();
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
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
