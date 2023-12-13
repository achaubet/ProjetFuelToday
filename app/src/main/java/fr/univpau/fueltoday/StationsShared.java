package fr.univpau.fueltoday;

import android.app.Application;

import org.json.JSONObject;

public class StationsShared extends Application {
    private static StationsShared singleton;
    public JSONObject stations;
    public double longitude;
    public double latitude;
    public static StationsShared getInstance() {
        return singleton;
    }
    @Override
    public final void onCreate() {
        super.onCreate();
        singleton = this;
    }
}
