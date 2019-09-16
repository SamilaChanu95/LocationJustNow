package com.example.locationjustnow;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.example.locationjustnow.Model.CountryDataSource;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    //Get the country from main activity class
    private String receiveCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //intent use for data exchange with main activity
        Intent mainActivityIntent = this.getIntent();
        receiveCountry = mainActivityIntent.getStringExtra(CountryDataSource.COUNTRY_KEY);
        if (receiveCountry == null) {

            receiveCountry = CountryDataSource.DEFAULT_COUNTRY_NAME;
        }

        // before the map in ready so need to get the country
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //if map is ready then get the data from MainActivity
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /* using for create circle and mark the particular location
        //65.003542, -18.309250 iceland

        // Add a marker in Iceland and move the camera
        LatLng iceland = new LatLng(65.003542, -18.309250);

        //create cameraUpdate variable
        CameraUpdate cameraUpdate;
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(iceland, 14.0f);

        mMap.moveCamera(cameraUpdate);
        //mMap.addMarker(new MarkerOptions().position(iceland).title("Marker in iceland"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(iceland));

        //create variable
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(iceland);
        markerOptions.title("Welcome to Iceland.");
        markerOptions.snippet("Fantastic");
        mMap.addMarker(markerOptions);

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(iceland);
        circleOptions.radius(300);
        circleOptions.strokeWidth(20.0f);
        circleOptions.strokeColor(Color.YELLOW);
        mMap.addCircle(circleOptions);
        */

        double countryLatitude = CountryDataSource.DEFAULT_COUNTRY_LATITUDE;
        double countryLongitude = CountryDataSource.DEFAULT_COUNTRY_LONGITUDE;

        CountryDataSource countryDataSource = MainActivity.countryDataSource;

        //get the value from getTheInfoOfTheCountry method in CountryDatasource java class
        String countryMessage = countryDataSource.getTheInfoOfTheCountry(receiveCountry);

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        try {

            String countryAddress = receiveCountry;
            //Address is a class that representing an address such as set of Strings describing a location.
            List <Address> countryAddresses = geocoder.getFromLocationName(countryAddress,10);

            if(countryAddresses != null) {

                countryLatitude = countryAddresses.get(0).getLatitude();
                countryLongitude = countryAddresses.get(0).getLongitude();

            }

            else {

                receiveCountry = CountryDataSource.DEFAULT_COUNTRY_NAME;

            }

        } catch (IOException ioe) {

            receiveCountry = CountryDataSource.DEFAULT_COUNTRY_NAME;

        }

        LatLng myCountryLocation = new LatLng(countryLatitude, countryLongitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myCountryLocation, 12.0f);
        mMap.moveCamera(cameraUpdate);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(myCountryLocation);
        markerOptions.title(countryMessage);
        markerOptions.snippet(CountryDataSource.DEFAULT_MESSAGE);
        mMap.addMarker(markerOptions);

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(myCountryLocation);
        circleOptions.radius(400);
        circleOptions.strokeWidth(10.0f);
        circleOptions.strokeColor(Color.CYAN);
        mMap.addCircle(circleOptions);

    }
}
