package com.project.bullish.pedro.garcia.discount.service;

import com.project.bullish.pedro.garcia.cart.data.CartData;
import com.project.bullish.pedro.garcia.discount.data.DiscountData;
import com.project.bullish.pedro.garcia.product.data.ProductData;

public interface BullishDiscountService {

    DiscountData createDiscount(DiscountData discountData);

    DiscountData getDiscountById(Long discountId);

    boolean isDiscountApplicable(CartData cartData, ProductData productData, int productCount);

    double applyDiscount(CartData cartData, ProductData product);

}
