package com.project.bullish.pedro.garcia.order.data;

import com.project.bullish.pedro.garcia.cart.data.CartData;
import com.project.bullish.pedro.garcia.product.data.ProductData;

import java.util.List;

public class OrderData {

    private String id;
    private Double totalAmmount;
    private List<ProductData> productDataList;

    public OrderData() {
    }

    public OrderData(CartData cartData) {
        this.id = cartData.getId();
        this.totalAmmount = cartData.getTotalAmmount();
        this.productDataList = cartData.getProductDataList();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getTotalAmmount() {
        return totalAmmount;
    }

    public void setTotalAmmount(Double totalAmmount) {
        this.totalAmmount = totalAmmount;
    }

    public List<ProductData> getProductDataList() {
        return productDataList;
    }

    public void setProductDataList(List<ProductData> productDataList) {
        this.productDataList = productDataList;
    }
}
