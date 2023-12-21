package fr.univpau.fueltoday;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class StationAdapter extends BaseAdapter {

    private Context context;
    private List<Station> stationList;

    public StationAdapter(Context context, List<Station> stationList) {
        this.context = context;
        this.stationList = stationList;
    }

    @Override
    public int getCount() {
        return stationList.size();
    }

    @Override
    public Object getItem(int position) {
        return stationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            //convertView = LayoutInflater.from(context).inflate(R.layout.list_item_station, parent, false);
            convertView = LayoutInflater.from(context).inflate(R.layout.best_station, parent, false);
        }

        TextView textAdr = convertView.findViewById(R.id.textAdr);
        TextView textPrix = convertView.findViewById(R.id.textPrix);
        TextView textEssence = convertView.findViewById(R.id.textEssence);
        TextView textDist = convertView.findViewById(R.id.textDist);
        ImageButton imageButton = convertView.findViewById(R.id.imageButton);

        LinearLayout backgroundlinear = convertView.findViewById(R.id.backgroundlinear);

        Station station = (Station) getItem(position);

        String adresseVille = station.address + ", " + station.city;

        textAdr.setText(adresseVille);
        textEssence.setText(StationsShared.getInstance().carburant);


        String carburant = String.valueOf(StationsShared.getInstance().carburant);
        double prix = 0;  // Initialisation par défaut du prix
        switch (carburant) {
            case "SP95":
                prix = station.sp95_prix;
                break;
            case "E10":
                prix = station.e10_prix;
                break;
            case "SP98":
                prix = station.sp98_prix;
                break;
            case "Gasoil":
                prix = station.gasoil_prix;
                break;
            default:
                prix = 0;
                break;
        }
        textPrix.setText(String.valueOf(prix) + "€");
        if ( prix >= 1.86) {
            //backgroundlinear.setBackgroundColor(Color.parseColor("#e8712d"));           // modifier pour que ce soit en fonction du prix la couleur
            backgroundlinear.setBackgroundResource(R.drawable.rounded_corner_bottomred);
        } else if (prix >= 1.82) {
            backgroundlinear.setBackgroundResource(R.drawable.rounded_corner_bottomorange);
        }

        Log.d("lalong", "getView: " + station.latitude);

        double latutil = StationsShared.getInstance().latitude;
        double lonutil = StationsShared.getInstance().latitude;
        String dist = String.valueOf(calculateDistance(latutil, lonutil, station.latitude, station.longitude) / 1000);

        textDist.setText(dist + "km");

        Log.i("cladistutillat", "cladistlatu" + StationsShared.getInstance().latitude);
        Log.i("cladistutillong", "cladistlon" + StationsShared.getInstance().longitude);
        Log.i("cladistlat", "cladist " + station.latitude);
        Log.i("cladistlon", "cladist " + station.longitude);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Latitude et longitude de la station
                double stationLatitude = station.latitude;
                double stationLongitude = station.longitude;
                String stationCity = station.city;
                String stationAddress = station.address;
                String uri;

                String encodedAdress = Uri.encode(stationAddress + "," + stationCity);
                if (adresseCommenceParNombre(stationAddress)) {
                    uri = "geo:" + stationLatitude + "," + stationLongitude + "?q=" + stationLatitude + "," + stationLongitude + "(" + encodedAdress + ")";
                } else {
                    uri = "geo:" + stationLatitude + "," + stationLongitude + "?q=" + stationLatitude + "," + stationLongitude;
                }

                Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));

                context.startActivity(Intent.createChooser(mapIntent, "Choisir une application de cartographie"));
            }
        });


        return convertView;
    }
    private boolean adresseCommenceParNombre(String adresse) {
        return adresse.matches("^\\d.*");
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);
        double EARTH_RADIUS = 6371.0;
        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;

        return distance;
    }


    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0); }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI); }
}

