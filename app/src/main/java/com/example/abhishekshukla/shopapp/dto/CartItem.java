package com.example.abhishekshukla.shopapp.dto;

/**
 * Created by abhishekshukla on 18/4/15.
 */
public class CartItem extends Product{
    private int itemCount;

    public CartItem()
    {
        super();
        itemCount = 1;
    }

    public int getItemCount()
    {
        return itemCount;
    }

    public void setItemCount(int itemCount)
    {
        this.itemCount = itemCount;
    }
}
