package fr.univpau.fueltoday;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StationDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_detail);
        String value = "marchepas";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {


            TextView textSP95 = findViewById(R.id.textSP95);
            TextView textE10 = findViewById(R.id.textE10);
            TextView textE85 = findViewById(R.id.textE85);
            TextView textSP98 = findViewById(R.id.textSP98);
            TextView textGazole = findViewById(R.id.textGazole);
            TextView textGPLc = findViewById(R.id.textGPLc);

            String prixSP95 = extras.getString("stationSP95") + "€";
            String prixE10 = extras.getString("stationE10") + "€";
            String prixE85 = extras.getString("stationE85") + "€";
            String prixSP98 = extras.getString("stationSP98") + "€";
            String prixGazole = extras.getString("stationGazole") + "€";
            String prixGPLc = extras.getString("stationGPLc") + "€";
            textSP95.setText(prixSP95);
            textE10.setText(prixE10);
            textE85.setText(prixE85);
            textSP98.setText(prixSP98);
            textGazole.setText(prixGazole);
            textGPLc.setText(prixGPLc);

        }
    }
}

