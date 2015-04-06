package com.example.abhishekshukla.shopapp;

import com.example.abhishekshukla.shopapp.dto.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by abhishekshukla on 29/3/15.
 */
public class UserCart {

    private List<String> itemMap = new ArrayList<String>();
    private UserCart() {

    }
    final static UserCart  userCart = new UserCart();

    public static UserCart getInstance()
    {
        return userCart;
    }
    public void addItem(Product product) {itemMap.add("" + product.getId());}
    public int getCartSize()
    {
        return itemMap.size();
    }
}
