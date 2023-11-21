package com.project.bullish.pedro.garcia.cart.service;

import com.project.bullish.pedro.garcia.cart.data.CartData;
import com.project.bullish.pedro.garcia.product.data.ProductData;
import jakarta.annotation.Nonnull;

public interface BullishCartService {

    CartData getOrCreateCartById(String clientId);

    CartData getCartById(String clientId);

    void addProductToCart(@Nonnull CartData cart, ProductData product);

    CartData removeProductFromCartById(@Nonnull CartData cart, Long productId);

    void removeProductFromCartIfNeeded(ProductData productData);

    void clearCart(String clientId);

}
