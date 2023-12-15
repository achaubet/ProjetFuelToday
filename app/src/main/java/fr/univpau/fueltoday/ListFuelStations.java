package fr.univpau.fueltoday;

import android.app.Activity;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListFuelStations {
    private Activity activity;
    public ListFuelStations(Activity activity) {
        this.activity = activity;
    }
    public void updateStationsList() throws JSONException {
        JSONObject stations = StationsShared.getInstance().stations;
        ListView listView = activity.findViewById(R.id.listView);
        JSONArray resultsArray = stations.getJSONArray("results");
        List<Station> stationList = new ArrayList<>();
        for(int i = 0; i < resultsArray.length(); i++) {
            JSONObject firstResult = resultsArray.getJSONObject(i);
            int id = firstResult.getInt("id");

            JSONObject geomObject = firstResult.getJSONObject("geom");
            double latitude = geomObject.getDouble("lat");
            double longitude = geomObject.getDouble("lon");
            String cp = firstResult.getString("cp");
            String pop = firstResult.getString("pop");
            String address = firstResult.getString("adresse");
            String city = firstResult.getString("ville");
            Station station = new Station(id, latitude, longitude, cp, pop, address, city);
            stationList.add(station);
        }
        StationAdapter adapter = new StationAdapter(activity, stationList);
        listView.setAdapter(adapter);
    }
}
