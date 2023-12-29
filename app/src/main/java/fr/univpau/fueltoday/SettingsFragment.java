package fr.univpau.fueltoday;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import androidx.preference.SeekBarPreference;

import java.util.Set;


public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.grey));
        //view.setBackgroundResource(R.drawable.rounded_corner_parameter);

        return view;
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

    private void setTextColorForPreferenceScreen(PreferenceGroup preferenceGroup, int textColor) {
        int preferenceCount = preferenceGroup.getPreferenceCount();
        for (int i = 0; i < preferenceCount; i++) {
            Preference preference = preferenceGroup.getPreference(i);
            if (preference instanceof PreferenceGroup) {
                setTextColorForPreferenceScreen((PreferenceGroup) preference, textColor);
            } else {
                // Use custom layout for setting text color
                preference.setLayoutResource(R.layout.activity_settings);
                preference.setWidgetLayoutResource(0);
            }
        }
    }
}