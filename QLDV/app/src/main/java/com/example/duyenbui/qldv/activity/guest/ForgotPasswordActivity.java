package com.example.duyenbui.qldv.activity.guest;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duyenbui.qldv.R;

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

public class ForgotPasswordActivity extends AppCompatActivity {

    String url = null;

    EditText email;
    String txtEmail;
    String jsonString = null;
    String message;

    Button bt_resetPassword;
    Button bt_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = (EditText) findViewById(R.id.emailResetPassword);
        bt_resetPassword = (Button) findViewById(R.id.bt_resetPassword);
        bt_back = (Button) findViewById(R.id.bt_back);

        bt_resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtEmail = email.getText().toString();
                if(checkValidate()){

                    url = Uri.parse(getString(R.string.host_name)).buildUpon()
                            .appendPath("api")
                            .appendPath("resetpassword")
                            .appendPath(txtEmail)
                            .build().toString(); //url API server tra ve
                    Toast.makeText(ForgotPasswordActivity.this, url, Toast.LENGTH_SHORT).show();
                    new AsyncTaskLoadResetPassword().execute(url);
                }
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GuestLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean checkValidate() {
        boolean valid = true;

        if (!txtEmail.matches(getString(R.string.regex_email))) {
            email.setError(getString(R.string.valid_format_email));
            valid = false;
        }

        return valid;
    }

    private class AsyncTaskLoadResetPassword extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            jsonString = s;

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonString);
                message = jsonObject.getString("message");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
            builder.setMessage(message)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();

            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url(url).get().build();
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
