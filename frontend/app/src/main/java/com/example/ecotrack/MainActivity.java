package com.example.ecotrack;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 101;

    private Button scanButton;
    private TextView productInfo;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private TextView alternativesInfo;
    private Product scannedProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this::handleNavigationItemSelected);

        scanButton = findViewById(R.id.scan_button);
        productInfo = findViewById(R.id.product_info);
        alternativesInfo = findViewById(R.id.alternatives_info);

        checkCameraPermission();

        scanButton.setOnClickListener(v -> startBarcodeScanner());

        FloatingActionButton chatbotFab = findViewById(R.id.chatbot_fab);
        chatbotFab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GeminiChatActivity.class);
            startActivity(intent);
        });
    }

    private boolean handleNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Toast.makeText(this, "Accueil", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this, GeminiChatActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            Toast.makeText(this, "Déconnexion", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        drawerLayout.closeDrawers();
        return true;
    }


    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startBarcodeScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a product barcode");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String barcode = result.getContents();
            if (barcode != null) {
                fetchProductDetails(barcode);
                fetchSmartAlternatives(barcode);
                Button askButton = findViewById(R.id.ask_ecotrack_button);
                askButton.setOnClickListener(v -> {
                    FlaskApi flaskApi = ApiClient.getFlaskRetrofit().create(FlaskApi.class);
                    Call<ProductInput> call = flaskApi.getProductByBarcode(barcode);

                    call.enqueue(new Callback<ProductInput>() {
                        @Override
                        public void onResponse(Call<ProductInput> call, Response<ProductInput> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                ProductInput product = response.body();

                                Intent intent = new Intent(MainActivity.this, EcoPredictionActivity.class);
                                intent.putExtra("barcode", barcode); // 👈 ajoute cette ligne
                                intent.putExtra("name", product.getProduct_name());
                                intent.putExtra("brand", product.getBrands());
                                intent.putExtra("category", product.getCategories());
                                intent.putExtra("packaging", product.getPackaging());
                                intent.putExtra("quantity", product.getQuantity());
                                intent.putExtra("ingredients", product.getIngredients_text());
                                intent.putExtra("carbon", product.getCarbon_footprint_100g() != null
                                        ? product.getCarbon_footprint_100g().toString() : "N/A");
                                intent.putExtra("image_url", product.getImage_url());
                                intent.putExtra("additives", product.getAdditives_tags());
                                intent.putExtra("nutrition_grade", product.getNutrition_grade_fr());
                                intent.putExtra("energy", product.getEnergy_100g());
                                intent.putExtra("sugars", product.getSugars_100g());
                                intent.putExtra("fat", product.getFat_100g());
                                intent.putExtra("proteins", product.getProteins_100g());
                                intent.putExtra("salt", product.getSalt_100g());
                                intent.putExtra("nutrition_score", product.getNutrition_score_fr_100g());

                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, "Produit non trouvé (Flask).", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ProductInput> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Erreur Flask : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                });


            } else {
                productInfo.setText("Scan cancelled.");
            }
        }



    }

    private void fetchProductDetails(String barcode) {
        OpenFoodFactsApi api = RetrofitClient.getRetrofitInstance().create(OpenFoodFactsApi.class);
        Call<Product> call = api.getProductDetails(barcode);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    scannedProduct = response.body();
                    Product product = response.body();
                    Log.d("EcoTrack", "Product JSON: " + new Gson().toJson(product));

                    Double footprint = product.getCarbonFootprint();
                    String carbonText = (footprint != null)
                            ? footprint + " g CO₂e/100g"
                            : "Non disponible";

                    productInfo.setText("Name: " + product.getName() +
                            "\nBrand: " + product.getBrand() +
                            "\nCarbon Footprint: " + carbonText);
                } else {
                    productInfo.setText("Product not found.");
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                productInfo.setText("Error: " + t.getMessage());
            }
        });
    }

    private void fetchSmartAlternatives(String barcode) {
        SmartApi api = RetrofitClient.getRetrofitInstance().create(SmartApi.class);
        Call<SmartAlternativeResponse> call = api.getAlternatives(barcode);
        call.enqueue(new Callback<SmartAlternativeResponse>() {
            @Override
            public void onResponse(Call<SmartAlternativeResponse> call, Response<SmartAlternativeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SmartAlternativeResponse data = response.body();
                    StringBuilder altText = new StringBuilder("🌿 Alternatives suggérées :\n");
                    for (String alt : data.getAlternatives()) {
                        altText.append("- ").append(alt).append("\n");
                    }
                    alternativesInfo.setText(altText.toString());
                } else {
                    alternativesInfo.setText("🌿 Aucune alternative trouvée.");
                }
            }

            @Override
            public void onFailure(Call<SmartAlternativeResponse> call, Throwable t) {
                alternativesInfo.setText("🌿 Erreur lors du chargement des alternatives.");
            }
        });
    }

}
