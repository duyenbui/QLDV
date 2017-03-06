package com.example.duyenbui.qldv.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duyenbui.qldv.R;
import com.example.duyenbui.qldv.activity.guest.ForgotPasswordActivity;
import com.example.duyenbui.qldv.object.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.duyenbui.qldv.activity.guest.GuestLoginActivity.oauth;

public class UpdatePasswordActivity extends AppCompatActivity {

    EditText old_password;
    EditText new_password;
    EditText new_password_confirm;
    Button bt_updatePassword;

    String oldPass, newPass, passConfirm;

    String jsonString;
    String url = null;
    String newPassword;

    String id;
    String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        accessToken = oauth.getAccess_token();

        Intent i = getIntent();
        id = i.getStringExtra("id");

        old_password = (EditText) findViewById(R.id.profile_old_pass);
        new_password = (EditText) findViewById(R.id.profile_new_pass);
        new_password_confirm = (EditText) findViewById(R.id.profile_new_pass_confirm);
        bt_updatePassword = (Button) findViewById(R.id.bt_updatePassword);

        bt_updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                oldPass = old_password.getText().toString();
                newPass = new_password.getText().toString();
                passConfirm = new_password_confirm.getText().toString();
                if(checkValidate()){
                    url = Uri.parse(getString(R.string.host_name)).buildUpon()
                            .appendPath("api")
                            .appendPath("changepassword")
                            .appendPath(id)
                            .appendQueryParameter("access_token", accessToken)
                            .build().toString();
                    Toast.makeText(UpdatePasswordActivity.this, url, Toast.LENGTH_SHORT).show();
                    new AsyncTaskLoadUpdatePassword().execute();
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
        return true;
    }

    public boolean checkValidate() {
        boolean valid = true;



        if(!newPass.equals(passConfirm)){
            new_password_confirm.setError(getString(R.string.confirm_password));
            valid = false;
        } else new_password.setError(null);

        if (!oldPass.matches(getString(R.string.regex_password))) {
            old_password.setError(getString(R.string.valid_password));
            valid = false;
        } else old_password.setError(null);

        if (!newPass.matches(getString(R.string.regex_password))) {
            new_password.setError(getString(R.string.valid_password));
            valid = false;
        } else new_password.setError(null);

        if (!passConfirm.matches(getString(R.string.regex_password))) {
            new_password_confirm.setError(getString(R.string.valid_password));
            valid = false;
        } else new_password_confirm.setError(null);

        return valid;
    }

    private class AsyncTaskLoadUpdatePassword extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            jsonString = s;
            Toast.makeText(UpdatePasswordActivity.this, jsonString, Toast.LENGTH_SHORT).show();

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonString);

                if(jsonObject.length() == 1){
                    String message = jsonObject.getString("message");
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdatePasswordActivity.this);
                    builder.setMessage(message)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();

                    dialog.show();
                } else if(jsonObject.length() == 2){
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdatePasswordActivity.this);
                    builder.setMessage(getString(R.string.failed_access_token))
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();

                    dialog.show();
                } else {
//                    newPassword = jsonObject.getString("newPassword");
//                    if (newPass.equals(newPassword)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdatePasswordActivity.this);
                        builder.setMessage(getString(R.string.update_successful))
                                .setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();

                        dialog.show();
//                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... params) {

            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            Map<String, String> prs = new HashMap<>();
            prs.put("oldPassword", oldPass);
            prs.put("newPassword", newPass);
            JSONObject parameter = new JSONObject(prs);

            RequestBody postData = RequestBody.create(JSON, parameter.toString());
            Request request = new Request.Builder().url(url).put(postData)
                    .addHeader("content-type", "application/json; charset=utf-8")
                    .build();
            try {
                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    return response.body().string();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return getString(R.string.error_getAPI);

        }
    }
}
