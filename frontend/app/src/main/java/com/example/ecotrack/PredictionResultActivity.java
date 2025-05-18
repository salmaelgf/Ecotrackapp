package com.example.ecotrack;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PredictionResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction_result);

        TextView resultText = findViewById(R.id.prediction_result_text);

        String name = getIntent().getStringExtra("name");
        String carbon = getIntent().getStringExtra("predicted_cf");
        String impact = getIntent().getStringExtra("impact_category");

        String result = "🛍️ Produit : " + name + "\n\n" +
                "🌿 Empreinte carbone estimée : " + carbon + " g/100g\n" +
                "📊 Catégorie d’impact : " + impact;

        resultText.setText(result);
    }
}
