package com.project.bullish.pedro.garcia.cart.data;

import com.project.bullish.pedro.garcia.product.data.ProductData;

import java.util.List;

public class CartData {

    private String id;
    private Double totalAmount;
    private List<ProductData> productDataList;

    public CartData() {
    }

    public CartData(List<ProductData> productDataList) {
        this.productDataList = productDataList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<ProductData> getProductDataList() {
        return productDataList;
    }

    public void setProductDataList(List<ProductData> productDataList) {
        this.productDataList = productDataList;
    }
}
