package fr.univpau.fueltoday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_station, parent, false);
        }

        TextView idTextView = convertView.findViewById(R.id.idTextView);
        TextView addressTextView = convertView.findViewById(R.id.addressTextView);
        TextView cityTextView = convertView.findViewById(R.id.cityTextView);

        Station station = (Station) getItem(position);

        idTextView.setText(String.valueOf(station.id));
        addressTextView.setText(station.address);
        cityTextView.setText(station.city);

        return convertView;
    }
}
