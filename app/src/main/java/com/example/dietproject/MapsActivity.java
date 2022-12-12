package com.example.dietproject;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.dietproject.databinding.ActivityMapsBinding;
import com.google.common.collect.Maps;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    Double latitude;
    Double longitude;
    String latitude_s;
    String longtitude_s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(@NonNull Marker marker) {
            Intent intent = new Intent();
            intent.putExtra("위도", latitude.toString());
            intent.putExtra("경도", longitude.toString());
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng startingPoint = new LatLng(37.5584, 126.9991);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 16));
        //mMap.addMarker(new MarkerOptions().position(startingPoint).title("Marker in Dongguk"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startingPoint));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng Point) {
                MarkerOptions markerOptions = new MarkerOptions();

                markerOptions.title("이곳을 터치해서 위치를 입력하세요");
                latitude = Point.latitude; // 위도
                longitude = Point.longitude; // 경도

                markerOptions.snippet(latitude.toString() + ", " + longitude.toString());

                markerOptions.position(new LatLng(latitude, longitude));
                googleMap.addMarker(markerOptions);

                googleMap.setOnInfoWindowClickListener(infoWindowClickListener);
            }
        });
    }
}