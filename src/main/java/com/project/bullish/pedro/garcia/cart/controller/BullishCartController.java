package com.project.bullish.pedro.garcia.cart.controller;

import com.project.bullish.pedro.garcia.api.exception.BullishNullCartException;
import com.project.bullish.pedro.garcia.api.exception.BullishProductNotFoundException;
import com.project.bullish.pedro.garcia.cart.data.CartData;
import com.project.bullish.pedro.garcia.cart.facade.BullishCartFacade;
import com.project.bullish.pedro.garcia.product.data.ProductData;
import com.project.bullish.pedro.garcia.product.facade.BullishProductFacade;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.project.bullish.pedro.garcia.constants.BullishCoreConstants.ERROR_PRODUCT_NOT_FOUND_CODE;

@RestController
@RequestMapping("/api/v1/cart")
public class BullishCartController {

    @Resource
    private BullishCartFacade bullishCartFacade;

    @Resource
    private BullishProductFacade bullishProductFacade;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{clientId}/addProductById/{productId}")
    public ResponseEntity<CartData> addProductByIdToCart(
            @PathVariable final String clientId,
            @PathVariable final long productId) throws BullishNullCartException {
        CartData cart = bullishCartFacade.getOrCreateCartById(clientId);
        ProductData productData = bullishProductFacade.getProductById(productId);

        if (productData == null) {
            throw new BullishProductNotFoundException(ERROR_PRODUCT_NOT_FOUND_CODE, productId);
        }
        bullishCartFacade.addProductToCart(cart, productData);

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("/getCartByClientId/{clientId}")
    public ResponseEntity<CartData> getCartByClientId(
            @PathVariable final String clientId) {
        CartData cart = bullishCartFacade.getOrCreateCartById(clientId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @DeleteMapping("/{clientId}/removeProduct/{productId}")
    public ResponseEntity<CartData> removeProductFromCart(
            @PathVariable final String clientId,
            @PathVariable final long productId) throws BullishNullCartException {
        return new ResponseEntity<>(bullishCartFacade.removeProductFromCartById(clientId, productId)
                .orElseThrow(() -> new BullishNullCartException("Cart not found for ID: " + clientId)), HttpStatus.OK);
    }

}
