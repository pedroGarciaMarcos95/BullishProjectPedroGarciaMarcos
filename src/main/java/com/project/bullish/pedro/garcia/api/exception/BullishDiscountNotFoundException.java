package com.project.bullish.pedro.garcia.api.exception;

import static com.project.bullish.pedro.garcia.constants.BullishCoreConstants.PRODUCT_NOT_FOUND;

public class BullishDiscountNotFoundException extends RuntimeException {

    private final String key;
    private final Long param;

    public BullishDiscountNotFoundException(String key, Long params) {
        super(key);
        this.key = PRODUCT_NOT_FOUND;
        this.param = params;
    }

    public String getKey() {
        return key;
    }

    public Long getParam() {
        return param;
    }

    public String toString() {
        return String.format("BullishDiscountNotFoundException {messageKey: %s, Product ID: %s }", super.getMessage(), getParam());
    }
}
