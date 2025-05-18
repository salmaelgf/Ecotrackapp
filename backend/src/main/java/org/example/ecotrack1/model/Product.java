package org.example.ecotrack1.model;

import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String brand;

    @Column(name = "carbon_footprint")
    private Double carbonFootprint;

    private String barcode;

    @ElementCollection
    private List<String> alternatives;

    @Transient
    @SerializedName("categories_tags")
    private List<String> categoriesTags;

    public Product(String name, String brand, Double carbonFootprint, String barcode, List<String> alternatives) {
        this.name = name;
        this.brand = brand;
        this.carbonFootprint = carbonFootprint;
        this.barcode = barcode;
        this.alternatives = alternatives;
    }

    public Product() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getCarbonFootprint() {
        return carbonFootprint;
    }

    public void setCarbonFootprint(Double carbonFootprint) {
        this.carbonFootprint = carbonFootprint;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public List<String> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<String> alternatives) {
        this.alternatives = alternatives;
    }

    public List<String> getCategoriesTags() {
        return categoriesTags;
    }

    public void setCategoriesTags(List<String> categoriesTags) {
        this.categoriesTags = categoriesTags;
    }
}
