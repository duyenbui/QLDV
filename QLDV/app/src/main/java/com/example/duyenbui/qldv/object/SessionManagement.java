package com.example.duyenbui.qldv.object;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.duyenbui.qldv.activity.guest.GuestLoginActivity;
import com.example.duyenbui.qldv.activity.guest.GuestMainActivity;

import java.util.HashMap;

/**
 * Created by code-engine-studio on 01/03/2017.
 */

public class SessionManagement {
    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context context;

    int PRIVATE_MODE = 0;

    //Sharedpref file name
    private static final String PREF_NAME = "Biodiversity";

    public static final String ID = "id";
    public static final String KEY_USERNAME = "userName";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FULL_NAME = "fullName";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_PHONE_NUMBER = "phoneNumber";
    public static final String KEY_BIRTHDAY = "birthday";
    public static final String KEY_ID_ROLE = "idRole";
    public static final String KEY_ID_MEMBER = "idMember";


    public SessionManagement(Context _context) {
        this.context = _context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }

    //Create login session
    public void createLoginSession(String id, String username, String email, String fullName, String address, String phoneNumber, String birthday, String idRole, String idMember){
        editor = pref.edit();

        editor.putString(ID, id);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_FULL_NAME, fullName);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_PHONE_NUMBER, phoneNumber);
        editor.putString(KEY_BIRTHDAY, birthday);
        editor.putString(KEY_ID_ROLE, idRole);
        editor.putString(KEY_ID_MEMBER, idMember);
        editor.commit();
    }

    //Get stored session data
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(ID, pref.getString(ID, null));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_FULL_NAME, pref.getString(KEY_FULL_NAME, null));
        user.put(KEY_ADDRESS, pref.getString(KEY_ADDRESS, null));
        user.put(KEY_PHONE_NUMBER, pref.getString(KEY_PHONE_NUMBER, null));
        user.put(KEY_BIRTHDAY, pref.getString(KEY_BIRTHDAY, null));
        user.put(KEY_ID_ROLE, pref.getString(KEY_ID_ROLE, null));
        user.put(KEY_ID_MEMBER, pref.getString(KEY_ID_MEMBER, null));

        return user;
    }

    //Clear session details
    public void logoutUser(){
        editor.clear();
        editor.commit();

        Intent i = new Intent(context, GuestMainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

}
