package fr.univpau.fueltoday;

import android.app.Activity;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

            String prixString = firstResult.getString("prix");
            JSONArray prixArray = new JSONArray(prixString);

            double sp95_prix = 0.0;
            double e10_prix = 0.0;
            double sp98_prix = 0.0;
            double gasoilPrix = 0.0;

            for (int j = 0; j < prixArray.length(); j++) {
                JSONObject prixObject = prixArray.getJSONObject(j);
                String carburant = prixObject.getString("@nom");
                double valeur = prixObject.getDouble("@valeur");
                switch (carburant) {
                    case "SP95":
                        sp95_prix = valeur;
                        break;
                    case "E10":
                        e10_prix = valeur;
                        break;
                    case "SP98":
                        sp98_prix = valeur;
                        break;
                    case "Gazole":
                        gasoilPrix = valeur;
                        break;
                }
            }
            Set<String> servicesSet = new HashSet<>();
            try {
                JSONArray servicesArray = firstResult.getJSONArray("services_service");
                for (int j = 0; j < servicesArray.length(); j++) {
                    servicesSet.add(servicesArray.getString(j));
                }
            } catch(JSONException e) {
                e.printStackTrace();
            }
            Station station = new Station(id, latitude, longitude, cp, pop, address, city, sp95_prix, e10_prix, sp98_prix, gasoilPrix, servicesSet);
            stationList.add(station);
        }
        StationAdapter adapter = new StationAdapter(activity, stationList);
        listView.setAdapter(adapter);
    }
}
