package com.example.duyenbui.qldv.activity.guest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duyenbui.qldv.R;
import com.example.duyenbui.qldv.activity.MapActivity;
import com.example.duyenbui.qldv.activity.expert.ExpertMainActivity;
import com.example.duyenbui.qldv.activity.member.MemberMainActivity;
import com.example.duyenbui.qldv.object.ConnectDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GuestLoginActivity extends AppCompatActivity {

    private static final String HOST_NAME = "http://192.168.0.48:8081";
    String url = null;
    Boolean connection = false;

    EditText txt_username;
    EditText txt_password;
    Button bt_login;
    TextView bt_createAccount;
    String username, pass;
    String jsonString = null;
    String role;
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_login);

        layout = (RelativeLayout) findViewById(R.id.activity_guest_login);
        txt_username = (EditText) findViewById(R.id.txt_name);
        txt_password = (EditText) findViewById(R.id.txt_password);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_createAccount = (TextView) findViewById(R.id.bt_createAccount);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = txt_username.getText().toString();
                pass = txt_password.getText().toString();

                if(checkInternet()){
                    if(checkValidate(username, pass)){
                        url = Uri.parse(HOST_NAME).buildUpon().appendPath("api").appendPath("session").appendPath("").build().toString();
                        //Toast.makeText(GuestLoginActivity.this, url, Toast.LENGTH_SHORT).show();
                        new AsyncTaskLoad().execute(url);
                    }
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(GuestLoginActivity.this);
                    builder.setMessage("Vui lòng kiểm tra kết nối internet")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();

                    dialog.show();
                }
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

    @Override
    protected void onResume() {
        super.onResume();

    }

    public boolean checkValidate(String username, String pass) {
        // kiem tra truong username va pass co rong hay khong
        if (username.isEmpty() || pass.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(GuestLoginActivity.this);
            builder.setMessage("Vui lòng nhập đủ Username và password")
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();

            dialog.show();
            return false;

            //Kiem tra password it nhat 6 ky tu

        } else if (!pass.matches("[A-Za-z0-9]{6,21}")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(GuestLoginActivity.this);
            builder.setMessage("Password 6-21 ký tự và không có ký tự đặc biệt")
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
            return false;

        }
        else return true;
    }

    public boolean checkInternet() {
        ConnectDetector cd = new ConnectDetector(getApplicationContext());
        connection = cd.isConnectingToInternet();
        if (!connection) {
            return false;
        }
        return true;
    }


    private void switchRole() {
        if(jsonString != ""){

            try {
                JSONObject jsonObj = new JSONObject(jsonString);

                    role = jsonObj.getString("roleName");
                    role = role.trim().toLowerCase();
                    switch (role){
                        case "member":{
                            Intent intent = new Intent(getApplicationContext(), MemberMainActivity.class);
                            startActivity(intent);
                            break;
                        }
                        case "expert":{
                            Intent intent = new Intent(getApplicationContext(), ExpertMainActivity.class);
                            startActivity(intent);
                            break;
                        }
                        case "manager":{
                            Toast.makeText(this, "Xin lỗi, chưa có chức năng cho manager", Toast.LENGTH_SHORT).show();
                            txt_username.setText("");
                            txt_password.setText("");
                            break;
                        }
                        case "admin":{
                            Toast.makeText(this, "Xin lỗi, chưa có chức năng cho admin", Toast.LENGTH_SHORT).show();
                            txt_username.setText("");
                            txt_password.setText("");
                            break;
                        }
                        default:{

                        }
                    }
            } catch (final JSONException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }

        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(GuestLoginActivity.this);
            builder.setMessage("Tài khoản hoặc mật khẩu không đúng!")
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private class AsyncTaskLoad extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            jsonString = s;
            switchRole();
        }

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            Map<String, String> prs = new HashMap<>();
            prs.put("username", username);
            prs.put("password", pass);
            JSONObject parameter = new JSONObject(prs);

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
            return "Error cannot get API!";

        }
    }

}
