package fr.univpau.fueltoday;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
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

import java.text.DecimalFormat;
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
            case "Gazole":
                prix = station.gasoil_prix;
                break;
            case "E85":
                prix = station.e85_prix;
                break;
            case "GPLc":
                prix = station.gplc_prix;
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
        double lonutil = StationsShared.getInstance().longitude;


        Location startPoint = new Location("locationA");
        startPoint.setLatitude(latutil);
        startPoint.setLongitude(lonutil);
        Location endPoint = new Location("locationA");
        endPoint.setLatitude(station.latitude);
        endPoint.setLongitude(station.longitude);
        double distance = startPoint.distanceTo(endPoint);

        double dist = distance / 1000;
        DecimalFormat decimaldist = new DecimalFormat("#.#");
        String distarrondi = decimaldist.format(dist);

        textDist.setText(distarrondi + "km");

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
}

