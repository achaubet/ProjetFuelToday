package fr.univpau.fueltoday;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

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
/*
        TextView idTextView = convertView.findViewById(R.id.idTextView);
        TextView addressTextView = convertView.findViewById(R.id.addressTextView);
        TextView cityTextView = convertView.findViewById(R.id.cityTextView);

        Station station = (Station) getItem(position);

        idTextView.setText(String.valueOf(station.id));
        addressTextView.setText(station.address);
        cityTextView.setText(station.city);
*/
        TextView textAdr = convertView.findViewById(R.id.textAdr);
        TextView textPrix = convertView.findViewById(R.id.textPrix);
        TextView textOuv = convertView.findViewById(R.id.textOuv);
        TextView textDist = convertView.findViewById(R.id.textDist);

        LinearLayout backgroundlinear = convertView.findViewById(R.id.backgroundlinear);

        Station station = (Station) getItem(position);

        String adresseVille = station.address + ", " + station.city;

        textAdr.setText(adresseVille);
        textPrix.setText(String.valueOf(station.id)); // id pour le moment mais faudra récup le prix plus tard
        if ( station.id < 64140005) {
            backgroundlinear.setBackgroundColor(Color.parseColor("#e8712d"));           // modifier pour que ce soit en fonction du prix la couleur
        }

        Log.d("lalong", "getView: " + station.latitude);

        String testval = String.valueOf(distance(43.33154091, -0.37036942, station.latitude, station.longitude));

        Log.i("cladistlat", "cladist " + station.latitude);
        Log.i("cladistlon", "cladist " + station.longitude);
        Log.i("distance", "cladist " + testval);


        return convertView;
    }

    private double distance(double lat1, double lon1, double lat2, double
            lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist); }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0); }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI); }
}

