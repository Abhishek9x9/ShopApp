package com.example.abhishekshukla.shopapp.dto;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Created by abhishekshukla on 1/4/15.
 */

public class Product implements Serializable {
    long id;
    String url;
    String brand;
    String title;
    String imageUrl;
    String about;
    Set<String> sizePriceList;
    String saving;
    String price;
    Map<Long, String> relatedItems;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Set<String> getSizePriceList() {
        return sizePriceList;
    }

    public void setSizePriceList(Set<String> sizePriceList) {
        this.sizePriceList = sizePriceList;
    }

    public String getSaving() {
        return saving;
    }

    public void setSaving(String saving) {
        this.saving = saving;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Map<Long, String> getRelatedItems() {
        return relatedItems;
    }

    public void setRelatedItems(Map<Long, String> relatedItems) {
        this.relatedItems = relatedItems;
    }
}