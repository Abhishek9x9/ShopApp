package com.example.abhishekshukla.shopapp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abhishekshukla on 29/3/15.
 */
public class UserCart {

    private Map<String, String> itemMap = new HashMap<String, String>();
    private UserCart() {

    }
    final static UserCart  userCart = new UserCart();

    public static UserCart getInstance()
    {
        return userCart;
    }
    public int getCartSize()
    {
        return itemMap.size();
    }
}
