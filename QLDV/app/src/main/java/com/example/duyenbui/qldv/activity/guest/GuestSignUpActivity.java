package com.example.duyenbui.qldv.activity.guest;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class GuestSignUpActivity extends AppCompatActivity {

    String url = null;

    EditText userName;
    EditText password1;
    EditText password2;
    EditText email;
    Button bt_SignUp;

    String jsonString = null;
    String username;
    String pass;
    String txtEmail;
    String newUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_sign_up);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        userName = (EditText) findViewById(R.id.txt_username);
        password1 = (EditText) findViewById(R.id.txt_password1);
        password2 = (EditText) findViewById(R.id.txt_password2);
        email = (EditText) findViewById(R.id.txt_email);

        bt_SignUp = (Button) findViewById(R.id.bt_signUp);

        bt_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = userName.getText().toString();
                pass = password1.getText().toString();
                txtEmail = email.getText().toString();

                if(checkValidate()){
                    url = Uri.parse(getString(R.string.host_name)).buildUpon()
                            .appendPath("api")
                            .appendPath("accounts")
                            .build().toString(); //url API server tra ve
                    Toast.makeText(GuestSignUpActivity.this, url, Toast.LENGTH_SHORT).show();
                    new AsyncTaskLoadAddUser().execute(url);

                    Toast.makeText(GuestSignUpActivity.this, newUserName, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), GuestLoginActivity.class);
                    startActivity(i);
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
                Intent intent = new Intent(this, GuestLoginActivity.class);
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

        String spw2 = password2.getText().toString();

        if(!pass.equals(spw2)){
            password2.setError(getString(R.string.confirm_password));
            valid = false;
        } else password1.setError(null);

        if (!pass.matches(getString(R.string.regex_password))) {
            password1.setError(getString(R.string.valid_password));
            valid = false;
        } else password1.setError(null);

        if(!username.matches(getString(R.string.regex_username))){
            userName.setError(getString(R.string.valid_username));
            valid = false;
        } else userName.setError(null);

        if (!txtEmail.matches(getString(R.string.regex_email))) {
            email.setError(getString(R.string.valid_format_email));
            valid = false;
        }

        return valid;
    }

    // gui du lieu len server bang phuong thuc POST cua OkHttp
    private class AsyncTaskLoadAddUser extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            jsonString = s;

            JSONObject reader = null;
            try {
                reader = new JSONObject(jsonString);
                JSONObject account = reader.getJSONObject("account");
                newUserName = account.getString("username");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");    //dinh nghia du lieu gui den server laf mot file JSON
            Map<String, String> prs = new HashMap<>();                              //HashMap luu tru key, value cua params truyen di duoi dang JSON
            prs.put("username", username);
            prs.put("password", pass);
            prs.put("email", txtEmail);
            JSONObject parameter = new JSONObject(prs);                             //tham khao cach POST theo OkHttp tai http://square.github.io/okhttp/

            RequestBody postData = RequestBody.create(JSON, parameter.toString());
            Request request = new Request.Builder().url(url).post(postData).addHeader("content-type", "application/json; charset=utf-8").build();
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
