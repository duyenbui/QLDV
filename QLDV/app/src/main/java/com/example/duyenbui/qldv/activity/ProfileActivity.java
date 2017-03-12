package com.example.duyenbui.qldv.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duyenbui.qldv.R;
import com.example.duyenbui.qldv.activity.member.MemberMainActivity;
import com.example.duyenbui.qldv.object.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
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

import static com.example.duyenbui.qldv.activity.guest.GuestLoginActivity.oauth;

public class ProfileActivity extends AppCompatActivity {

    EditText pf_username;
    EditText pf_email;
    EditText pf_fullName;
    EditText pf_address;
    EditText pf_phoneNumber;
    EditText pf_birthday;
    TextView bt_logout;
    TextView bt_update_password;

    String url;
    String jsonString = null;

    String id;
    String idRole, idMember;
    String username;
    String txtFullName;
    String txtAddress;
    String txtPhone;
    String txtEmail;
    String txtBirthday;

    String  newEmail,
            newFullName,
            newAddress,
            newPhone,
            newBirthday;

    ImageButton bt_Date;
    Calendar calendar = Calendar.getInstance();
    String message;

    SessionManagement session;
    String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initCollapsingToolbar();

        session = new SessionManagement(getApplicationContext());
        accessToken = oauth.getAccess_token();

        pf_username = (EditText) findViewById(R.id.profile_username);
        pf_email = (EditText) findViewById(R.id.profile_email);
        pf_fullName = (EditText) findViewById(R.id.profile_fullname);
        pf_address = (EditText) findViewById(R.id.profile_address);
        pf_phoneNumber = (EditText) findViewById(R.id.profile_phoneNumber);
        pf_birthday = (EditText) findViewById(R.id.profile_birthday);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        bt_logout = (TextView) findViewById(R.id.bt_logout);
        bt_update_password = (TextView) findViewById(R.id.profile_changePassword);
        bt_Date = (ImageButton) findViewById(R.id.bt_Date);

        showInformationAccount();

        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
            }
        });

        bt_update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdatePasswordActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        bt_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ProfileActivity.this, listener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
    }

    // listener tao DatePickerDialog dinh dang chuoi hien thi
    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            pf_birthday.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
        }
    };


    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.save_profile: {
                username = pf_username.getText().toString();
                txtEmail = pf_email.getText().toString();
                txtFullName = pf_fullName.getText().toString();
                txtAddress = pf_address.getText().toString();
                txtPhone = pf_phoneNumber.getText().toString();
                txtBirthday = pf_birthday.getText().toString();
                if (checkValidate()) {
                    startAsyncTaskGetAPI();
                } else {
                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            }
            case android.R.id.home:
                Intent i = new Intent(this, MemberMainActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }

        return true;
    }

    public void showInformationAccount() {
        HashMap<String, String> user = session.getUserDetails();
        id = user.get(SessionManagement.ID);
        idRole = user.get(SessionManagement.KEY_ID_ROLE);
        idMember = user.get(SessionManagement.KEY_ID_MEMBER);

        username = user.get(SessionManagement.KEY_USERNAME);
        pf_username.setText(username);

        txtFullName = user.get(SessionManagement.KEY_FULL_NAME);
        pf_fullName.setText(txtFullName);

        txtAddress = user.get(SessionManagement.KEY_ADDRESS);
        pf_address.setText(txtAddress);

        txtPhone = user.get(SessionManagement.KEY_PHONE_NUMBER);
        pf_phoneNumber.setText(txtPhone);

        txtEmail = user.get(SessionManagement.KEY_EMAIL);
        pf_email.setText(txtEmail);

        txtBirthday = user.get(SessionManagement.KEY_BIRTHDAY);
        pf_birthday.setText(txtBirthday);
    }

    public void startAsyncTaskGetAPI() {
        url = Uri.parse(getString(R.string.host_name)).buildUpon()
                .appendPath("api")
                .appendPath("accounts")
                .appendPath(id)
                .appendQueryParameter("access_token", accessToken)
                .build().toString();
        new AsyncTaskLoadUpdateUser().execute();
    }

    public boolean checkValidate() {
        boolean valid = true;
        if (!username.matches(getString(R.string.regex_username))) {
            pf_username.setError(getString(R.string.valid_username));
            valid = false;
        } else pf_username.setError(null);

        if (!txtFullName.matches("[0-9a-zA-Z\\sÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ]{1,}")) {
            pf_fullName.setError(getString(R.string.valid_string_name));
            valid = false;
        } else pf_fullName.setError(null);

        if (!txtAddress.matches("[0-9a-zA-Z\\sÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ]{1,}")) {
            pf_address.setError(getString(R.string.valid_string_name));
            valid = false;
        } else pf_address.setError(null);

        if (!txtPhone.matches(getString(R.string.regex_phoneNumber))) {
            pf_phoneNumber.setError(getString(R.string.valid_string_phone));
            valid = false;
        } else pf_phoneNumber.setError(null);

        if (!txtEmail.matches(getString(R.string.regex_email))) {
            pf_email.setError(getString(R.string.valid_format_email));
            valid = false;
        }

        return valid;
    }

    private void updateJsonString() {
        try {
            JSONObject reader = new JSONObject(jsonString);
            if (reader.length() == 1) {
                if (reader.has("message")) {
                    message = reader.getString("message");
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setMessage(message)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();

                    dialog.show();
                } else {
                    JSONObject account = reader.getJSONObject("account");
                    newEmail = account.getString("email");
                    newAddress = account.getString("address");
                    newFullName = account.getString("fullName");
                    newPhone = account.getString("phonenumber");
                    newBirthday = account.getString("birthday");

                    session.createLoginSession(id, username, newEmail, newFullName, newAddress, newPhone, newBirthday, idRole, idMember);

                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setMessage(getString(R.string.update_successful))
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();

                    dialog.show();
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setMessage(getString(R.string.failed_access_token))
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();

                dialog.show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // cap nhat du lieu len server bang phuong thuc POST cua OkHttp
    private class AsyncTaskLoadUpdateUser extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            jsonString = s;
            updateJsonString();

        }

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");    //dinh nghia du lieu gui den server laf mot file JSON
            Map<String, String> prs = new HashMap<>();                              //HashMap luu tru key, value cua params truyen di duoi dang JSON
            prs.put("username", username);
            prs.put("fullName", txtFullName);
            prs.put("address", txtAddress);
            prs.put("phonenumber", txtPhone);
            prs.put("email", txtEmail);
            prs.put("birthday", txtBirthday);
            prs.put("idRole", idRole);
            prs.put("idMember", idMember);
            JSONObject parameter = new JSONObject(prs);                             //tham khao cach POST theo OkHttp tai http://square.github.io/okhttp/

            RequestBody postData = RequestBody.create(JSON, parameter.toString());
            Request request = new Request.Builder().url(url).put(postData).addHeader("content-type", "application/json; charset=utf-8").build();
            try {
                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return response.body().string();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return getString(R.string.error_getAPI);

        }
    }

}
