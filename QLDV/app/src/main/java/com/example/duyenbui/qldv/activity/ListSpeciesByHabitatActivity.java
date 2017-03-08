package com.example.duyenbui.qldv.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.duyenbui.qldv.R;
import com.example.duyenbui.qldv.fragment.MapsFragment;
import com.example.duyenbui.qldv.object.Habitat;

import io.realm.RealmResults;

import static com.example.duyenbui.qldv.fragment.LibrarySpeciesFragment.realm;

public class ListSpeciesByHabitatActivity extends AppCompatActivity {

    int idHabitat;
    private RecyclerView recyclerView;

    RealmResults<Habitat> habitat;

    TextView title;

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
        title.setText("Động vật quý hiếm tại "
                + habitat.first().getLocationName()
                + " Tọa độ ( " + habitat.first().getLatitude()
                +", "+ habitat.first().getLongitude()+" )");
//        title.setText("");
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
//                Intent intent = new Intent(getApplicationContext(), MapsFragment.class);
//                startActivity(intent);
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
}
