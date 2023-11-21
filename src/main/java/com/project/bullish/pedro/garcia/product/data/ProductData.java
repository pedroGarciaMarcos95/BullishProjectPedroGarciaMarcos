package com.project.bullish.pedro.garcia.product.data;

import com.project.bullish.pedro.garcia.discount.data.DiscountData;

public class ProductData {

    private Long id;
    private String name;
    private double price;
    private DiscountData discount;

    public ProductData() {
    }

    public ProductData(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public ProductData(String name, double price, DiscountData discount) {
        this.name = name;
        this.price = price;
        this.discount = discount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public DiscountData getDiscount() {
        return discount;
    }

    public void setDiscount(DiscountData discount) {
        this.discount = discount;
    }

}
