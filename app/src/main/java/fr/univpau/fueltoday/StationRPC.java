package fr.univpau.fueltoday;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StationRPC extends AsyncTask<String, Void, JSONObject> {
    private volatile MainActivity activity;
    private OkHttpClient httpClient;
    public StationRPC(MainActivity activity) {
        this.activity = activity;
        this.httpClient = new OkHttpClient();
    }
    @Override
    protected JSONObject doInBackground(String... strings) {
        double lon = -0.360448;
        double lat = 43.319296;
        int km = 1;
        int limit = 10;
        String url = "https://data.economie.gouv.fr/api/explore/v2.1/catalog/datasets/prix-des-carburants-en-france-flux-instantane-v2/records?where=within_distance(geom%2C%20GEOM%27POINT(" + lon + "%20" + lat + ")%27%2C%20" + km + "km)&limit=" + limit +"";
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = this.httpClient.newCall(request).execute()) {
            String str_resp = response.body().string();
            JSONObject jsonObject = new JSONObject(str_resp);
            Log.i("JSONApp", str_resp);
            int totalCount = jsonObject.getInt("total_count");
            Log.i("JSONAppObj", String.valueOf(totalCount));
            //Stations.getInstance().stations = jsonObject;
            return jsonObject;
        } catch (IOException | JSONException e) {
            return  null;
        }
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        this.activity.updateStations(result); // callback
    }
}
