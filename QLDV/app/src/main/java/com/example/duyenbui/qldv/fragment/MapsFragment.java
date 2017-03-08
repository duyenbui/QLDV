package com.example.duyenbui.qldv.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.duyenbui.qldv.R;
import com.example.duyenbui.qldv.activity.ListSpeciesByHabitatActivity;
import com.example.duyenbui.qldv.object.ConnectDetector;
import com.example.duyenbui.qldv.object.Habitat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.LOCATION_SERVICE;
import static com.example.duyenbui.qldv.fragment.LibrarySpeciesFragment.realm;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsFragment extends Fragment implements LocationListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //Tao flag kiem tra internet
    Boolean connection = false;
    private View v;
    MapView mapView;
    private GoogleMap myMap;
    private ProgressDialog myProgress;
    FragmentManager fm;

    String jsonString;
    String url = null;

    private RealmResults<Habitat> itemsHabitat;

    //ma yeu cau nguoi dung cho xem vi tri hien tai cua ho
    public static final int REQUEST_ID_ACCESS_LOCATION = 120;

    public MapsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapsFragment newInstance(String param1, String param2) {
        MapsFragment fragment = new MapsFragment();
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
        v =  inflater.inflate(R.layout.fragment_maps, container, false);

        if (checkInternet()) {
        //tao progress bar
            myProgress = new ProgressDialog(getContext());
            myProgress.setMessage("Loading...");
            myProgress.setCancelable(true);

            myProgress.show();

            mapView = (MapView) v.findViewById(R.id.map);
            mapView.onCreate(savedInstanceState);

            mapView.onResume();

            try{
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e){
                e.printStackTrace();
            }

            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    isMapReady(googleMap);
                }
            });

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(R.string.check_internet)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public boolean checkInternet() {
        ConnectDetector cd = new ConnectDetector(getContext());
        connection = cd.isConnectingToInternet();
        if (!connection) {
            return false;
        }
        return true;
    }

    public void isMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        //thiet lap su kien khi tai Map thanh cong
        myMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                myProgress.dismiss();
                askPermissionAndShowLocation();
                startGetAPIShowLocation();
            }
        });

        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        myMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the UserAccount grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        myMap.setMyLocationEnabled(true);

        myMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int idMarker = Integer.parseInt(marker.getTitle());
                Intent intent = new Intent(getActivity(), ListSpeciesByHabitatActivity.class);
                intent.putExtra("idMarker", idMarker);
                startActivity(intent);
                return false;
            }
        });

    }

    public void askPermissionAndShowLocation() {
        if (Build.VERSION.SDK_INT >= 21) {
            int accessCoarsePermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
            int accessFinePermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);

            if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED || accessFinePermission != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
                ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_ID_ACCESS_LOCATION);
                return;
            }
        }
        this.showMyLocation();
        startGetAPIShowLocation();
    }

    private String getLocationProviderEnabled() {     //Tim mot nha cung cap vi tri
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria(); //tieu chi tim nha cung cap
        String getProvider = locationManager.getBestProvider(criteria, true);
        Toast.makeText(getContext(), getProvider, Toast.LENGTH_SHORT).show();

        if (!locationManager.isProviderEnabled(getProvider)) {
            Toast.makeText(getContext(), "no provider enable!", Toast.LENGTH_SHORT).show();
            return null;
        }

        return getProvider;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        while(requestCode == REQUEST_ID_ACCESS_LOCATION){
            if(grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                //      Toast.makeText(this, "request success!", Toast.LENGTH_LONG).show();
                showMyLocation();
            }
            else{
                Toast.makeText(getContext(), "request failed!", Toast.LENGTH_LONG).show();
            }
            break;
        }
    }

    private void showMyLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        String provider = getLocationProviderEnabled();
        if (provider == null) {
            Toast.makeText(getContext(), "provider not founded", Toast.LENGTH_SHORT).show();
        }

        // Millisecond
        final long MIN_TIME = 1000;
        // Met
        final float MIN_DISTANCE = 1;
        Location myLocation = null;

        try {
            //yeu cau ng dung xac nhan cho xem dia chi
            locationManager.requestLocationUpdates(provider, MIN_TIME, MIN_DISTANCE, this);
            //lay ra dia chi
            if(Build.VERSION.SDK_INT >= 21){
                myLocation = locationManager.getLastKnownLocation(provider);
            } else {
                myLocation = myMap.getMyLocation();
            }

        }
        catch (SecurityException e){
            Toast.makeText(getContext(), "Error show location: "+e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return;
        }

        if (myLocation != null) {

            //lay kinh do, vi do cua vi tri myLocation
            LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)             // Sets the center of the map to location UserAccount
                    .zoom(12)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            // Thêm Marker cho Map:
            MarkerOptions option = new MarkerOptions();
            option.title("My Location");
            option.snippet("....");
            option.position(latLng);
            Marker currentMarker = myMap.addMarker(option);
            currentMarker.showInfoWindow();
        } else {
            Toast.makeText(getContext(), "Do not create marker", Toast.LENGTH_LONG).show();
        }

        LatLng danang = new LatLng(16.060960, 108.227182);
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(danang, 12));

        myMap.addMarker(new MarkerOptions()
                        .title("1")
                        .position(danang));

        LatLng dn = new LatLng(16.060960, 108.220000);
        myMap.addMarker(new MarkerOptions()
                .title("2")
                .position(dn));

    }

    public void startGetAPIShowLocation(){
        url = Uri.parse(getString(R.string.host_name)).buildUpon()
                .appendPath("api")
                .appendPath("habitats")
                .build().toString();
        new AsyncTaskLoadListLocation().execute();
    }

    public void showMarkerLocation(){
        if(jsonString != null){
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray arrayHabitat = jsonObject.getJSONArray("habitats");

                realm.beginTransaction();
                realm.createOrUpdateAllFromJson(Habitat.class, arrayHabitat);
                realm.commitTransaction();

                itemsHabitat = realm.where(Habitat.class).findAll();

                for(int i = 0; i < arrayHabitat.length(); i++){
                    JSONObject location = arrayHabitat.getJSONObject(i);
                    int id = location.getInt("id");
                    double lat = location.getDouble("latitude");
                    double lng = location.getDouble("longitude");
                    LatLng showLocation = new LatLng(lat, lng);
                    myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(showLocation, 12));

                    myMap.addMarker(new MarkerOptions()
                            .title(String.valueOf(id))
                            .position(showLocation));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else{
            Toast.makeText(getContext(), "Don't create marker of habitat", Toast.LENGTH_LONG).show();
        }
    }

    private class AsyncTaskLoadListLocation extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            jsonString = s;
            showMarkerLocation();
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

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
