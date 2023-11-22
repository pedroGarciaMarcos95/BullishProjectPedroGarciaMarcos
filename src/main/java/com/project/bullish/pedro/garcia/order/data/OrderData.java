package com.project.bullish.pedro.garcia.order.data;

import com.project.bullish.pedro.garcia.cart.data.CartData;
import com.project.bullish.pedro.garcia.product.data.ProductData;

import java.util.List;

public class OrderData {

    private String id;
    private Double totalAmount;
    private List<ProductData> productDataList;

    public OrderData() {
    }

    public OrderData(CartData cartData) {
        this.id = cartData.getId();
        this.totalAmount = cartData.getTotalAmmount();
        this.productDataList = cartData.getProductDataList();
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
