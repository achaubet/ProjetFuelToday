package fr.univpau.fueltoday;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StationRPC extends AsyncTask<String, Void, JSONObject> {
    private volatile MainActivity activity;
    private OkHttpClient httpClient;
    private static final double EARTH_RADIUS = 6371.0;
    public StationRPC(MainActivity activity) {
        this.activity = activity;
        this.httpClient = new OkHttpClient();
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        String petrolType = StationsShared.getInstance().carburant;
        String sortBy = StationsShared.getInstance().sortBy;
        Set<String> services = StationsShared.getInstance().services;
        double lon = StationsShared.getInstance().longitude;
        double lat = StationsShared.getInstance().latitude;
        int km = StationsShared.getInstance().rayon;
        int limit = 100;
        String url = "https://data.economie.gouv.fr/api/explore/v2.1/catalog/datasets/prix-des-carburants-en-france-flux-instantane-v2/records?where=within_distance(geom%2C%20GEOM%27POINT(" + lon + "%20" + lat + ")%27%2C%20" + km + "km)&limit=" + limit +"";
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = this.httpClient.newCall(request).execute()) {
            String str_resp = response.body().string();
            JSONObject jsonObject = new JSONObject(str_resp);
            if(services != null) {
                jsonObject = sortByServices(jsonObject, services);
            }
            if(sortBy.equals("byLocation")) {
                jsonObject = sortResultsByDistance(jsonObject, lat, lon);
            } else if (sortBy.equals("byPrice")) {
                jsonObject = sortResultsByPrice(jsonObject, petrolType);
            }
            JSONArray resultsArray = jsonObject.getJSONArray("results");
            return jsonObject;
        } catch (IOException | JSONException e) {
            return  null;
        }
    }

    private JSONObject sortResultsByPrice(JSONObject jsonObject, final String petrolType) {
        try {
            JSONArray resultsArray = jsonObject.getJSONArray("results");

            List<JSONObject> resultList = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                resultList.add(resultsArray.getJSONObject(i));
            }

            resultList.sort(Comparator.comparingDouble(o -> {
                try {
                    Object prixObject = o.get("prix");
                    if(prixObject instanceof String) {
                        prixObject = new JSONArray((String) prixObject);
                    }
                    if(prixObject instanceof JSONArray) {
                        JSONArray pricesArray = (JSONArray) prixObject;
                        for(int j = 0; j < pricesArray.length(); j++) {
                            JSONObject priceObject = pricesArray.getJSONObject(j);
                            String fuelType = priceObject.getString("@nom");
                            if(fuelType.equals(petrolType)) {
                                return priceObject.getDouble("@valeur");
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return Double.MAX_VALUE;
            }));
            JSONArray sortedResultsArray = new JSONArray(resultList);
            jsonObject.put("results", sortedResultsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject sortResultsByDistance(JSONObject jsonObject, final double targetLat, final double targetLon) {
        try {
            JSONArray resultsArray = jsonObject.getJSONArray("results");

            List<JSONObject> resultList = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                resultList.add(resultsArray.getJSONObject(i));
            }

            resultList.sort(Comparator.comparingDouble(o -> {
                try {
                    double lat = o.getJSONObject("geom").getDouble("lat");
                    double lon = o.getJSONObject("geom").getDouble("lon");
                    return calculateDistance(lat, lon, targetLat, targetLon);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return Double.MAX_VALUE;
                }
            }));

            JSONArray sortedResultsArray = new JSONArray(resultList);
            jsonObject.put("results", sortedResultsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject sortByServices(JSONObject jsonObject, Set<String> selectedServices) {
        try {
            JSONArray resultsArray = jsonObject.getJSONArray("results");
            JSONArray filteredResultsArray = new JSONArray();

            for(int i = 0; i < resultsArray.length(); i++) {
                JSONObject stationObject = resultsArray.getJSONObject(i);
                Object servicesField = stationObject.get("services");

                if(servicesField instanceof String) {
                    try {
                        servicesField = new JSONObject((String) servicesField);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        continue;
                    }
                }

                JSONArray stationServices;
                if(servicesField instanceof JSONObject) {
                    stationServices = ((JSONObject) servicesField).optJSONArray("service");
                    if(stationServices == null) {
                        stationServices = new JSONArray();
                        stationServices.put(((JSONObject) servicesField).optString("service"));
                    }
                } else if (servicesField instanceof JSONArray) {
                    stationServices = (JSONArray) servicesField;
                } else {
                    continue;
                }

                Set<String> stationServiceSet = new HashSet<>();
                for(int j = 0; j < stationServices.length(); j++) {
                    String service = stationServices.getString(j);
                    stationServiceSet.add(service);
                }

                if(stationServiceSet.containsAll(selectedServices)) {
                    filteredResultsArray.put(stationObject);
                }
            }
            jsonObject.put("results", filteredResultsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;
        
        return distance;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        this.activity.updateStations(result); // callback
        if(this.activity.refreshListener != null) {
            this.activity.refreshListener.onTaskCompleted();
        }
    }
}
