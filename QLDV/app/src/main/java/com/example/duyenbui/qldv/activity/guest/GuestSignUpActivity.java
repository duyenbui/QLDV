package com.example.duyenbui.qldv.activity.guest;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.duyenbui.qldv.R;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GuestSignUpActivity extends AppCompatActivity {

    private static final String HOST_NAME = "http://192.168.0.48:8081";
    String url = null;

    EditText userName;
    EditText password1;
    EditText password2;
    EditText lastName;
    EditText firstName;
    EditText email;
    EditText birthDay;
    Button bt_SignUp;
    Button bt_Reset;
    ImageButton bt_Date;

    String jsonString = null;
    String username;
    String pass;
    String txtFirstName;
    String txtLastName;
    String txtEmail;
    String txtBirthday;
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_sign_up);

        userName = (EditText) findViewById(R.id.txt_username);
        password1 = (EditText) findViewById(R.id.txt_password1);
        password2 = (EditText) findViewById(R.id.txt_password2);
        firstName = (EditText) findViewById(R.id.txt_first_name);
        lastName = (EditText) findViewById(R.id.txt_last_name);
        email = (EditText) findViewById(R.id.txt_email);
        birthDay = (EditText) findViewById(R.id.txt_birthday);

         username = userName.getText().toString();
         pass = password1.getText().toString();
         txtFirstName = firstName.getText().toString();
         txtLastName = lastName.getText().toString();
         txtEmail = email.getText().toString();

        bt_SignUp = (Button) findViewById(R.id.bt_signUp);
        bt_Reset = (Button) findViewById(R.id.bt_clear);
        bt_Date = (ImageButton) findViewById(R.id.bt_date);

        password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bt_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(GuestSignUpActivity.this, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        bt_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidate()){
                    url = Uri.parse(HOST_NAME).buildUpon().appendPath("api").appendPath("user").appendPath("").build().toString();
                    Toast.makeText(GuestSignUpActivity.this, url, Toast.LENGTH_SHORT).show();
                    new AsyncTaskLoadAddUser().execute(url);

                    Toast.makeText(GuestSignUpActivity.this, jsonString, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), GuestLoginActivity.class);
                    startActivity(i);
                }
            }
        });

        bt_Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPage();
            }
        });

    }

    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            birthDay.setText(dayOfMonth + "/" + (month+1) + "/" + year);
        }
    };

    public void resetPage(){
        userName.setText("");
        password1.setText("");
        password2.setText("");
        firstName.setText("");
        lastName.setText("");
        email.setText("");
        birthDay.setText("");
    }

    public boolean checkValidate() {
        boolean valid = true;

        String spw2 = password2.getText().toString();
        if(!pass.equals(spw2)){
            password2.setError("Xác nhận lại mật khẩu");
        } else password1.setError(null);

        if (!pass.matches("[A-Za-z0-9]{6,21}")) {
            password1.setError("6-21 ký tự và không có ký tự đặc biệt");
            valid = false;
        } else password1.setError(null);

        if(!username.matches("[A-Za-z0-9]{3,}")){
            userName.setError("Ít nhất 3 ký tự và không chứa ký tự đặc biệt");
            valid = false;
        } else userName.setError(null);

        if(!txtFirstName.matches("^[0-9a-zA-Z\\sÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ]+$")){
            firstName.setError("Không rỗng và không chứa ký tự đặc biệt");
            valid = false;
        } else firstName.setError(null);

        if(!txtLastName.matches("^[0-9a-zA-Z\\sÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ]+$")){
            lastName.setError("Không rỗng và không chứa ký tự đặc biệt");
            valid = false;
        } else lastName.setError(null);

        if (!txtEmail.matches("[a-zA-Z0-9_\\.]+@[a-zA-Z]+\\.[a-zA-Z]+(\\.[a-zA-Z])*") && !txtEmail.isEmpty()) {
            email.setError("Nhập đúng định dạng email");
        }
        return valid;
    }

    private class AsyncTaskLoadAddUser extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            jsonString = s;
        }

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            Map<String, String> prs = new HashMap<>();
            prs.put("username", username);
            prs.put("password", pass);
            prs.put("firstName", txtFirstName);
            prs.put("lastName", txtLastName);
            prs.put("email", txtEmail);
            prs.put("birthday", txtBirthday);
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
