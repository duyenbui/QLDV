package com.example.duyenbui.qldv.activity;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.duyenbui.qldv.R;
import com.example.duyenbui.qldv.adapter.ListSpeciesAdapter;
import com.example.duyenbui.qldv.fragment.LibrarySpeciesFragment;
import com.example.duyenbui.qldv.object.ConnectDetector;
import com.example.duyenbui.qldv.object.Species;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListSpeciesActivity extends AppCompatActivity {

    Boolean connection = false;

    private RecyclerView recyclerView;
    private ListSpeciesAdapter adapter;
    private List<Species> listSpecies;

    String url;
    String jsonString = null;
    public Realm realm;
    private RealmResults<Species> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_species);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(getApplicationContext()).build();
        Realm.setDefaultConfiguration(realmConfiguration);
        try{
            realm = Realm.getDefaultInstance();
        } catch (Exception e){
            RealmConfiguration config = new RealmConfiguration.Builder(getApplicationContext())
                    .deleteRealmIfMigrationNeeded()
                    .build();
            realm = Realm.getInstance(config);
        }

        if (checkInternet()) {

            startAsyncTaskGetAPI();

            if (checkExistRealmObject()) {
                Toast.makeText(this, "Co RealmObject", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "K ton tai RealmObject", Toast.LENGTH_SHORT).show();
            }

            recyclerView = (RecyclerView) findViewById(R.id.list_species_recycler_view);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter( new ListSpeciesAdapter(getApplicationContext(),items, new ListSpeciesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Species speciesItem) {
                    Toast.makeText(ListSpeciesActivity.this, "Click", Toast.LENGTH_SHORT).show();
                }
            })
            );


        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ListSpeciesActivity.this);
            builder.setMessage(getString(R.string.check_internet))
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }


    }

    public boolean checkInternet() {
        ConnectDetector cd = new ConnectDetector(getApplicationContext());
        connection = cd.isConnectingToInternet();
        if (!connection) {
            return false;
        }
        return true;
    }

    public boolean checkExistRealmObject() {
        items = realm.where(Species.class).findAll();
        if (items != null) {
            return true;
        } else return false;
    }

    public void startAsyncTaskGetAPI() {
        url = Uri.parse(getString(R.string.host_name)).buildUpon().appendPath("api").appendPath("species").build().toString();
        Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
        new AsyncTaskLoadListSpecies().execute(url);
    }

    public void createListSpecies() {
        if (jsonString != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray arraySpecies = jsonObject.getJSONArray("specieses");
                Toast.makeText(getApplicationContext(), String.valueOf(arraySpecies.length()), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < arraySpecies.length(); i++) {
                    JSONObject species = arraySpecies.getJSONObject(i);
                    int id = species.getInt("id");
                    String vietnameseName = species.getString("vietnameseName");
                    String scienceName = species.getString("scienceName");
                    String image = species.getString("image");
                    String nameFamily = species.getString("vietnameseNameFamily");

                    Species addSpecies = new Species(id, vietnameseName, scienceName, nameFamily, image);
                    realm.beginTransaction();
                    Species copyOfSpecies = realm.copyToRealm(addSpecies);
                    realm.commitTransaction();
                }
                items = realm.where(Species.class).findAll();
                adapter.setList(items);
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ListSpeciesActivity.this);
            builder.setMessage(getString(R.string.error_get_data_of_list_species))
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    private class AsyncTaskLoadListSpecies extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            jsonString = s;
            createListSpecies();
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
