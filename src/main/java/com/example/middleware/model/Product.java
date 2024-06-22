package com.example.middleware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "product")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private int id;

    @Column(name = "title")
    @JsonProperty("title")
    private String title;

    @Column(name = "description")
    @JsonProperty("description")
    private String description;

    @Column(name = "price")
    @JsonProperty("price")
    private double price;

    @Column(name = "category")
    @JsonProperty("category")
    private String category;

    @Column(name = "thumbnail")
    @JsonProperty("thumbnail")
    private String thumbnail;

    public Product(){}

    public Product(int id, String title, String description, double price, String category, String thumbnail) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.thumbnail = thumbnail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
