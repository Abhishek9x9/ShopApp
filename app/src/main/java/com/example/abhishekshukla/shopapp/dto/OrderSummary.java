package com.example.abhishekshukla.shopapp.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by nishantgupta on 28/10/15.
 */
@Getter
@Setter
public class OrderSummary {
    private long orderId;
    private double orderValue;
    private Date orderDate;
    private int orderItemCount;

    public OrderSummary(long orderId, double orderValue, Date orderDate, int orderItemCount){
        this.orderId = orderId;
        this.orderValue = orderValue;
        this.orderDate = orderDate;
        this.orderItemCount = orderItemCount;
    }

}
