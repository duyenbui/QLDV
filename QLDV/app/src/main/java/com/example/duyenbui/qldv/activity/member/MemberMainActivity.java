package com.example.duyenbui.qldv.activity.member;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.duyenbui.qldv.R;
import com.example.duyenbui.qldv.activity.MapActivity;
import com.example.duyenbui.qldv.fragment.LibrarySpeciesFragment;
import com.example.duyenbui.qldv.fragment.MapsFragment;

public class MemberMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , LibrarySpeciesFragment.OnFragmentInteractionListener, MapsFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.member_toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = LibrarySpeciesFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.member_fl_container, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.member_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.member_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.member_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
//                onRestart();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.member_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.member_action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();
        Fragment fragment = null;
        Class fragmentClass = null;
        int id = item.getItemId();

        if (id == R.id.member_nav_map) {
            sFm.beginTransaction().replace(R.id.guest_fl_container, new MapsFragment()).commit();
        } else {
            if (id == R.id.member_nav_library) {
                fragmentClass = LibrarySpeciesFragment.class;
            } else if (id == R.id.member_nav_supplyInformation) {

            } else if (id == R.id.member_nav_contact) {

            } else if (id == R.id.member_nav_profile) {

            } else if (id == R.id.member_nav_search_image) {

            } else if (id == R.id.member_nav_search_keyword) {

            }

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            fragmentManager.beginTransaction().replace(R.id.member_fl_container, fragment).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.member_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
