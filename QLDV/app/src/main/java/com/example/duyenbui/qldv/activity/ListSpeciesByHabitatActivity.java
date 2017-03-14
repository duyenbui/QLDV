package com.example.duyenbui.qldv.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duyenbui.qldv.R;
import com.example.duyenbui.qldv.adapter.ListSpeciesByHabitatAdapter;
import com.example.duyenbui.qldv.fragment.MapsFragment;
import com.example.duyenbui.qldv.object.Habitat;
import com.example.duyenbui.qldv.object.Species;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.duyenbui.qldv.fragment.LibrarySpeciesFragment.realm;

public class ListSpeciesByHabitatActivity extends AppCompatActivity {

    int idHabitat;
    int idItem;
    String jsonString, url;
    private RecyclerView recyclerView;
    private RealmResults<Species> items;
    ArrayList<Species> speciesList;

    RealmResults<Habitat> habitat;

    TextView title, Lat_lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_species_by_habitat);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent i = getIntent();
        idHabitat = i.getIntExtra("idMarker", 1);

        realm.beginTransaction();
        habitat = realm.where(Habitat.class).equalTo("id", idHabitat).findAll();
        realm.commitTransaction();

        title = (TextView) findViewById(R.id.title_list_by_habitat);
        Lat_lng = (TextView) findViewById(R.id.title_list_by_habitat1);
        title.setText("Động vật quý hiếm tại "
                + habitat.first().getLocationName());
        Lat_lng.setText("Tọa độ ( "+ habitat.first().getLatitude()
                +", "+ habitat.first().getLongitude()+" )");

        recyclerView = (RecyclerView) findViewById(R.id.list_species_recyclerView_byHabitat);

        startAsyncTaskGetAPILoadListSpeciesByHabitat();

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                onBackPressed();
                break;
            }
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void startAsyncTaskGetAPILoadListSpeciesByHabitat() {
        url = Uri.parse(getString(R.string.host_name)).buildUpon()
                .appendPath("api")
                .appendPath("habitat")
                .appendPath("species")
                .appendPath(String.valueOf(idHabitat))
                .build().toString();
        Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
        new AsyncTaskLoadListSpecies().execute(url);
    }

    private void showListSpecies(){
        if (jsonString != null) {
            try {
                speciesList = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray arraySpecies = jsonObject.getJSONArray("specieses");

                realm.beginTransaction();
                realm.createOrUpdateAllFromJson(Species.class, arraySpecies);
                realm.commitTransaction();

                for (int i = 0; i < arraySpecies.length(); i++) {
                    JSONObject species = arraySpecies.getJSONObject(i);
                    int id = species.getInt("id");

                    items = realm.where(Species.class).equalTo("id", id).findAll();
                    speciesList.add(items.get(0));
                }

                recyclerView.setAdapter( new ListSpeciesByHabitatAdapter(getApplicationContext(),speciesList, new ListSpeciesByHabitatAdapter.OnItemSpeciesClickListener() {
                            @Override
                            public void onItemClick(Species speciesItem) {
                                idItem = speciesItem.getId();
                                Intent i = new Intent(getApplicationContext(), SpeciesDetailActivity.class);
                                i.putExtra("idItem", idItem);
                                startActivity(i);
                            }
                        })
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
            showListSpecies();
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
