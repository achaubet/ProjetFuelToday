package fr.univpau.fueltoday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.Manifest;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    Location gps_loc;
    ListFuelStations listFuelStations;
    SwipeRefreshLayout swipeRefreshLayout;
    RefreshListener refreshListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.listFuelStations = new ListFuelStations(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        PositionListener positionListener = new PositionListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, positionListener);

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
            StationsShared.getInstance().latitude = latitude;
            StationsShared.getInstance().longitude = longitude;
        }
        this.swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        this.refreshListener = new RefreshListener(this);
        this.swipeRefreshLayout.setOnRefreshListener(this.refreshListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new StationRPC(this).execute();

    }

    protected void updateStations(JSONObject stations) {
        StationsShared.getInstance().stations = stations;
        try {
            this.listFuelStations.updateStationsList();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    protected void updateLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
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
            StationsShared.getInstance().latitude = latitude;
            StationsShared.getInstance().longitude = longitude;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLocation();
        new StationRPC(this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            // Open SettingsActivity when the settings button is clicked
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}