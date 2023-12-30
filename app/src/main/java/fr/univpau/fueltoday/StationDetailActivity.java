package fr.univpau.fueltoday;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StationDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_detail);
        String value = "marchepas";
        Bundle extras = getIntent().getExtras();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (extras != null) {

            TextView textDist = findViewById(R.id.textDist);
            TextView textEssence = findViewById(R.id.textEssence);
            TextView textPrix = findViewById(R.id.textPrix);
            TextView textAdresse = findViewById(R.id.textAdr);
            TextView textOuv = findViewById(R.id.textOuv);


            String distanceDetail = extras.getString("distancedetail");
            String typesDetail = extras.getString("typessdetail");
            String adresseDetail = extras.getString("stationAdress");
            String openDetail = extras.getString("opendetail");
            Double prixDetail = extras.getDouble("prixdetail");

            textDist.setText(distanceDetail + "km");
            textEssence.setText(typesDetail);
            textAdresse.setText(adresseDetail);
            textOuv.setText(openDetail);
            textPrix.setText(String.valueOf(prixDetail) + "€");

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

            Set<String> services = new HashSet<>(getIntent().getStringArrayListExtra("servicesdetail"));
            List<String> servicesList = new ArrayList<>(services);
            RecyclerView recyclerView = findViewById(R.id.listServices);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            ServicesAdapter adapter = new ServicesAdapter(servicesList);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Termine l'activité et retourne à l'activité parente
        return true;
    }
}

