package com.project.bullish.pedro.garcia.discount.data;

public class DiscountData {

    private Long id;
    private boolean isPercentage;
    private int minNumberOfProducts;
    private double value;
    private boolean isApplied;

    public DiscountData() {
    }

    public DiscountData(boolean isPercentage, int minNumberOfProducts, double value) {
        this.isPercentage = isPercentage;
        this.minNumberOfProducts = minNumberOfProducts;
        this.value = value;
        this.isApplied = false;
    }

    public DiscountData(boolean isPercentage, double value) {
        this.isPercentage = isPercentage;
        this.minNumberOfProducts = 1;
        this.value = value;
        this.isApplied = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isPercentage() {
        return isPercentage;
    }

    public void setPercentage(boolean percentage) {
        isPercentage = percentage;
    }

    public int getMinNumberOfProducts() {
        return minNumberOfProducts;
    }

    public void setMinNumberOfProducts(int minNumberOfProducts) {
        this.minNumberOfProducts = minNumberOfProducts;
    }

    public boolean isApplied() {
        return isApplied;
    }

    public void setApplied(boolean applied) {
        isApplied = applied;
    }
}
