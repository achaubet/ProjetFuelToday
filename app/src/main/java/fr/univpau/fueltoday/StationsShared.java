package fr.univpau.fueltoday;

import android.app.Application;

import org.json.JSONObject;

import java.util.Set;

public class StationsShared extends Application {
    private static StationsShared singleton;
    public JSONObject stations;
    public double longitude;
    public double latitude;
    public int rayon;
    public String carburant;
    public Set<String> services;
    public String sortBy;
    public static StationsShared getInstance() {
        return singleton;
    }
    @Override
    public final void onCreate() {
        super.onCreate();
        singleton = this;
    }
}
