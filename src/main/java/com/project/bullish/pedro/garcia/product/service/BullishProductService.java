package com.project.bullish.pedro.garcia.product.service;

import com.project.bullish.pedro.garcia.api.exception.BullishProductException;
import com.project.bullish.pedro.garcia.discount.data.DiscountData;
import com.project.bullish.pedro.garcia.product.data.ProductData;
import jakarta.annotation.Nonnull;

import java.util.List;


public interface BullishProductService {

    ProductData createProduct(ProductData product);

    void deleteProduct(@Nonnull Long productId) throws BullishProductException;

    List<ProductData> getAllProducts();

    ProductData getProductById(Long productId);

    ProductData setDiscountToProduct(DiscountData discountData, Long productId);

}
