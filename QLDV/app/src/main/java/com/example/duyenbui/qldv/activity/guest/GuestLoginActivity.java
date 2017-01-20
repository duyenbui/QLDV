package com.example.duyenbui.qldv.activity.guest;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.duyenbui.qldv.R;
import com.example.duyenbui.qldv.activity.MapActivity;

import io.realm.Realm;
import okhttp3.OkHttpClient;

public class GuestLoginActivity extends AppCompatActivity {

    private static final String HOST_NAME = "192.168.0.48:8081";

    EditText txt_username;
    EditText txt_password;
    Button bt_login;
    TextView bt_createAccount;

    public Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_login);

        txt_username = (EditText) findViewById(R.id.txt_name);
        txt_password = (EditText) findViewById(R.id.txt_password);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_createAccount = (TextView) findViewById(R.id.bt_createAccount);

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

    public void startAsyncTaskGetAPI(){
        String url = Uri.parse(HOST_NAME).buildUpon().appendPath("user").build().toString();

    }

    public class AsyncTaskLoad extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings){
            String url = strings[0];
            OkHttpClient okHttpClient = new OkHttpClient();
            return "Error can not get API";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
