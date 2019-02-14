package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();

        client = LocationServices.getFusedLocationProviderClient(this);

        Button button = findViewById(R.id.getLocation);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    return;

                client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>(){
                    public void onSuccess(Location location){
                        if(location != null){
                            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            List<Address> addresses  = null;

                            try {
                                addresses = geocoder.getFromLocation(latitude,longitude, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Address add = addresses.get(0);
                            String addressString = add.getAddressLine(0);
                            String country = add.getCountryName();
                            String city = add.getLocality();
                            String test = addressString + country + city;

                            TextView textView = findViewById(R.id.location);
                            textView.setText(test);
                        }
                    }
                });
            }
        });
    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }
}
