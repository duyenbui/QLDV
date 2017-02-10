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
import android.widget.TextView;
import android.widget.Toast;

import com.example.duyenbui.qldv.R;
import com.example.duyenbui.qldv.activity.MapActivity;
import com.example.duyenbui.qldv.object.user;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GuestLoginActivity extends AppCompatActivity {

    private static final String HOST_NAME = "192.168.0.48:8081";

    EditText txt_username;
    EditText txt_password;
    Button bt_login;
    TextView bt_createAccount;

    public String jsonString;
    public Realm realm;
    private RealmResults<user> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_login);

        txt_username = (EditText) findViewById(R.id.txt_name);
        txt_password = (EditText) findViewById(R.id.txt_password);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_createAccount = (TextView) findViewById(R.id.bt_createAccount);

        realm = Realm.getDefaultInstance();
        if(!checkExistRealmObject()){
            Toast.makeText(getApplicationContext(), "Waiting... ", Toast.LENGTH_SHORT).show();
            startAsyncTaskGetAPI();
        }

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
            }
        });

        bt_createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GuestSignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean checkExistRealmObject(){
        items = realm.where(user.class). findAll();
        if(items != null){
            return true;
        }
        else return false;
    }

    public void startAsyncTaskGetAPI() {
        String url = Uri.parse(HOST_NAME).buildUpon().appendPath("user").build().toString();
        new AsyncTaskLoad().execute(url);
    }

    public boolean checkExisUser(){
        String username = txt_username.getText().toString();
        String password = txt_password.getText().toString();



        return false;
    }

    public void checkValidate(){
        final String username = txt_username.getText().toString();
        final String pass = txt_password.getText().toString();

        // kiem tra truong email va pass co rong hay khong
        if (username.isEmpty() || pass.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(GuestLoginActivity.this);
            builder.setMessage("Vui lòng nhập đủ Username và password")
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();

            dialog.show();

            //Kiem tra password hon 6 ky tu

        } else if (!pass.matches("[A-Za-z0-9._%+-]{6,}")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(GuestLoginActivity.this);
            builder.setMessage("Password phải trên 6 ký tự")
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }

    public class AsyncTaskLoad extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).get().build();
            try {
                Response response = okHttpClient.newCall(request).execute();

                if (response.isSuccessful()) {
                    return response.body().string();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Error can not get API";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            jsonString = s;
//            Realm realm = Realm.getDefaultInstance();
//            try {
//                JSONObject jsonObject = new JSONObject(jsonString);
//                realm.beginTransaction();
//                realm.createOrUpdateAllFromJson(user.class, jsonObject.optString("users"));
//                realm.commitTransaction();
//                items = realm.where(user.class).findAll();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }
    }
}
