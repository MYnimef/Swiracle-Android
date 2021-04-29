package com.mynimef.swiracle.api.database;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.mynimef.swiracle.api.Price;

@Entity(tableName = "clothes_element_table")
public class ClothesElement {
    @PrimaryKey(autoGenerate = true)
    private int id;
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    private int postId;
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    private final String brand;
    private final String description;
    @Embedded
    private final Price price;
    private final String url;

    public ClothesElement(String brand, String description, Price price, String url) {
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.url = url;
    }

    public String getBrand() { return brand; }
    public String getDescription() { return description; }
    public Price getPrice() { return price; }
    public String getUrl() { return url; }
}