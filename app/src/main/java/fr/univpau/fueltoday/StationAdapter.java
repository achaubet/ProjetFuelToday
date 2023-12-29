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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        double prix = 0.0;
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

        final double prixFinal = prix;
        textPrix.setText(String.valueOf(prix) + "€");
        if ( prix >= 1.86) {
            backgroundlinear.setBackgroundResource(R.drawable.rounded_corner_bottomred);
        } else if (prix >= 1.82) {
            backgroundlinear.setBackgroundResource(R.drawable.rounded_corner_bottomorange);
        } else {
            backgroundlinear.setBackgroundResource(R.drawable.rounded_corner_bottomgreen);
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

        imageButton.setOnClickListener(new MapButtonClickListener(context, station));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<String> serv = station.services;
                Station selectedStation = (Station) getItem(position);
                Log.i("POSITEM", "sqd" + String.valueOf(selectedStation));
                Intent intent = new Intent(context, StationDetailActivity.class);

                intent.putExtra("stationAdress", station.address);
                intent.putExtra("stationSP95", String.valueOf(station.sp95_prix));
                intent.putExtra("stationE10", String.valueOf(station.e10_prix));
                intent.putExtra("stationE85", String.valueOf(station.e85_prix));
                intent.putExtra("stationSP98", String.valueOf(station.sp98_prix));
                intent.putExtra("stationGazole", String.valueOf(station.gasoil_prix));
                intent.putExtra("stationGPLc", String.valueOf(station.gplc_prix));

                intent.putExtra("distancedetail", distarrondi);
                intent.putExtra("typessdetail", carburant);
                intent.putExtra("prixdetail", prixFinal);

                ArrayList<String> servicesList = new ArrayList<>(serv);
                intent.putStringArrayListExtra("servicesdetail", servicesList);

                // Démarrer l'activité
                context.startActivity(intent);
            }
        });


        return convertView;
    }
    private boolean adresseCommenceParNombre(String adresse) {
        return adresse.matches("^\\d.*");
    }
}

