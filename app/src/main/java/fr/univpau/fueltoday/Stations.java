package fr.univpau.fueltoday;

import android.app.Application;

import org.json.JSONObject;

public class Stations extends Application {
    private static Stations singleton;
    public JSONObject stations;
    public static Stations getInstance() {
        return singleton;
    }
    @Override
    public final void onCreate() {
        super.onCreate();
        singleton = this;
    }
}
