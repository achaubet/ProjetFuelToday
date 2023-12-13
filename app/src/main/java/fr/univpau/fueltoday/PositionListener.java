package fr.univpau.fueltoday;

import android.location.Location;
import android.location.LocationListener;

import androidx.annotation.NonNull;

public class PositionListener implements LocationListener {
    @Override
    public void onLocationChanged(@NonNull Location location) {
        // String longitude = "Longitude: " + location.getLongitude();
        // Log.i("Positions", longitude);
        // String latitude = "Latitude: " + location.getLatitude();
        // Log.i("Positions", latitude);
        StationsShared.getInstance().latitude = location.getLongitude();
        StationsShared.getInstance().longitude = location.getLatitude();
    }
}
