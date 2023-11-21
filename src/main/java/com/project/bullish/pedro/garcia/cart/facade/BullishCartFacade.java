package com.project.bullish.pedro.garcia.cart.facade;

import com.project.bullish.pedro.garcia.api.exception.BullishNullCartException;
import com.project.bullish.pedro.garcia.cart.data.CartData;
import com.project.bullish.pedro.garcia.product.data.ProductData;

import java.util.Optional;

public interface BullishCartFacade {

    CartData getOrCreateCartById(String clientId);

    CartData getCartById(String clientId);

    void addProductToCart(CartData cart, ProductData product) throws BullishNullCartException;

    Optional<CartData> removeProductFromCartById(String cartId, Long productId) throws BullishNullCartException;

}
