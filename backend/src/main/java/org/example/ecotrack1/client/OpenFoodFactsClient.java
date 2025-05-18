package org.example.ecotrack1.client;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.example.ecotrack1.model.Product;
import org.example.ecotrack1.model.OpenFoodFactsProduct;
import org.example.ecotrack1.model.ProductResponse;
import org.example.ecotrack1.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class OpenFoodFactsClient {

    private final OpenFoodFactsApi openFoodFactsApi;

    public OpenFoodFactsClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://world.openfoodfacts.org/api/v0/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openFoodFactsApi = retrofit.create(OpenFoodFactsApi.class);
    }

    @Autowired
    private ProductRepository productRepository;

    public Product fetchProduct(String barcode) {
        Call<ProductResponse> call = openFoodFactsApi.getProductDetails(barcode);
        try {
            ProductResponse response = call.execute().body();
            if (response != null && response.getProduct() != null) {
                OpenFoodFactsProduct apiProduct = response.getProduct();
                Product product = new Product();

                // Barcode
                product.setBarcode(apiProduct.getCode() != null ? apiProduct.getCode() : barcode);

                // Name (handle null or empty name)
                String name = apiProduct.getName();
                product.setName((name != null && !name.trim().isEmpty()) ? name : "Nom indisponible");

                // Brand
                String brand = apiProduct.getBrand();
                product.setBrand((brand != null && !brand.trim().isEmpty()) ? brand : "Marque inconnue");

                // Categories Tags
                List<String> categories = apiProduct.getCategoriesTags();
                product.setCategoriesTags(categories != null ? categories : new ArrayList<>());

                // Carbon Footprint
                Double footprint = null;
                if (apiProduct.getEcoscoreData() != null && apiProduct.getEcoscoreData().getAgribalyse() != null) {
                    footprint = apiProduct.getEcoscoreData().getAgribalyse().getCo2Total();
                }
                product.setCarbonFootprint((footprint != null && footprint > 0.0) ? footprint : null);

                // Log it
                System.out.println("✅ Produit analysé : " + product.getName() + ", CO2 : " + product.getCarbonFootprint());

                // Smart Alternatives
                List<String> smartAlternatives = fetchSmartAlternatives(product.getCategoriesTags(), product.getCarbonFootprint());
                product.setAlternatives(smartAlternatives);

                // Save and return
                productRepository.save(product);
                return product;
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la récupération du produit : " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }


    private List<String> fetchSmartAlternatives(List<String> categories, Double currentFootprint) {
        List<String> alternatives = new ArrayList<>();

        if (categories == null || categories.isEmpty() || currentFootprint == null) return alternatives;

        String category = categories.get(0).replace("en:", "");
        String url = "https://world.openfoodfacts.org/category/" + category + ".json";

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(10))
                .readTimeout(Duration.ofSeconds(15))
                .callTimeout(Duration.ofSeconds(20))
                .build();

        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String body = response.body().string();
                Gson gson = new Gson();
                com.google.gson.JsonObject root = gson.fromJson(body, com.google.gson.JsonObject.class);
                com.google.gson.JsonArray products = root.getAsJsonArray("products");

                int count = 0;
                for (com.google.gson.JsonElement elem : products) {
                    com.google.gson.JsonObject p = elem.getAsJsonObject();
                    if (p.has("ecoscore_data")) {
                        com.google.gson.JsonObject ecoscore = p.getAsJsonObject("ecoscore_data");
                        if (ecoscore.has("agribalyse")) {
                            com.google.gson.JsonObject agribalyse = ecoscore.getAsJsonObject("agribalyse");
                            if (agribalyse.has("co2_total")) {
                                double altFootprint = agribalyse.get("co2_total").getAsDouble();
                                if (altFootprint > 0 && altFootprint < currentFootprint) {
                                    String name = p.has("product_name") ? p.get("product_name").getAsString() : "Produit alternatif";
                                    alternatives.add(name + " (" + altFootprint + " CO₂e/100g)");
                                    count++;
                                }
                            }
                        }
                    }
                    if (count >= 3) break;
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to fetch smart alternatives: " + e.getMessage());
        }

        return alternatives;
    }
}
