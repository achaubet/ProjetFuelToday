package fr.univpau.fueltoday;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

public class MapButtonClickListener implements View.OnClickListener {

    private Context context;
    private Station station;

    public MapButtonClickListener(Context context, Station station) {
        this.context = context;
        this.station = station;
    }

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

    private boolean adresseCommenceParNombre(String adresse) {
        return adresse.matches("^\\d.*");
    }
}
