package com.example.abhishekshukla.shopapp.dto;

import lombok.Data;

/**
 * Created by abhishekshukla on 18/4/15.
 */
@Data
public class CartItem extends Product{
    private int itemCount = 1;
}
