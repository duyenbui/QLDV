package com.example.duyenbui.qldv.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.duyenbui.qldv.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements LocationListener {

    private GoogleMap myMap;
    private ProgressDialog myProgress;

    // private static final String UserTag = "UserTag";

    //ma yeu cau nguoi dung cho xem vi tri hien tai cua ho
    public static final int REQUEST_ID_ACCESS_LOCATION = 120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //tao progress bar
        myProgress = new ProgressDialog(this);
        myProgress.setMessage("Loading...");
        myProgress.setCancelable(true);

        myProgress.show();

        //truy xuat va su dung doi tuowng GoogleMap tu the fragment trong XML
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment);

        //tao su kien Map san sang;
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override

            public void onMapReady(GoogleMap googleMap) {
                isMapReady(googleMap);

            }
        });

    }

    public void isMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        //thiet lap su kien khi tai Map thanh cong
        myMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                myProgress.dismiss();
                askPermissionAndShowLocation();
            }
        });

        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        myMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        myMap.setMyLocationEnabled(true);
    }

    public void askPermissionAndShowLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            int accessCoarsePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            int accessFinePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

            if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED || accessFinePermission != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
                ActivityCompat.requestPermissions(this, permissions, REQUEST_ID_ACCESS_LOCATION);
                return;
            }
        }
        this.showMyLocation();
    }

    private String getLocationProviderEnabled() {     //Tim mot nha cung cap vi tri
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria(); //tieu chi tim nha cung cap
        String getProvider = locationManager.getBestProvider(criteria, true);
        Toast.makeText(this, getProvider, Toast.LENGTH_SHORT).show();

        if (!locationManager.isProviderEnabled(getProvider)) {
            Toast.makeText(this, "no provider enable!", Toast.LENGTH_SHORT).show();
        //    Log.i(UserTag, "No location provider enabled!");
            return null;
        }

        return getProvider;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        while(requestCode == REQUEST_ID_ACCESS_LOCATION){
            if(grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
          //      Toast.makeText(this, "request success!", Toast.LENGTH_LONG).show();
                showMyLocation();
            }
            else{
                Toast.makeText(this, "request failed!", Toast.LENGTH_LONG).show();
            }
            break;
        }

    }

    public void showMyLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        String provider = getLocationProviderEnabled();
        if (provider == null) {
            Toast.makeText(this, "provider not founded", Toast.LENGTH_SHORT).show();
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
            myLocation = locationManager.getLastKnownLocation(provider);
//            if(myLocation == null){
//                Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
//            }
        }
        catch (SecurityException e){
            Toast.makeText(this, "Error show location: "+e.getMessage(), Toast.LENGTH_LONG).show();
          //  Log.e(UserTag, "Error show location:" + e.getMessage());
            e.printStackTrace();
            return;
        }

        if (myLocation != null) {

            //lay kinh do, vi do cua vi tri myLocation
            LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)             // Sets the center of the map to location user
                    .zoom(15)                   // Sets the zoom
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
            Toast.makeText(this, "Do not create marker", Toast.LENGTH_LONG).show();
           // Log.e(UserTag, "Do not create marker");
        }

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
}
