package org.example.ecotrack1.client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class SmartAlternativesEngine {

    private final OkHttpClient client;

    public SmartAlternativesEngine() {
        this.client = new OkHttpClient.Builder()
                .callTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public List<String> suggestAlternatives(String category, String name, String brand, double currentFootprint) {
        List<String> alternatives = new ArrayList<>();

        List<String> searchTerms = new ArrayList<>();
        if (category != null && !category.isBlank()) {
            searchTerms.add("category/" + sanitize(category));
        }
        if (name != null && !name.isBlank()) {
            searchTerms.add("product_name/" + sanitize(name));
        }
        if (brand != null && !brand.isBlank()) {
            searchTerms.add("brand/" + sanitize(brand));
        }

        for (String term : searchTerms) {
            String searchUrl = "https://world.openfoodfacts.org/" + term + ".json";

            Request request = new Request.Builder()
                    .url(searchUrl)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String json = response.body().string();
                    JsonObject root = new Gson().fromJson(json, JsonObject.class);
                    JsonArray products = root.getAsJsonArray("products");

                    for (JsonElement elem : products) {
                        JsonObject prod = elem.getAsJsonObject();
                        if (prod.has("ecoscore_data")) {
                            JsonObject ecoscoreData = prod.getAsJsonObject("ecoscore_data");
                            if (ecoscoreData.has("agribalyse")) {
                                JsonObject agribalyse = ecoscoreData.getAsJsonObject("agribalyse");
                                if (agribalyse.has("co2_total") && !agribalyse.get("co2_total").isJsonNull()) {
                                    double co2 = agribalyse.get("co2_total").getAsDouble();
                                    if (co2 < currentFootprint && prod.has("product_name")) {
                                        String altName = prod.get("product_name").getAsString();
                                        if (!altName.isBlank()) {
                                            alternatives.add(altName);
                                            if (alternatives.size() >= 5) return alternatives;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("❌ Erreur API pour " + term + ": " + e.getMessage());
            }
        }

        return alternatives;
    }

    private String sanitize(String input) {
        return URLEncoder.encode(input.toLowerCase().replace(" ", "-"), StandardCharsets.UTF_8);
    }
}
