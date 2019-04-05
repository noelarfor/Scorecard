package com.roleon.scorecard.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.roleon.scorecard.model.Score;
import com.roleon.scorecard.model.User;
import com.roleon.scorecard.sql.repo.ScoreRepo;
import com.roleon.scorecard.sql.repo.UserRepo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkStateChecker extends BroadcastReceiver {

    private List<User> listUsers;
    private List<Score> listScores;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                AppHelper.showProgressDialogTimed(context, "SyncDB", "Synchronisation with remote DB...", 2000);

                listUsers = UserRepo.getUnsyncedUsers();
                for (int i = 0; i < listUsers.size(); i++) {
                       syncUsers(listUsers.get(i));
                }

                listScores = ScoreRepo.getUnsyncedScores();
                for (int i = 0; i < listScores.size(); i++) {
                    syncScores(listScores.get(i));
                }
            }
        }
    }

    /*
     * if user is successfully sent
     * we will update the status as synced in SQLite
     */
    private void syncUsers(final User user) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SIGNUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(context, "RemoteDB: " + obj.getString("message"), Toast.LENGTH_LONG).show();
                                //updating the status in sqlite
                                UserRepo.updateUserStatus(user, AppHelper.SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(AppHelper.DATA_SAVED_BROADCAST));
                            }
                            else {
                                Toast.makeText(context, "RemoteDB: " + obj.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "RemoteDB: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", user.getName());
                params.put("password", user.getPassword());
                params.put("timestamp", user.getCreated_at());
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    /*
     * if score is successfully sent
     * we will update the status as synced in SQLite
     */
    private void syncScores(final Score score) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ADD_SCORE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(context, "RemoteDB: " + obj.getString("message"), Toast.LENGTH_LONG).show();
                                //updating the status in sqlite
                                ScoreRepo.updateScore(score, AppHelper.SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(AppHelper.DATA_SAVED_BROADCAST));
                            }
                            else {
                                Toast.makeText(context, "RemoteDB: " + obj.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "RemoteDB: " + error.getMessage(), Toast.LENGTH_LONG).show();
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

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}
