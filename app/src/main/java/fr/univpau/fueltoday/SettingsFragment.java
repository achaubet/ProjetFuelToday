package fr.univpau.fueltoday;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Set;


public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("radius_key")) {
            int radiusValue = sharedPreferences.getInt("radius_key", 5);
            Log.i("updatedPref", "Radius: " + radiusValue + " km");
        } else if (key.equals("petrol_type_key")) {
            String petrolTypeValue = sharedPreferences.getString("petrol_type_key", "undefined");
            Log.i("updatedPref", "Petrol Type: " + petrolTypeValue);
        } else if (key.equals("services_type_key")) {
            Set<String> services =  sharedPreferences.getStringSet("services_type_key", null);
            services.forEach((str) -> {
                Log.i("updatedPref", "Service type: " + str);
            });
        }
    }
}