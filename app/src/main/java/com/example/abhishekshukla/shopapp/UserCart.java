package com.example.abhishekshukla.shopapp;

import com.example.abhishekshukla.shopapp.dto.CartItem;
import com.example.abhishekshukla.shopapp.dto.Product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by abhishekshukla on 29/3/15.
 */
public class UserCart {

    private Map<Long, CartItem> itemMap = new HashMap<Long, CartItem>();
    private UserCart() {

    }
    final static UserCart  userCart = new UserCart();

    public static UserCart getInstance()
    {
        return userCart;
    }

    public void addItem(Product product) {
        if(null != itemMap.get(product.getId()))
        {
            itemMap.get(product.getId()).setItemCount(itemMap.get(product.getId()).getItemCount() + 1);
        }
        else
        {
            CartItem cartItem = new CartItem();
            cartItem.setId(product.getId());
            cartItem.setTitle(product.getTitle());
            cartItem.setAbout(product.getAbout());
            cartItem.setPrice(product.getPrice());
            cartItem.setBrand(product.getBrand());
            cartItem.setItemCount(1);
            cartItem.setImageUrl(product.getImageUrl());
            cartItem.setUrl(product.getUrl());
            cartItem.setRelatedItems(product.getRelatedItems());
            cartItem.setSizePriceList(product.getSizePriceList());
            cartItem.setSaving(product.getSaving());
            itemMap.put(cartItem.getId(), cartItem);
        }
    }

    public int getCartSize()
    {
        return itemMap.size();
    }

    public List<CartItem> getAllProducts()
    {
        return new ArrayList(itemMap.values());
    }

    public void removeItem(Long id) {
        itemMap.remove(id);
        removeItemFromDb(id);
    }

    public void removeItemFromDb(Long id) {};

    public void saveOrUpdateItemFromDb(CartItem item) {
        if(null != item)
        {
            itemMap.put(item.getId(), item);
        }
    };

    public void loadFromDisk() {
        itemMap = new HashMap<Long, CartItem>();
    }

    public void cleanCart()
    {
        for (Long id : itemMap.keySet())
        {
            removeItemFromDb(id);
        }
        itemMap.clear();
    }
}
