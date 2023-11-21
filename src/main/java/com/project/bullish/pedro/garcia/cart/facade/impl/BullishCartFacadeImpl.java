package com.project.bullish.pedro.garcia.cart.facade.impl;

import com.project.bullish.pedro.garcia.api.exception.BullishNullCartException;
import com.project.bullish.pedro.garcia.cart.data.CartData;
import com.project.bullish.pedro.garcia.cart.facade.BullishCartFacade;
import com.project.bullish.pedro.garcia.cart.service.BullishCartService;
import com.project.bullish.pedro.garcia.product.data.ProductData;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class BullishCartFacadeImpl implements BullishCartFacade {

    @Resource
    private BullishCartService bullishCartService;

    @Override
    public CartData getOrCreateCartById(String clientId) {
        return bullishCartService.getOrCreateCartById(clientId);
    }

    @Override
    public CartData getCartById(String clientId) {
        return bullishCartService.getCartById(clientId);
    }

    @Override
    public void addProductToCart(CartData cart, ProductData product) throws BullishNullCartException {
        if (Objects.nonNull(cart)) {
            bullishCartService.addProductToCart(cart, product);
        } else {
            throw new BullishNullCartException("The cart must not be null");
        }
    }

    @Override
    public Optional<CartData> removeProductFromCartById(String cartId, Long productId) {
        return Optional.ofNullable(removeCartEntry(cartId, productId));
    }

    public CartData removeCartEntry(String cartId, long productId) {
        CartData cart = getCartById(cartId);
        if (Objects.nonNull(cart)) {
            return bullishCartService.removeProductFromCartById(cart, productId);
        }
        return null;
    }
}
