package fr.univpau.fueltoday;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.Manifest;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Location gps_loc;
    LocationManager locationManager;
    PositionListener positionListener;
    ListFuelStations listFuelStations;
    SwipeRefreshLayout swipeRefreshLayout;
    RefreshListener refreshListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.listFuelStations = new ListFuelStations(this);
        this.getPreferences();
        this.swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        this.refreshListener = new RefreshListener(this);
        this.swipeRefreshLayout.setOnRefreshListener(this.refreshListener);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissions();
        } else {
            startLocationManager();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        new StationRPC(this).execute();
    }

    protected void startLocationManager() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            this.positionListener = new PositionListener();
            this.updateLocation();
            this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, positionListener);
        }
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
    }

    protected void updateStations(JSONObject stations) {
        StationsShared.getInstance().stations = stations;
        try {
            this.listFuelStations.updateStationsList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void updateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                gps_loc = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (this.gps_loc != null) {
                Location final_loc = this.gps_loc;
                double latitude = final_loc.getLatitude();
                double longitude = final_loc.getLongitude();
                Log.i("LocationLat", String.valueOf(latitude));
                Log.i("LocationLon", String.valueOf(longitude));
                StationsShared.getInstance().latitude = latitude;
                StationsShared.getInstance().longitude = longitude;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.updateLocation();
        this.getPreferences();
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1) {
                startLocationManager();
            } else {
                Toast.makeText(this, "Location permission denied, the application will not work as intended...", Toast.LENGTH_LONG).show();
            }
    }


    public void getPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int radiusValue = sharedPreferences.getInt("radius_key", 5);
        String petrolTypeValue = sharedPreferences.getString("petrol_type_key", "undefined");
        String sortBy = sharedPreferences.getString("prefered_sort_key", "byLocation");
        Set<String> services = sharedPreferences.getStringSet("services_type_key", null);
        StationsShared.getInstance().rayon = radiusValue;
        StationsShared.getInstance().carburant = petrolTypeValue;
        StationsShared.getInstance().services = services;
        StationsShared.getInstance().sortBy = sortBy;
    }
}