package com.example.googlemappromt;

import android.app.Activity;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.example.googlemappromt.gps.StartLocationAlert;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends FragmentActivity implements LocationListener,OnMapReadyCallback {

    public static final String TAG = "MainActivity";
    Activity mContext = MainActivity.this;
    LocationManager locationManager;
    private GoogleMap mMap;
    Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true){
            Log.e("onResume","ifWorked");
            StartLocationAlert startLocationAlert = new StartLocationAlert(mContext);
            requestLocationUpdates();

        }else{

            requestLocationUpdates();
        }


    }

    public void requestLocationUpdates() {
       // Log.e(TAG,"requestLocationUpdates");
        if (locationManager != null) {
            // LocationListener mLocationListener = new MainActivity();
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                //                                         );
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }


    @Override
    public void onLocationChanged(Location location) {
       // Toast.makeText(MainActivity.this, "Lat" + location.getLatitude() + " Lon" + location.getLongitude(), Toast.LENGTH_SHORT).show();
        this.location = location;
        Log.e(TAG,"Lat" + location.getLatitude() + " Lon" + location.getLongitude());
        if(this.mMap != null && this.location !=null){
            addMarker();
        }else{
            Log.e(TAG,"mMap and location are null");
        }
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        if(this.mMap != null){
            mMap.animateCamera(CameraUpdateFactory.zoomBy(15));
            addMarker();
        }
    }

    public void addMarker(){
        if(this.location != null){

            LatLng sydney = new LatLng(this.location.getLatitude(), this.location.getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(sydney).title("Your Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        }else{
            Toast.makeText(MainActivity.this, "Please wait", Toast.LENGTH_SHORT).show();
        }
    }
}
