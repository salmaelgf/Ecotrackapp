package org.example.ecotrack1.service;

import org.example.ecotrack1.model.ProductInput;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class PredictionService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String flaskApiUrl = "http://localhost:5000/predict"; // OR your server IP

    public double predictCarbonFootprint(ProductInput input) {
        Map<String, Object> request = new HashMap<>();
        request.put("nutrition_score_fr_100g", input.getNutrition_score());
        request.put("categories", input.getCategory());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(flaskApiUrl, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Object result = response.getBody().get("predicted_carbon_footprint_100g");
                return Double.parseDouble(result.toString());
            } else {
                throw new RuntimeException("Flask prediction failed or returned invalid data.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error communicating with Flask API", e);
        }
    }
}
