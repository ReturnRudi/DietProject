package com.example.dietproject;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.dietproject.databinding.ActivityMapShowBinding;

public class MapShow extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapShowBinding binding;
    String latitude, longitude;
    double latitude_d, longitude_d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapShowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        latitude = intent.getExtras().getString("위도");
        longitude = intent.getExtras().getString("경도");

        latitude_d = Double.parseDouble(latitude);
        longitude_d = Double.parseDouble(longitude);
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

        // Add a marker in Sydney and move the camera

        LatLng position = new LatLng(latitude_d, longitude_d);
        mMap.addMarker(new MarkerOptions().position(position).title("식사한 위치"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
        //mMap.addMarker(new MarkerOptions().position(startingPoint).title("Marker in Dongguk"));
    }
}