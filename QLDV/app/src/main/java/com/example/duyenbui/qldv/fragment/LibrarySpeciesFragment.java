package com.example.duyenbui.qldv.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.duyenbui.qldv.R;
import com.example.duyenbui.qldv.activity.SpeciesDetailActivity;
import com.example.duyenbui.qldv.adapter.ListSpeciesAdapter;
import com.example.duyenbui.qldv.object.ConnectDetector;
import com.example.duyenbui.qldv.object.Species;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LibrarySpeciesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LibrarySpeciesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibrarySpeciesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Boolean connection = false;

    int idItem;

    private RecyclerView recyclerView;
    private ProgressDialog myProgress;

    String url;
    String jsonString = null;

    public static Realm realm;
    private RealmResults<Species> items;


    public LibrarySpeciesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibrarySpeciesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibrarySpeciesFragment newInstance(String param1, String param2) {
        LibrarySpeciesFragment fragment = new LibrarySpeciesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guest_library_species, container, false);

        //tao progress bar
        myProgress = new ProgressDialog(getContext());
        myProgress.setMessage("Loading...");
        myProgress.setCancelable(true);

        myProgress.show();

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(getContext()).build();
        Realm.setDefaultConfiguration(realmConfiguration);
        try{
            realm = Realm.getDefaultInstance();
        } catch (Exception e){
            RealmConfiguration config = new RealmConfiguration.Builder(getContext())
                    .deleteRealmIfMigrationNeeded()
                    .build();
            realm = Realm.getInstance(config);
        }


        if (checkInternet()) {

            startAsyncTaskGetAPI();
            myProgress.dismiss();
            if (!checkExistRealmObject()) {
                Toast.makeText(getContext(), "Tao RealmObject", Toast.LENGTH_SHORT).show();
                startAsyncTaskGetAPI();
            }

            recyclerView = (RecyclerView) view.findViewById(R.id.list_species_recycler_view);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());


        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getString(R.string.check_internet))
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }


        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
//        MenuItem mSearchMenuItem = menu.findItem(R.id.action_search);
//
//        mSearchMenuItem.setVisible(true);
//
//        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                newText = newText.toLowerCase();
//                List<Species> newList = new ArrayList<>();
//                for(Species species : items){
//                    String vietnameseName = species.getVietnameseName().toLowerCase();
//                    String otherName = species.getOtherName().toLowerCase();
//                    if(vietnameseName.contains(newText) || otherName.contains(newText)){
//                        newList.add(species);
//                    }
//
//                }
//                recyclerView.setAdapter( new ListSpeciesAdapter(getContext(),newList, new ListSpeciesAdapter.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(Species speciesItem) {
//                                int idItem = speciesItem.getId();
//                                Intent i = new Intent(getContext(), SpeciesDetailActivity.class);
//                                i.putExtra("idItem", idItem);
//                                startActivity(i);
//                            }
//                        })
//                );
//                return true;
//            }
//        });
    }

    public boolean checkInternet() {
        ConnectDetector cd = new ConnectDetector(getContext());
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
        url = Uri.parse(getString(R.string.host_name)).buildUpon()
                .appendPath("api")
                .appendPath("species")
                .build().toString();
        new AsyncTaskLoadListSpecies().execute(url);
    }

    public void createListSpecies() {
        if (jsonString != null) {
            try {

                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray arraySpecies = jsonObject.getJSONArray("specieses");

                realm.beginTransaction();
                realm.createOrUpdateAllFromJson(Species.class, arraySpecies);
                realm.commitTransaction();

                items = realm.where(Species.class).findAll();
                recyclerView.setAdapter( new ListSpeciesAdapter(getContext(),items, new ListSpeciesAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Species speciesItem) {
                                idItem = speciesItem.getId();
                                Intent i = new Intent(getActivity(), SpeciesDetailActivity.class);
                                i.putExtra("idItem", idItem);
                                startActivity(i);
                            }
                        })
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

            Builder builder = new Builder();
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.writeTimeout(10, TimeUnit.SECONDS);
            OkHttpClient client = builder.build();

            Request request = new Request.Builder().url(url).get().build();
//            for(int retries = 0; retries < 7; retries++){
                try {
                    Response response = client.newCall(request).execute();

                    if (response.isSuccessful()) {
                        return response.body().string();
                    }
                } catch (final java.net.SocketTimeoutException e) {
                    e.printStackTrace();
//                    continue;
                } catch (IOException e) {
                    e.printStackTrace();
                }
//            }

            return getString(R.string.error_getAPI);

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
