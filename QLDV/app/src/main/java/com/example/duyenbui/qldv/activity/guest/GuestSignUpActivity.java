package com.example.duyenbui.qldv.activity.guest;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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

public class GuestSignUpActivity extends AppCompatActivity {

    String url = null;

    EditText userName;
    EditText password1;
    EditText password2;
//    EditText address;
//    EditText phone;
//    EditText firstName;
    EditText email;
//    EditText birthDay;
    Button bt_SignUp;
//    ImageButton bt_Date;

    String jsonString = null;
    String username;
    String pass;
//    String txtFirstName;
//    String txtAddress;
//    String txtPhone;
    String txtEmail;
//    String txtBirthday;
//    Calendar calendar = Calendar.getInstance();
    String newUserName;

  //  String regexStringName = "^[0-9a-zA-Z\\sÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ]+$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_sign_up);

        userName = (EditText) findViewById(R.id.txt_username);
        password1 = (EditText) findViewById(R.id.txt_password1);
        password2 = (EditText) findViewById(R.id.txt_password2);
//        firstName = (EditText) findViewById(R.id.txt_first_name);
//        address = (EditText) findViewById(R.id.txt_address);
        email = (EditText) findViewById(R.id.txt_email);
//        birthDay = (EditText) findViewById(R.id.txt_birthday);
//        phone = (EditText) findViewById(R.id.txt_phoneNumber);

        bt_SignUp = (Button) findViewById(R.id.bt_signUp);
//        bt_Date = (ImageButton) findViewById(R.id.bt_date);
//
//        bt_Date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new DatePickerDialog(GuestSignUpActivity.this, listener,
//                        calendar.get(Calendar.YEAR),
//                        calendar.get(Calendar.MONTH),
//                        calendar.get(Calendar.DAY_OF_MONTH))
//                        .show();
//            }
//        });

        bt_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = userName.getText().toString();
                pass = password1.getText().toString();
//                txtFirstName = firstName.getText().toString();
//                txtAddress = address.getText().toString();
                txtEmail = email.getText().toString();
//                txtPhone = phone.getText().toString();
//                txtBirthday = birthDay.getText().toString();

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

    // listener tao DatePickerDialog dinh dang chuoi hien thi
//    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener(){
//        @Override
//        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//            birthDay.setText(year + "-" + (month+1) + "-" + dayOfMonth);
//        }
//    };


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

//        if(!txtFirstName.matches(getString(R.string.regex_string_name))){
//            firstName.setError(getString(R.string.valid_string_name));
//            valid = false;
//        } else firstName.setError(null);
//
//        if(!txtAddress.matches(getString(R.string.regex_string_name))){
//            address.setError(getString(R.string.valid_string_name));
//            valid = false;
//        } else address.setError(null);
//
//        if(!txtPhone.matches(getString(R.string.regex_phoneNumber))){
//            phone.setError(getString(R.string.valid_string_phone));
//            valid = false;
//        } else phone.setError(null);

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
//            prs.put("fullName", txtFirstName);
//            prs.put("address", txtAddress);
//            prs.put("phonenumber", txtPhone);
            prs.put("email", txtEmail);
//            prs.put("birthday", txtBirthday);
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
