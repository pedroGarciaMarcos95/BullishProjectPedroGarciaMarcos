package com.project.bullish.pedro.garcia.discount.facade;

import com.project.bullish.pedro.garcia.discount.data.DiscountData;
import com.project.bullish.pedro.garcia.product.data.ProductData;

import java.util.Optional;

public interface BullishDiscountFacade {

    DiscountData createDiscount(DiscountData discountData);

    Optional<ProductData> assignDiscountToProduct(Long productId, Long discountId);

    Optional<ProductData> createAndAssignDiscountToProduct(DiscountData discountData, Long productId);


}
