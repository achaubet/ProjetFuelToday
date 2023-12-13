package fr.univpau.fueltoday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.Manifest;
import android.util.Log;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    Location gps_loc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        PositionListener positionListener = new PositionListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, positionListener);

        try {
            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (gps_loc != null) {
            Location final_loc = gps_loc;
            double latitude = final_loc.getLatitude();
            double longitude = final_loc.getLongitude();
            Log.i("LocationLat", String.valueOf(latitude));
            Log.i("LocationLon", String.valueOf(longitude));
            Stations.getInstance().latitude = latitude;
            Stations.getInstance().longitude = longitude;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        new StationRPC(this).execute();
    }

    protected void updateStations(JSONObject stations) {
        Stations.getInstance().stations = stations;
    }

}