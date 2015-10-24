package com.example.abhishekshukla.shopapp.dto;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import lombok.Data;

/**
 * Created by abhishekshukla on 1/4/15.
 */
@Data
public class Product implements Serializable {
    private long id;
    private String brand;
    private String category;
    private String title;
    private String imageUrl;
    private String saving;
    private String originalPrice;
    private String sellingPrice;
}