package com.example.edeze_v1;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

// db
public class FindTutorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    private CameraPosition cameraPosition;

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    SearchView searchView;
    List<tutorClass> tutorList = new ArrayList<tutorClass>();
    String[] navbar_temp_array;
    boolean locationPermissionGranted;
    int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    // The entry point to the Places API.
    private PlacesClient placesClient;

    private Location lastKnownLocation;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    private static final String TAG = "FindTutorActivity";
    private static final int DEFAULT_ZOOM = 15;
    private final LatLng defaultLocation = new LatLng(37.3496, -121.9390);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "On Create");
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        setContentView(R.layout.activity_tutors_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //remove title from action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        searchView = findViewById(R.id.search_view_location);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map_fragment);
        mapFragment.getMapAsync(this::onMapReady);
        // Initialize the SDK
        Places.initialize(getApplicationContext(), "AIzaSyDofNZzNPiFIRUKHg4pcc6lOkUNZ7n-7tE");

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                if (location != null || !location.equals("")){
                    Geocoder geocoder = new Geocoder(FindTutorActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                }
                return false;
            }


            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        mapFragment.getMapAsync(this::onMapReady);

        // update tutor list on the screen
        List<String> subj_list1 = new ArrayList<String>();
        subj_list1.add("Calculus");
        subj_list1.add("Physics");
        tutorClass tutor1 = new tutorClass("Angela", 37.330, -121.862, "San Jose", subj_list1);
        tutorClass tutor2 = new tutorClass("Bob", 37.777, -122.421, "San Francisco", subj_list1);
        tutorClass tutor3 = new tutorClass("Fred", 37.330,-121.862,"San Jose",subj_list1);
        tutorClass tutor4 = new tutorClass("Jessica", 37.330,-121.862,"San Jose",subj_list1);
        tutorClass tutor5 = new tutorClass("Roger", 37.354,-121.954,"Santa Clara",subj_list1);
        tutorClass tutor6 = new tutorClass("Wendy", 37.777,-122.421,"San Francisco",subj_list1);
        tutorClass tutor7 = new tutorClass("Vernon", 37.354,-121.954,"Santa Clara",subj_list1);

        Button tutorButton1 = findViewById(R.id.tutor1button);
        //tutorButton1.setOnClickListener((View.OnClickListener) this);
        Button tutorButton2 = findViewById(R.id.tutor2button);
        Button tutorButton3 = findViewById(R.id.tutor3button);
        Button tutorButton4 = findViewById(R.id.tutor4button);
        Button tutorButton5 = findViewById(R.id.tutor5button);
        Button tutorButton6 = findViewById(R.id.tutor6button);
        Button tutorButton7 = findViewById(R.id.tutor7button);

        tutorButton1.setText(tutor1.getIdentifier() + ", " + tutor1.getCity());
        tutorButton2.setText(tutor2.getIdentifier() + ", " + tutor2.getCity());
        tutorButton3.setText(tutor3.getIdentifier() + ", " + tutor3.getCity());
        tutorButton4.setText(tutor4.getIdentifier() + ", " + tutor4.getCity());
        tutorButton5.setText(tutor5.getIdentifier() + ", " + tutor5.getCity());
        tutorButton6.setText(tutor6.getIdentifier() + ", " + tutor6.getCity());
        tutorButton7.setText(tutor7.getIdentifier() + ", " + tutor7.getCity());


        Resources res = getResources();
        navbar_temp_array = res.getStringArray(R.array.navbar_array);
        setSupportActionBar(toolbar);
        Spinner dropdown = findViewById(R.id.navSpinner);
        dropdown.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.navbar_array, android.R.layout.simple_spinner_item);
        dropdown.setAdapter(adapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"On Start");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"On Resume");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.i(TAG,"On Pause");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"On Stop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(TAG,"On Destroy");
    }


    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocationUI();
    }



    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation =null ;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    //@Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        googleMap.addMarker(new MarkerOptions()
                .position(defaultLocation)
                .title("Marker in Santa Clara"));
        updateLocationUI();
        getDeviceLocation();
    }


    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }



    public void viewTutorClick(View view) {
        // will be added in a later phase
        Intent intent = new Intent(this, FindTutorActivity.class);
        startActivity(intent);
    }

    public void toolbarProfileClick(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Context context = getApplicationContext();
        CharSequence text = navbar_temp_array[pos];
        if (text.toString().equals("Profile")){
            Intent intent = new Intent(this,ProfileActivity.class);
            startActivity(intent);
        }
        else if(text.toString().equals("Settings")){
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        }
        else if(text.toString().equals("Logout")){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else if(text.toString().equals("Q&A")){
            Intent intent = new Intent(this, QAActivity.class);
            startActivity(intent);
        }
        else if(text.toString().equals("Tutors")){
            //do nothing; we are in tutors
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
