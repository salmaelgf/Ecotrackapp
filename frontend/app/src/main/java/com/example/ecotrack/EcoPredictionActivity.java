package com.example.ecotrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EcoPredictionActivity extends AppCompatActivity {

    private TextView predictionResult;
    private TextView productInfo;
    private ImageView productImage;
    private String barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eco_prediction);


        productInfo = findViewById(R.id.product_info);
        productImage = findViewById(R.id.product_image);

        findViewById(R.id.ask_ecotrack_button).setOnClickListener(v -> {
            Intent intent = new Intent(EcoPredictionActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // 📦 Receive product info from Intent
        barcode = getIntent().getStringExtra("barcode");
        String name = getIntent().getStringExtra("name");
        String brand = getIntent().getStringExtra("brand");
        String category = getIntent().getStringExtra("category");
        String packaging = getIntent().getStringExtra("packaging");
        String quantity = getIntent().getStringExtra("quantity");
        String ingredients = getIntent().getStringExtra("ingredients");
        String carbon = getIntent().getStringExtra("carbon");
        String imageUrl = getIntent().getStringExtra("image_url");
        double nutritionScore = getIntent().getDoubleExtra("nutrition_score", -1);


        String additives = getIntent().getStringExtra("additives");
        String nutritionGrade = getIntent().getStringExtra("nutrition_grade");
        double energy = getIntent().getDoubleExtra("energy", -1);
        double sugars = getIntent().getDoubleExtra("sugars", -1);
        double fat = getIntent().getDoubleExtra("fat", -1);
        double proteins = getIntent().getDoubleExtra("proteins", -1);
        double salt = getIntent().getDoubleExtra("salt", -1);

        // 🖼️ Show image if available
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(productImage);
        }

        // 📝 Show product info
        String info = "📦 Nom : " + name + "\n" +
                "🏷 Marque : " + brand + "\n" +
                "📚 Catégorie : " + category + "\n" +
                "📦 Emballage : " + packaging + "\n" +
                "⚖ Quantité : " + quantity + "\n" +
                "🥣 Ingrédients : " + ingredients + "\n" +
                "🥄 Additifs : " + additives + "\n" +
                "🔥 Énergie : " + (energy != -1 ? energy + " kJ/100g" : "Non disponible") + "\n" +
                "🍬 Sucres : " + (sugars != -1 ? sugars + " g/100g" : "Non disponible") + "\n" +
                "🧈 Matières grasses : " + (fat != -1 ? fat + " g/100g" : "Non disponible") + "\n" +
                "💪 Protéines : " + (proteins != -1 ? proteins + " g/100g" : "Non disponible") + "\n" +
                "🧂 Sel : " + (salt != -1 ? salt + " g/100g" : "Non disponible") + "\n" +
                "🌿 Empreinte carbone : " + carbon + "\n" +
                "🏅 Score nutrition : " + (nutritionScore != -1 ? nutritionScore : "Non disponible") + "\n" +
                "🅰️ Note nutritionnelle : " + nutritionGrade;

        productInfo.setText(info);

        findViewById(R.id.see_prediction_button).setOnClickListener(v -> {
            if (barcode != null && !barcode.isEmpty()) {
                fetchPredictionAndLaunch(barcode);
            } else {
                Toast.makeText(this, "Aucun code-barres fourni", Toast.LENGTH_SHORT).show();
            }
        });





    }

    private void fetchPredictionAndLaunch(String barcode) {
        FlaskPredictionApi api = RetrofitClient.getFlaskInstance().create(FlaskPredictionApi.class);

        api.getPrediction(barcode).enqueue(new Callback<PredictionResponse>() {
            @Override
            public void onResponse(Call<PredictionResponse> call, Response<PredictionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PredictionResponse result = response.body();

                    Intent intent = new Intent(EcoPredictionActivity.this, PredictionResultActivity.class);
                    intent.putExtra("name", result.getProduct_name());
                    intent.putExtra("predicted_cf", String.valueOf(result.getPredicted_carbon_footprint_100g()));
                    intent.putExtra("impact_category", result.getPredicted_impact_category());
                    startActivity(intent);
                } else {
                    Toast.makeText(EcoPredictionActivity.this, "Réponse invalide de l'API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PredictionResponse> call, Throwable t) {
                Toast.makeText(EcoPredictionActivity.this, "Erreur de prédiction : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
